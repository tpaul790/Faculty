import React, { useState, useMemo } from 'react';
import {
    IonContent, IonHeader, IonPage, IonTitle, IonToolbar,
    IonList, IonItem, IonLabel, IonBadge, IonButton, IonIcon,
    IonLoading, IonRefresher, IonRefresherContent
} from '@ionic/react';
import { arrowBack } from 'ionicons/icons';
import { useTasks } from '../hooks/TaskContext';
import TaskListItem from '../components/TaskListItem';
import { useHistory } from 'react-router';
import { LocalTask } from '../models/Task';

const Home: React.FC = () => {
    const { tasks, loading, error, fetchTasks, markAsRead, saveTaskInBackground, resolveConflict } = useTasks();
    const [selectedTag, setSelectedTag] = useState<string | null>(null);
    const history = useHistory();

    // 1. Group tasks by tag & calculate unread
    const tagStats = useMemo(() => {
        const stats: Record<string, number> = {};
        tasks.forEach(t => {
            if (!stats[t.tag]) stats[t.tag] = 0;
            if (t.isUnread) stats[t.tag]++;
        });
        return stats;
    }, [tasks]);

    const uniqueTags = Object.keys(tagStats).sort();

    const handleTagClick = (tag: string) => {
        markAsRead(tag); // 2. Mark local as read
        setSelectedTag(tag);
    };

    const handleTaskClick = async (task: LocalTask) => {
        if (task.status === 'offline') {
            // 6. Retry sending
            if (task.localText) {
                saveTaskInBackground({ ...task, text: task.localText });
            }
        } else if (task.status === 'conflict') {
            // 7. Resolve conflict -> fetch server version then navigate
            await resolveConflict(task.id);
            history.push(`/edit/${task.id}`);
        } else {
            // 3. Normal edit
            history.push(`/edit/${task.id}`);
        }
    };

    const doRefresh = async (event: CustomEvent) => {
        await fetchTasks();
        event.detail.complete();
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    {selectedTag && (
                        <IonButton slot="start" fill="clear" onClick={() => setSelectedTag(null)}>
                            <IonIcon icon={arrowBack} /> Return
                        </IonButton>
                    )}
                    <IonTitle>{selectedTag ? `Tasks: ${selectedTag}` : 'My Tasks (PPDM)'}</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent fullscreen>
                <IonRefresher slot="fixed" onIonRefresh={doRefresh}>
                    <IonRefresherContent />
                </IonRefresher>

                <IonLoading isOpen={loading} message={'Loading...'} />

                {/* Error Retry Logic */}
                {error && !loading && (
                    <div className="ion-padding ion-text-center">
                        <p style={{color: 'red'}}>{error}</p>
                        <IonButton onClick={() => fetchTasks()}>Retry</IonButton>
                    </div>
                )}

                <IonList>
                    {/* View 1: Tag List */}
                    {!selectedTag && uniqueTags.map(tag => (
                        <IonItem key={tag} button onClick={() => handleTagClick(tag)}>
                            <IonLabel>{tag}</IonLabel>
                            {tagStats[tag] > 0 && <IonBadge slot="end">{tagStats[tag]}</IonBadge>}
                        </IonItem>
                    ))}

                    {/* View 2: Task List for selected Tag */}
                    {selectedTag && tasks
                        .filter(t => t.tag === selectedTag)
                        .map(task => (
                            <TaskListItem key={task.id} task={task} onClick={handleTaskClick} />
                        ))}
                </IonList>
            </IonContent>
        </IonPage>
    );
};

export default Home;