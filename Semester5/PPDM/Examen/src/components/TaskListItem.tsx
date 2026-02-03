import React from 'react';
import { IonItem, IonLabel, IonBadge } from '@ionic/react';
import { LocalTask } from '../models/Task';

interface Props {
    task: LocalTask;
    onClick: (task: LocalTask) => void;
}

const TaskListItem: React.FC<Props> = ({ task, onClick }) => {

    const getStatusColor = () => {
        switch (task.status) {
            case 'conflict': return 'danger';
            case 'offline': return 'warning';
            case 'sending': return 'secondary';
            default: return 'success';
        }
    };

    // Alegem textul etichetei
    const getStatusText = () => {
        switch (task.status) {
            case 'conflict': return 'Version conflict';
            case 'offline': return 'Not sent';
            case 'sending': return 'Sending...'; // Aici este textul cerut
            default: return '';
        }
    };

    return (
        <IonItem button onClick={() => onClick(task)}>
            <IonLabel>
                <h2>ID: {task.id}</h2>
                <p style={{ whiteSpace: 'pre-wrap' }}>
                    {task.localText !== undefined ? task.localText : task.text}
                </p>
            </IonLabel>

            <div slot="end" style={{display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '4px'}}>
                {task.isUnread && <IonBadge color="primary">New</IonBadge>}

                {task.status && task.status !== 'synced' && (
                    <IonBadge color={getStatusColor()}>
                        {getStatusText()}
                    </IonBadge>
                )}
            </div>
        </IonItem>
    );
};

export default TaskListItem;