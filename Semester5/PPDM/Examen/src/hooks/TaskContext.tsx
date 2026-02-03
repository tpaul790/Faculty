import React, { createContext, useState, useEffect, useCallback, useContext } from 'react';
import { Task, LocalTask } from '../models/Task';
import { taskApi } from '../api/taskApi';
import { IonToast } from '@ionic/react';

interface TaskContextType {
    tasks: LocalTask[];
    loading: boolean;
    error: string | null;
    fetchTasks: () => Promise<void>;
    saveTaskInBackground: (task: Task) => void;
    markAsRead: (tag: string) => void;
    resolveConflict: (id: number) => Promise<void>;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export const TaskProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [tasks, setTasks] = useState<LocalTask[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [toastMessage, setToastMessage] = useState<string | null>(null);

    // 1. Fetch initial via GET /task
    const fetchTasks = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await taskApi.getAll();
            const initialTasks: LocalTask[] = response.data.map(t => ({
                ...t,
                isUnread: false, // Inițial nu știm ce e nou, dar vom marca via WS
                status: 'synced'
            }));
            setTasks(initialTasks);
        } catch (err: any) {
            setError('Failed to fetch tasks. Please retry.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    // Initial load & WebSocket setup
    useEffect(() => {
        fetchTasks();

        // 8. WebSocket Integration
        const ws = new WebSocket('ws://localhost:3000');
        ws.onopen = () => console.log('WS Connected');
        ws.onmessage = (event) => {
            const updatedTask: Task = JSON.parse(event.data);
            console.log('WS Update:', updatedTask);

            setTasks(prevTasks => {
                const index = prevTasks.findIndex(t => t.id === updatedTask.id);
                if (index > -1) {
                    // Actualizăm task-ul existent și îl marcăm ca necitit
                    const newTasks = [...prevTasks];
                    // Păstrăm starea locală dacă este în conflict, altfel suprascriem
                    if (newTasks[index].status === 'synced') {
                        newTasks[index] = { ...updatedTask, isUnread: true, status: 'synced' };
                    } else {
                        // Dacă userul editează ceva ce s-a schimbat pe server, devine conflict doar la Save
                        // Dar putem notifica vizual
                        newTasks[index].isUnread = true;
                    }
                    return newTasks;
                } else {
                    return [...prevTasks, { ...updatedTask, isUnread: true, status: 'synced' }];
                }
            });
            setToastMessage(`Task ${updatedTask.id} updated!`);
        };

        return () => ws.close();
    }, [fetchTasks]);

    // 3. & 4. Save Task in Background
    const saveTaskInBackground = async (taskToSave: Task) => {
        // Update local UI immediately to "Sending..."
        setTasks(prev => prev.map(t =>
            t.id === taskToSave.id
                ? { ...t, text: taskToSave.text, status: 'sending', localText: taskToSave.text }
                : t
        ));

        try {
            const response = await taskApi.update(taskToSave);
            // Success
            setTasks(prev => prev.map(t =>
                t.id === taskToSave.id
                    ? { ...response.data, status: 'synced', isUnread: false, localText: undefined, serverVersionTask: undefined }
                    : t
            ));
        } catch (err: any) {
            console.error(err);
            if (err.response && err.response.status === 409) {
                // 5. Version Conflict
                setTasks(prev => prev.map(t =>
                    t.id === taskToSave.id
                        ? { ...t, status: 'conflict' }
                        : t
                ));
            } else {
                // 5. Offline / Network Error
                setTasks(prev => prev.map(t =>
                    t.id === taskToSave.id
                        ? { ...t, status: 'offline' }
                        : t
                ));
            }
        }
    };

    // 2. Mark tasks as read when entering tag view
    const markAsRead = (tag: string) => {
        setTasks(prev => prev.map(t =>
            t.tag === tag ? { ...t, isUnread: false } : t
        ));
    };

    // 7. Fetch server version for conflict resolution
    const resolveConflict = async (id: number) => {
        setLoading(true);
        try {
            const response = await taskApi.getById(id);
            const serverTask = response.data;
            setTasks(prev => prev.map(t =>
                t.id === id
                    ? { ...t, serverVersionTask: serverTask }
                    : t
            ));
        } catch (e) {
            setToastMessage("Could not fetch server version.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <TaskContext.Provider value={{ tasks, loading, error, fetchTasks, saveTaskInBackground, markAsRead, resolveConflict }}>
    {children}
    <IonToast isOpen={!!toastMessage} message={toastMessage || ''} duration={2000} onDidDismiss={() => setToastMessage(null)} />
    </TaskContext.Provider>
);
};

export const useTasks = () => {
    const context = useContext(TaskContext);
    if (!context) throw new Error('useTasks must be used within a TaskProvider');
    return context;
};