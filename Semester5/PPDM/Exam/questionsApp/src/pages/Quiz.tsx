import React, { useState, useEffect } from 'react';
import {
    IonContent, IonHeader, IonPage, IonTitle, IonToolbar,
    IonCard, IonCardContent, IonCardHeader, IonCardSubtitle, IonCardTitle,
    IonItem, IonLabel, IonList, IonButton, IonFooter
} from '@ionic/react';
import { useQuestions } from '../QuestionContext';

const Quiz: React.FC = () => {
    const { questions, answers, submitAnswer, stats } = useQuestions();

    const getInitialIndex = () => {
        const idx = questions.findIndex(q => answers[q.id] === undefined);
        return idx === -1 ? questions.length : idx;
    };

    const [currentIndex, setCurrentIndex] = useState(0);
    const [selectedOption, setSelectedOption] = useState<number | null>(null);

    useEffect(() => {
        if (currentIndex >= questions.length) {
            setCurrentIndex(getInitialIndex());
        }
    }, [questions.length]);

    const currentQuestion = questions[currentIndex];
    const isFinished = !currentQuestion;

    useEffect(() => {
        if (isFinished) return;

        setSelectedOption(null); // Reset la schimbarea întrebării

        const timer = setTimeout(() => {
            // Timpul a expirat, trecem la următoarea
            handleNext(true);
        }, 5000);

        return () => clearTimeout(timer);
    }, [currentIndex, isFinished]);

    const handleNext = (autoSkipped = false) => {
        if (!autoSkipped && selectedOption !== null && currentQuestion) {
            submitAnswer(currentQuestion.id, selectedOption);
        }
        // Indiferent dacă a răspuns sau a expirat timpul, trecem mai departe
        setCurrentIndex(prev => prev + 1);
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Quiz</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">
                {/* Req 5: Statistici */}
                <div className="ion-margin-bottom ion-text-center">
                    <IonLabel>
                        <h2>Questions: {stats.answered} / {stats.total}</h2>
                        <p>Correct answers: {stats.correct} / {stats.answered}</p>
                    </IonLabel>
                </div>

                {!isFinished ? (
                    <IonCard>
                        <IonCardHeader>
                            <IonCardSubtitle>Question #{currentQuestion.id}</IonCardSubtitle>
                            <IonCardTitle>{currentQuestion.text}</IonCardTitle>
                        </IonCardHeader>
                        <IonCardContent>
                            <IonList>
                                {currentQuestion.options.map((opt, idx) => (
                                    <IonItem
                                        button
                                        key={idx}
                                        onClick={() => setSelectedOption(idx)}
                                        // Req 6: Highlight selection
                                        color={selectedOption === idx ? 'primary' : ''}
                                        lines="full"
                                    >
                                        <IonLabel>{opt}</IonLabel>
                                    </IonItem>
                                ))}
                            </IonList>
                        </IonCardContent>
                    </IonCard>
                ) : (
                    <div className="ion-text-center ion-padding">
                        <h2>No more questions pending!</h2>
                        <p>Waiting for WebSocket updates...</p>
                    </div>
                )}
            </IonContent>

            {/* Req 6: Buton Next */}
            {!isFinished && (
                <IonFooter>
                    <IonToolbar>
                        <IonButton expand="full" onClick={() => handleNext(false)} disabled={selectedOption === null}>
                            Next
                        </IonButton>
                    </IonToolbar>
                </IonFooter>
            )}
        </IonPage>
    );
};

export default Quiz;