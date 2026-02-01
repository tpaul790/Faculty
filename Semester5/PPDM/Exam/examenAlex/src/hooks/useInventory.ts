import { useState, useEffect } from 'react';
import { AuditItem, Product } from '../models/types';
import api from '../api/axios';

const STORAGE_KEY_ITEMS = 'audit_items';
const STORAGE_KEY_ZONE = 'audit_zone';

export const useInventory = () => {
    const [items, setItems] = useState<AuditItem[]>([]);
    const [zone, setZone] = useState<string>('');
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [globalError, setGlobalError] = useState<string | null>(null);
    const [isZoneLocked, setIsZoneLocked] = useState<boolean>(false);

    // 1. Inițializare la pornire (Load from LocalStorage)
    useEffect(() => {
        const loadData = () => {
            const savedZone = localStorage.getItem(STORAGE_KEY_ZONE);
            const savedItems = localStorage.getItem(STORAGE_KEY_ITEMS);

            if (savedZone) {
                setZone(savedZone);
                setIsZoneLocked(true);
            }

            if (savedItems) {
                setItems(JSON.parse(savedItems));
            }
        };
        loadData();
    }, []);

    // 2. Conectare WebSocket (doar dacă avem zonă dar nu avem iteme)
    useEffect(() => {
        if (isZoneLocked && items.length === 0) {
            setIsLoading(true);
            const ws = new WebSocket('ws://localhost:3000');

            ws.onopen = () => {
                console.log('WS Connected');
            };

            ws.onmessage = (event) => {
                try {
                    const data: Product[] = JSON.parse(event.data);
                    // Transformăm datele de la server în structura noastră locală
                    const initialItems: AuditItem[] = data.map(p => ({
                        ...p,
                        status: 'idle'
                    }));
                    setItems(initialItems);
                    localStorage.setItem(STORAGE_KEY_ITEMS, JSON.stringify(initialItems));
                    setIsLoading(false);
                    ws.close(); // Închidem conexiunea după ce primim datele (conform cerinței, e one-time fetch logic)
                } catch (e) {
                    console.error('WS Parse Error', e);
                    setGlobalError('Eroare la procesarea datelor de inventar.');
                    setIsLoading(false);
                }
            };

            ws.onerror = (e) => {
                console.error('WS Error', e);
                setGlobalError('Nu s-a putut conecta la serverul de inventar.');
                setIsLoading(false);
            };

            return () => {
                if (ws.readyState === 1) ws.close();
            };
        }
    }, [isZoneLocked, items.length]);

    // Funcție pentru setarea zonei
    const saveZone = (newZone: string) => {
        if (!newZone) return;
        localStorage.setItem(STORAGE_KEY_ZONE, newZone);
        setZone(newZone);
        setIsZoneLocked(true);
    };

    // Funcție pentru actualizarea numărătorii (Counted)
    const updateCount = (code: number, val: number) => {
        const newItems = items.map(it => {
            if (it.code === code) {
                return { ...it, counted: val, status: 'idle' as const, errorMessage: undefined }; // Reset status on edit
            }
            return it;
        });
        setItems(newItems);
        localStorage.setItem(STORAGE_KEY_ITEMS, JSON.stringify(newItems));
    };

    // 7. & 8. & 9. Trimitere Audit (Parallel POSTs)
    const submitAudits = async () => {
        if (!zone) {
            setGlobalError('Zona nu este definită!');
            return;
        }

        // Identificăm itemele care au un count valid și nu sunt deja sync sau sending
        const itemsToSend = items.filter(it =>
            it.counted !== undefined && it.status !== 'synced' && it.status !== 'sending'
        );

        if (itemsToSend.length === 0) return;

        // Setăm status 'sending' pentru UI
        setItems(prev => prev.map(it =>
            itemsToSend.find(send => send.code === it.code)
                ? { ...it, status: 'sending' }
                : it
        ));

        // Executăm request-uri în paralel, dar gestionăm erorile individual
        const promises = itemsToSend.map(async (item) => {
            try {
                await api.post('/audit', {
                    code: item.code,
                    counted: item.counted,
                    zone: zone
                });

                // Success: Update item status
                setItems(prev => {
                    const updated = prev.map(it => it.code === item.code ? { ...it, status: 'synced' as const } : it);
                    localStorage.setItem(STORAGE_KEY_ITEMS, JSON.stringify(updated));
                    return updated;
                });

            } catch (error: any) {
                // Error: Update item status to error
                setItems(prev => {
                    const updated = prev.map(it => it.code === item.code ? { ...it, status: 'error' as const } : it);
                    localStorage.setItem(STORAGE_KEY_ITEMS, JSON.stringify(updated));
                    return updated;
                });

                // Dacă e eroare de rețea (fără răspuns de la server), setăm notificare globală
                if (!error.response) {
                    setGlobalError(`Eroare de conexiune pentru produsul ${item.name}`);
                }
            }
        });

        await Promise.all(promises);
    };

    return {
        items,
        zone,
        isLoading,
        globalError,
        isZoneLocked,
        setGlobalError,
        saveZone,
        updateCount,
        submitAudits
    };
};