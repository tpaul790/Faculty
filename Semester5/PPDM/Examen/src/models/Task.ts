export interface Task {
    id: number;
    text: string;
    tag: string;
    version: number;
}

// Extindem Task pentru a gestiona starea locală a UI-ului
export interface LocalTask extends Task {
    isUnread: boolean; // Pentru contorul de notificări
    status: 'synced' | 'sending' | 'conflict' | 'offline'; // Starea operației
    localText?: string; // Textul modificat local care încă nu a fost salvat
    serverVersionTask?: Task; // Task-ul de pe server în caz de conflict
}