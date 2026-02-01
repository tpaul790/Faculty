import React, { useState } from 'react';
import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar, IonItem, IonLabel, IonInput, IonButton, IonCard, IonCardContent } from '@ionic/react';
import { useHistory } from 'react-router';

interface Props {
    onSetZone: (zone: string) => void;
    isLocked: boolean;
}

const ZonePage: React.FC<Props> = ({ onSetZone, isLocked }) => {
    const [inputZone, setInputZone] = useState('');
    const history = useHistory();

    // Dacă zona e deja setată, redirecționăm automat
    if (isLocked) {
        history.replace('/audit');
    }

    const handleSet = () => {
        if (inputZone.trim().length > 0) {
            onSetZone(inputZone);
            history.replace('/audit');
        }
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Configurare Zonă</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">
                <IonCard>
                    <IonCardContent>
                        <p>Introduceți zona depozitului pentru a începe auditul.</p>
                        <IonItem>
                            <IonLabel position="stacked">Zonă Depozit</IonLabel>
                            <IonInput
                                value={inputZone}
                                onIonChange={e => setInputZone(e.detail.value!)}
                                placeholder="Ex: A1, B2..."
                            />
                        </IonItem>
                        <IonButton expand="block" className="ion-margin-top" onClick={handleSet} disabled={!inputZone}>
                            Set Zone
                        </IonButton>
                    </IonCardContent>
                </IonCard>
            </IonContent>
        </IonPage>
    );
};

export default ZonePage;