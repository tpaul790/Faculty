import axios from './axios';
import { Task } from '../models/Task';

export const taskApi = {
    getAll: async () => {
        return axios.get<Task[]>('/task');
    },

    getById: async (id: number) => {
        return axios.get<Task>(`/task/${id}`);
    },

    update: async (task: Task) => {
        return axios.put<Task>(`/task/${task.id}`, task);
    }
};