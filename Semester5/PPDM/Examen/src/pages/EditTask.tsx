import React, { useState, useEffect } from 'react';
import {
    IonContent, IonHeader, IonPage, IonTitle, IonToolbar,
    IonButtons, IonBackButton, IonInput, IonItem, IonLabel,
    IonButton, IonCard, IonCardContent, IonCardHeader, IonCardSubtitle,
    IonLoading
} from '@ionic/react';
import { useHistory, useParams } from 'react-router';
import { useTasks } from '../hooks/TaskContext';

const EditTask: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const history = useHistory();

    const { tasks, saveTaskInBackground, loading } = useTasks();

    const task = tasks.find(t => t.id === parseInt(id, 10));

    const [text, setText] = useState<string>('');
    const [isInitialized, setIsInitialized] = useState(false);

    useEffect(() => {
        if (task && !isInitialized) {
            const initialText = task.localText !== undefined ? task.localText : task.text;
            setText(initialText);
            setIsInitialized(true);
        }
    }, [task, isInitialized]);

    const handleSave = () => {
        if (task) {
            const versionToUse = task.serverVersionTask
                ? task.serverVersionTask.version
                : task.version;

            const taskToSave = {
                ...task,
                text: text,
                version: versionToUse
            };

            saveTaskInBackground(taskToSave);
            history.goBack();
        }
    };

    if (loading && !task) {
        return (
            <IonPage>
                <IonContent>
                    <IonLoading isOpen={true} message="Loading task..." />
                </IonContent>
            </IonPage>
        );
    }

    if (!task) {
        return (
            <IonPage>
                <IonHeader>
                    <IonToolbar>
                        <IonButtons slot="start"><IonBackButton /></IonButtons>
                        <IonTitle>Task Not Found</IonTitle>
                    </IonToolbar>
                </IonHeader>
                <IonContent className="ion-padding">
                    Task with ID {id} not found.
                </IonContent>
            </IonPage>
        );
    }

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonBackButton defaultHref="/home" />
                    </IonButtons>
                    <IonTitle>Edit Task {id}</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">

                {task.status === 'conflict' && task.serverVersionTask && (
                    <IonCard color="warning">
                        <IonCardHeader>
                            <IonCardSubtitle>Server Version (Newer)</IonCardSubtitle>
                        </IonCardHeader>
                        <IonCardContent>
                            <p><strong>Server Text:</strong> {task.serverVersionTask.text}</p>
                            <p><strong>Version:</strong> {task.serverVersionTask.version}</p>
                            <p><em>Saving will overwrite the server version.</em></p>
                        </IonCardContent>
                    </IonCard>
                )}

                <IonItem>
                    <IonLabel position="stacked">Description</IonLabel>
                    <IonInput
                        value={text}
                        placeholder="Edit task description"
                        onIonInput={e => setText(e.detail.value!)}
                    />
                </IonItem>

                <div className="ion-padding-top">
                    <IonButton expand="block" onClick={handleSave}>
                        Save Changes
                    </IonButton>
                </div>
            </IonContent>
        </IonPage>
    );
};

export default EditTask;