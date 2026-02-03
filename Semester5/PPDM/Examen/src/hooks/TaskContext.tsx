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
    // Initializare din LocalStorage
    const [tasks, setTasks] = useState<LocalTask[]>(() => {
        try {
            const savedTasks = localStorage.getItem('tasks');
            return savedTasks ? JSON.parse(savedTasks) : [];
        } catch (e) {
            return [];
        }
    });

    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [toastMessage, setToastMessage] = useState<string | null>(null);

    // Salvare automată în LocalStorage
    useEffect(() => {
        localStorage.setItem('tasks', JSON.stringify(tasks));
    }, [tasks]);

    // 1. FETCH TASKS
    const fetchTasks = useCallback(async () => {
        if (tasks.length === 0) setLoading(true);
        setError(null);
        try {
            const response = await taskApi.getAll();
            const serverTasks = response.data;

            setTasks(currentTasks => {
                const mergedTasks = serverTasks.map(serverTask => {
                    const localCounterpart = currentTasks.find(t => t.id === serverTask.id);

                    // Păstrăm task-ul local dacă are activitate în curs
                    if (localCounterpart && (
                        localCounterpart.status === 'sending' ||
                        localCounterpart.status === 'offline' ||
                        localCounterpart.status === 'conflict'
                    )) {
                        return localCounterpart;
                    }

                    const isUnread = localCounterpart ? localCounterpart.isUnread : true;

                    return { ...serverTask, isUnread, status: 'synced' } as LocalTask;
                });
                return mergedTasks;
            });

        } catch (err: any) {
            setError('Failed to fetch tasks. Using local data.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchTasks();

        const ws = new WebSocket('ws://localhost:3000');
        ws.onopen = () => console.log('WS Connected');

        ws.onmessage = (event) => {
            const updatedTask: Task = JSON.parse(event.data);
            console.log('WS Update:', updatedTask);

            setTasks(prevTasks => {
                const index = prevTasks.findIndex(t => t.id === updatedTask.id);
                if (index > -1) {
                    const currentTask = prevTasks[index];
                    const newTasks = [...prevTasks];

                    if (currentTask.status === 'synced' || !currentTask.status) {
                        newTasks[index] = { ...updatedTask, isUnread: true, status: 'synced' };
                        setToastMessage(`Task ${updatedTask.id} updated from server!`);
                    } else {
                        newTasks[index].isUnread = true;
                        console.log(`Ignored WS update for task ${currentTask.id} because status is ${currentTask.status}`);
                    }
                    return newTasks;
                } else {
                    return [...prevTasks, { ...updatedTask, isUnread: true, status: 'synced' }];
                }
            });
        };

        return () => ws.close();
    }, [fetchTasks]);

    const saveTaskInBackground = async (taskToSave: Task) => {
        // Pas 1: Update Instantaneu în UI (Optimistic Update)
        setTasks(prev => prev.map(t =>
            t.id === taskToSave.id
                ? {
                    ...t,
                    text: taskToSave.text,
                    status: 'sending',
                    localText: taskToSave.text,
                    // MODIFICARE AICI: Ștergem datele despre conflictul anterior
                    // pentru ca mesajul de eroare să dispară imediat ce dăm Save.
                    serverVersionTask: undefined
                }
                : t
        ));

        try {
            const response = await taskApi.update(taskToSave);

            // Pas 2: Confirmare de la server (Success)
            setTasks(prev => prev.map(t =>
                t.id === taskToSave.id
                    ? {
                        ...response.data,
                        status: 'synced',
                        isUnread: false,
                        localText: undefined,
                        serverVersionTask: undefined
                    }
                    : t
            ));
        } catch (err: any) {
            console.error('Save failed:', err);

            if (err.response && err.response.status === 409) {
                setTasks(prev => prev.map(t =>
                    t.id === taskToSave.id
                        ? { ...t, status: 'conflict' }
                        : t
                ));
            } else {
                setTasks(prev => prev.map(t =>
                    t.id === taskToSave.id
                        ? { ...t, status: 'offline' }
                        : t
                ));
            }
        }
    };

    const markAsRead = (tag: string) => {
        setTasks(prev => prev.map(t =>
            t.tag === tag ? { ...t, isUnread: false } : t
        ));
    };

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