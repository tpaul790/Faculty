import axios from 'axios';

// ATENȚIE: Schimbă URL-ul dacă rulezi pe Android Emulator (10.0.2.2) sau Device Real
const BASE_URL = 'http://localhost:3000';
const WS_URL = 'ws://localhost:3000';

export interface Question {
    id: number;
    text: string;
    options: number[];
    indexCorrectOption: number;
}

export interface AuthResponse {
    token: string;
    questionIds: number[];
}

export const api = {
    auth: async (id: string): Promise<AuthResponse> => {
        const res = await axios.post(`${BASE_URL}/auth`, { id });
        return res.data;
    },
    getQuestion: async (id: number): Promise<Question> => {
        const res = await axios.get(`${BASE_URL}/question/${id}`);
        return res.data;
    },
    getWsUrl: () => WS_URL
};