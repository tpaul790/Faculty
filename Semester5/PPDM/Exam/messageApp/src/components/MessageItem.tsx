import React, { useEffect, useState } from 'react';
import { IonItem, IonLabel, IonIcon, IonNote } from '@ionic/react';
import { checkmarkDone } from 'ionicons/icons';
import { Message } from '../hooks/useMessages';

interface Props {
    message: Message;
    onMarkRead: (msg: Message) => void;
}

const MessageItem: React.FC<Props> = ({ message, onMarkRead }) => {
    // Stare locală pentru efectul vizual de bold
    const [isHighlight, setIsHighlight] = useState(false);

    useEffect(() => {
        // Cerinta 5: Dacă mesajul e necitit, îl facem bold, așteptăm 1s, apoi îl marcăm citit
        if (!message.read) {
            setIsHighlight(true);

            const timer = setTimeout(() => {
                setIsHighlight(false); // Scoatem bold
                onMarkRead(message);   // Trimitem la server (Cerinta 6)
            }, 1000);

            return () => clearTimeout(timer); // Cleanup dacă userul iese rapid
        }
    }, [message.id, message.read, onMarkRead]); // Dependențe

    return (
        <IonItem lines="none" style={{ '--background': 'rgba(104,102,102,0.5)' }}>
            <IonLabel className="ion-text-wrap">
                {/* Aplicăm stil bold condiționat */}
                <h3 style={{
                    fontWeight: isHighlight ? '900' : 'normal',
                    color: isHighlight ? 'var(--ion-color-primary)' : 'inherit',
                    transition: 'all 0.5s ease'
                }}>
                    {message.text}
                </h3>
                <IonNote style={{ fontSize: '0.8em' }}>
                    {new Date(message.created).toLocaleString()}
                </IonNote>
            </IonLabel>
            {message.read && <IonIcon icon={checkmarkDone} color="success" slot="end" />}
        </IonItem>
    );
};

export default MessageItem;