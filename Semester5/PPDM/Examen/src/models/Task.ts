export interface Task {
    id: number;
    text: string;
    tag: string;
    version: number;
}

export interface LocalTask extends Task {
    isUnread: boolean;
    status: 'synced' | 'sending' | 'conflict' | 'offline';
    localText?: string;
    serverVersionTask?: Task;
}