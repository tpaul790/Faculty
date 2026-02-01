import React, { useState, useRef } from 'react';
import { IonItem, IonLabel, IonInput, IonSpinner, IonNote } from '@ionic/react';
import { AuditItem } from '../models/types';

interface Props {
    item: AuditItem;
    onUpdate: (code: number, val: number) => void;
}

const AuditItemCard: React.FC<Props> = ({ item, onUpdate }) => {
    const [isEditing, setIsEditing] = useState(false);
    const inputRef = useRef<HTMLIonInputElement>(null);

    const handleFinishEdit = () => {
        setIsEditing(false);
        if (inputRef.current && inputRef.current.value !== undefined && inputRef.current.value !== '') {
            const val = parseInt(inputRef.current.value as string, 10);
            if (!isNaN(val)) {
                onUpdate(item.code, val);
            }
        }
    };

    const getStatusColor = () => {
        if (item.status === 'error') return 'danger'; // Cerința 8: Font roșu (folosim culoare Ionic danger)
        if (item.status === 'synced') return 'success';
        return 'medium';
    };

    return (
        <IonItem button={!isEditing} onClick={() => !isEditing && setIsEditing(true)}>
            <IonLabel className="ion-text-wrap">
                <h2>{item.name}</h2>
                <p>Cod: {item.code} | Scriptic: {item.quantity}</p>
            </IonLabel>

            {/* Cerința 9: Indicator progres per element */}
            {item.status === 'sending' && <IonSpinner slot="end" name="dots" />}

            {/* Cerința 5: Editare vs Readonly Text */}
            {isEditing ? (
                <IonInput
                    ref={inputRef}
                    type="number"
                    placeholder="0"
                    value={item.counted}
                    slot="end"
                    className="ion-text-end"
                    style={{ maxWidth: '80px', borderBottom: '1px solid var(--ion-color-primary)' }}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') handleFinishEdit();
                    }}
                    onIonBlur={handleFinishEdit}
                />
            ) : (
                <div slot="end" className="ion-text-end">
                    {item.counted !== undefined ? (
                        // Cerința 8: Afișare cu roșu dacă a fost eroare
                        <IonLabel color={getStatusColor()} style={{ fontWeight: 'bold', fontSize: '1.2em' }}>
                            {item.counted}
                        </IonLabel>
                    ) : (
                        <IonNote>Netestat</IonNote>
                    )}
                </div>
            )}
        </IonItem>
    );
};

export default AuditItemCard;