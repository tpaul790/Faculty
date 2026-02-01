import React, { createContext, useState, useEffect, useCallback, useContext, useRef } from 'react';
import { api, Question } from './api';

interface QuestionContextType {
    isAuthenticated: boolean;
    token: string | null;
    questions: Question[];
    answers: Record<number, number>;
    downloadProgress: { current: number; total: number } | null;
    downloadError: boolean;
    isDownloading: boolean;
    login: (id: string) => Promise<void>;
    startDownload: () => Promise<void>;
    submitAnswer: (questionId: number, optionIndex: number) => void;
    stats: { answered: number; total: number; correct: number };
}

const QuestionContext = createContext<QuestionContextType | null>(null);

export const QuestionProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
    const [questionIdsToFetch, setQuestionIdsToFetch] = useState<number[]>([]);
    const [questions, setQuestions] = useState<Question[]>(() => {
        const saved = localStorage.getItem('questions');
        return saved ? JSON.parse(saved) : [];
    });
    const [answers, setAnswers] = useState<Record<number, number>>(() => {
        const saved = localStorage.getItem('answers');
        return saved ? JSON.parse(saved) : {};
    });

    const [downloadProgress, setDownloadProgress] = useState<{ current: number; total: number } | null>(null);
    const [downloadError, setDownloadError] = useState(false);
    const [isDownloading, setIsDownloading] = useState(false);

    const isDownloadingRef = useRef(false);

    useEffect(() => { localStorage.setItem('questions', JSON.stringify(questions)); }, [questions]);
    useEffect(() => { localStorage.setItem('answers', JSON.stringify(answers)); }, [answers]);
    useEffect(() => { if (token) localStorage.setItem('token', token); }, [token]);

    useEffect(() => {
        if (!token) return;
        const ws = new WebSocket(api.getWsUrl());

        ws.onopen = () => console.log('WS Connected');
        ws.onmessage = (event) => {
            try {
                const newQuestion: Question = JSON.parse(event.data);
                console.log('New Question via WS:', newQuestion);
                setQuestions(prev => {
                    if (prev.find(q => q.id === newQuestion.id)) return prev;
                    return [...prev, newQuestion];
                });
            } catch (e) {
                console.error('WS Parse error', e);
            }
        };
        return () => ws.close();
    }, [token]);

    const login = async (id: string) => {
        try {
            const data = await api.auth(id);
            setToken(data.token);
            setQuestionIdsToFetch(data.questionIds);
        } catch (error) {
            console.error("Login failed", error);
            alert("Login failed!");
            throw error;
        }
    };

    const startDownload = useCallback(async () => {
        if (isDownloadingRef.current || questionIdsToFetch.length === 0) return;

        isDownloadingRef.current = true;
        setIsDownloading(true);
        setDownloadError(false);
        setDownloadProgress({ current: 0, total: questionIdsToFetch.length });

        const newDownloadedQuestions: Question[] = [];

        try {
            for (let i = 0; i < questionIdsToFetch.length; i++) {
                const qId = questionIdsToFetch[i];

                const exists = questions.find(q => q.id === qId);

                if (exists) {
                    newDownloadedQuestions.push(exists);
                } else {
                    const q = await api.getQuestion(qId);
                    newDownloadedQuestions.push(q);
                }

                setDownloadProgress({ current: i + 1, total: questionIdsToFetch.length });
            }

            setQuestions(prev => {
                const existingIds = new Set(prev.map(p => p.id));
                const final = [...prev];
                newDownloadedQuestions.forEach(q => {
                    if(!existingIds.has(q.id)) final.push(q);
                });
                return final;
            });

            setQuestionIdsToFetch([]);
        } catch (error) {
            console.error("Download failed", error);
            setDownloadError(true);
        } finally {
            setIsDownloading(false);
            isDownloadingRef.current = false;
        }
    }, [questionIdsToFetch, questions]);

    const submitAnswer = (qId: number, optionIndex: number) => {
        setAnswers(prev => ({ ...prev, [qId]: optionIndex }));
    };

    const stats = {
        answered: Object.keys(answers).length,
        total: questions.length,
        correct: Object.keys(answers).reduce((acc, qIdStr) => {
            const qId = parseInt(qIdStr);
            const question = questions.find(q => q.id === qId);
            if (question && question.indexCorrectOption === answers[qId]) {
                return acc + 1;
            }
            return acc;
        }, 0)
    };

    return (
        <QuestionContext.Provider value={{
            isAuthenticated: !!token,
            token,
            questions,
            answers,
            downloadProgress,
            downloadError,
            isDownloading,
            login,
            startDownload,
            submitAnswer,
            stats
        }}>
            {children}
        </QuestionContext.Provider>
    );
};

export const useQuestions = () => {
    const context = useContext(QuestionContext);
    if (!context) throw new Error("useQuestions must be used within QuestionProvider");
    return context;
};