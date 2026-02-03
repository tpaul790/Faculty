import React from 'react';
import { IonItem, IonLabel, IonBadge, IonNote, IonSpinner } from '@ionic/react';
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
            case 'sending': return 'medium';
            default: return 'success';
        }
    };

    const getStatusText = () => {
        switch (task.status) {
            case 'conflict': return 'Version conflict';
            case 'offline': return 'Not sent';
            case 'sending': return 'Sending...';
            default: return '';
        }
    };

    return (
        <IonItem button onClick={() => onClick(task)}>
            <IonLabel>
                <h2>ID: {task.id}</h2>
                <p>{task.text}</p>
            </IonLabel>

            <div slot="end" style={{display: 'flex', flexDirection: 'column', alignItems: 'flex-end'}}>
                {task.isUnread && <IonBadge color="primary">New</IonBadge>}

                {task.status === 'sending' && <IonSpinner name="dots" />}

                {task.status !== 'synced' && task.status !== 'sending' && (
                    <IonBadge color={getStatusColor()}>{getStatusText()}</IonBadge>
                )}
            </div>
        </IonItem>
    );
};

export default TaskListItem;