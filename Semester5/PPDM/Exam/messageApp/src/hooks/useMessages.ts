import { useState, useEffect, useCallback } from 'react';
import { Preferences } from '@capacitor/preferences';

export interface Message {
    id: number;
    text: string;
    read: boolean;
    sender: string;
    created: number;
}

const API_URL = 'http://localhost:3000';
const WS_URL = 'ws://localhost:3000';

export const useMessages = () => {
    const [messages, setMessages] = useState<Message[]>([]);

    // Salvare locală (Cerinta 8)
    const saveToStorage = async (msgs: Message[]) => {
        await Preferences.set({
            key: 'messages',
            value: JSON.stringify(msgs),
        });
    };

    // Încărcare inițială
    useEffect(() => {
        const init = async () => {
            // 1. Load from Storage
            const { value } = await Preferences.get({ key: 'messages' });
            if (value) {
                setMessages(JSON.parse(value));
            }

            // 2. Fetch from API (Cerinta 1)
            try {
                const res = await fetch(`${API_URL}/message`);
                const data = await res.json();
                // Combinăm cu local (simplificat: suprascriem, dar păstrăm în memorie)
                setMessages(prev => {
                    const newMsgs = data;
                    saveToStorage(newMsgs);
                    return newMsgs;
                });
            } catch (err) {
                console.error("Fetch error (offline?):", err);
            }
        };

        init();

        // 3. WebSocket (Cerinta 7)
        const ws = new WebSocket(WS_URL);
        ws.onmessage = (event) => {
            const newMsg: Message = JSON.parse(event.data);
            console.log('WS received:', newMsg);
            setMessages(prev => {
                // Evităm duplicatele
                if (prev.find(m => m.id === newMsg.id)) return prev;
                const updated = [...prev, newMsg];
                saveToStorage(updated);
                return updated;
            });
        };

        return () => {
            ws.close();
        };
    }, []);

    // Funcția de Update cu Retry (Cerinta 6 & 9)
    const markAsRead = useCallback(async (message: Message) => {
        // Optimistic UI Update: actualizăm local imediat
        setMessages(prev => {
            const updated = prev.map(m => m.id === message.id ? { ...m, read: true } : m);
            saveToStorage(updated);
            return updated;
        });

        const updateWithRetry = async (attempt = 1) => {
            try {
                const res = await fetch(`${API_URL}/message/${message.id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ ...message, read: true })
                });
                if (!res.ok) throw new Error('Server error');
                console.log(`Synced message ${message.id}`);
            } catch (err) {
                console.warn(`Sync failed for ${message.id}, attempt ${attempt}`);
                if (attempt < 5) {
                    setTimeout(() => updateWithRetry(attempt + 1), 2000); // Retry logic
                }
            }
        };

        updateWithRetry();
    }, []);

    return { messages, markAsRead };
};