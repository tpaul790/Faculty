import React, { useState } from 'react';
import {
    IonContent, IonHeader, IonPage, IonTitle, IonToolbar,
    IonList, IonLoading, IonFooter, IonButton, IonToast,
    IonSegment, IonSegmentButton, IonLabel, IonButtons, IonIcon
} from '@ionic/react';
import { filter } from 'ionicons/icons';
import { useInventory } from '../hooks/useInventory';
import AuditItemCard from '../components/AuditItemCard';

const InventoryPage: React.FC = () => {
    const {
        items, zone, isLoading, globalError,
        setGlobalError, updateCount, submitAudits
    } = useInventory();

    const [filterMode, setFilterMode] = useState<'all' | 'discrepancy'>('all');

    // Cerința 6: Filtrare (Discrepanțe = Scriptic != Faptic)
    const displayedItems = items.filter(item => {
        if (filterMode === 'all') return true;
        // Discrepanță: doar dacă a fost numărat ȘI diferă de scriptic
        return item.counted !== undefined && item.counted !== item.quantity;
    });

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Audit: {zone}</IonTitle>
                    <IonButtons slot="end">
                        {/* Cerința 6: UI pentru Filtrare */}
                        <IonButton onClick={() => setFilterMode(filterMode === 'all' ? 'discrepancy' : 'all')}>
                            <IonIcon icon={filter} color={filterMode === 'discrepancy' ? 'primary' : 'medium'} />
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
                <IonToolbar>
                    <IonSegment value={filterMode} onIonChange={e => setFilterMode(e.detail.value as any)}>
                        <IonSegmentButton value="all">
                            <IonLabel>Toate</IonLabel>
                        </IonSegmentButton>
                        <IonSegmentButton value="discrepancy">
                            <IonLabel>Discrepanțe</IonLabel>
                        </IonSegmentButton>
                    </IonSegment>
                </IonToolbar>
            </IonHeader>

            <IonContent>
                {/* Cerința 2: Loading Indicator pentru WS */}
                <IonLoading isOpen={isLoading} message="Descarcare inventar..." />

                {/* Cerința 9: Notificări globale */}
                <IonToast
                    isOpen={!!globalError}
                    message={globalError || ''}
                    duration={3000}
                    color="danger"
                    onDidDismiss={() => setGlobalError(null)}
                />

                <IonList>
                    {displayedItems.map(item => (
                        <AuditItemCard
                            key={item.code}
                            item={item}
                            onUpdate={updateCount}
                        />
                    ))}
                    {displayedItems.length === 0 && !isLoading && (
                        <div className="ion-text-center ion-padding">Nu există produse de afișat.</div>
                    )}
                </IonList>
            </IonContent>

            <IonFooter>
                <IonToolbar className="ion-padding">
                    {/* Cerința 7 & 8: Buton Audit Complete - retrimite și pe cele eșuate */}
                    <IonButton expand="block" onClick={submitAudits}>
                        Audit Complete
                    </IonButton>
                </IonToolbar>
            </IonFooter>
        </IonPage>
    );
};

export default InventoryPage;