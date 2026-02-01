import React, { useState, useEffect } from 'react';
import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar, IonInput, IonButton, IonItem, IonLabel } from '@ionic/react';
import { useQuestions } from '../QuestionContext';
import { useHistory } from 'react-router';

const Login: React.FC = () => {
    const [id, setId] = useState('');
    const { login, isAuthenticated } = useQuestions();
    const history = useHistory();

    // FIX: Navigare reactivă
    useEffect(() => {
        if (isAuthenticated) {
            history.replace('/download');
        }
    }, [isAuthenticated, history]);

    const handleLogin = async () => {
        if (!id) return;
        await login(id);
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Examen Login</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">
                <IonItem>
                    <IonLabel position="floating">Student ID</IonLabel>
                    <IonInput value={id} onIonChange={e => setId(e.detail.value!)} />
                </IonItem>
                <IonButton expand="block" onClick={handleLogin} className="ion-margin-top">
                    Register / Login
                </IonButton>
            </IonContent>
        </IonPage>
    );
};

export default Login;