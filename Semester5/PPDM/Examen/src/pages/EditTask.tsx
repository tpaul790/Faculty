import React, { useState, useEffect } from 'react';
import {
    IonContent, IonHeader, IonPage, IonTitle, IonToolbar,
    IonButtons, IonBackButton, IonInput, IonItem, IonLabel,
    IonButton, IonCard, IonCardContent, IonCardHeader, IonCardSubtitle
} from '@ionic/react';
import { useHistory, useParams } from 'react-router';
import { useTasks } from '../hooks/TaskContext';

const EditTask: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const history = useHistory();
    const { tasks, saveTaskInBackground } = useTasks();

    const [text, setText] = useState('');
    const [task, setTask] = useState<any>(null);

    useEffect(() => {
        const foundTask = tasks.find(t => t.id === parseInt(id));
        if (foundTask) {
            setTask(foundTask);
            // Dacă avem text local (ne-salvat/conflict), îl afișăm pe acela
            setText(foundTask.localText || foundTask.text);
        }
    }, [id, tasks]);

    const handleSave = () => {
        if (task) {
            // 3. Trigger background save and go back immediately
            const taskToSave = { ...task, text: text };
            saveTaskInBackground(taskToSave);
            history.goBack();
        }
    };

    if (!task) return <IonContent>Task not found</IonContent>;

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonBackButton />
                    </IonButtons>
                    <IonTitle>Edit Task {id}</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">

                {/* 7. Conflict Visualization */}
                {task.status === 'conflict' && task.serverVersionTask && (
                    <IonCard color="warning">
                        <IonCardHeader>
                            <IonCardSubtitle>Server Version (Newer)</IonCardSubtitle>
                        </IonCardHeader>
                        <IonCardContent>
                            <p><strong>Text:</strong> {task.serverVersionTask.text}</p>
                            <p><strong>Version:</strong> {task.serverVersionTask.version}</p>
                            <p><em>The server has a newer version. Saving now will overwrite it with your text below.</em></p>
                        </IonCardContent>
                    </IonCard>
                )}

                <IonItem>
                    <IonLabel position="stacked">Description</IonLabel>
                    <IonInput
                        value={text}
                        onIonChange={e => setText(e.detail.value!)}
                    />
                </IonItem>

                <div className="ion-padding-top">
                    <IonButton expand="block" onClick={handleSave}>
                        Save
                    </IonButton>
                </div>
            </IonContent>
        </IonPage>
    );
};

export default EditTask;