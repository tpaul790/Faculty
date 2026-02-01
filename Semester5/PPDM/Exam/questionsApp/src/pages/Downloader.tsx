import React, { useEffect } from 'react';
import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar, IonProgressBar, IonText, IonButton } from '@ionic/react';
import { useQuestions } from '../QuestionContext';
import { useHistory } from 'react-router';

const Downloader: React.FC = () => {
    const { startDownload, downloadProgress, downloadError, isDownloading, questions } = useQuestions();
    const history = useHistory();

    useEffect(() => {
        startDownload();
    }, [startDownload]);

    useEffect(() => {
        if (isDownloading || downloadError) return;

        const isDownloadSessionFinished =
            downloadProgress !== null &&
            downloadProgress.current === downloadProgress.total;

        const hasLocalQuestions = questions.length > 0 && downloadProgress === null;

        if (isDownloadSessionFinished || hasLocalQuestions) {
            history.replace('/quiz');
        }
    }, [isDownloading, downloadError, downloadProgress, questions, history]);

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Downloading...</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding ion-text-center">
                {isDownloading && downloadProgress ? (
                    <>
                        <h2 style={{ marginTop: '50px' }}>
                            Downloading {downloadProgress.current} / {downloadProgress.total}
                        </h2>
                        <IonProgressBar
                            value={downloadProgress.current / downloadProgress.total}
                            color="primary"
                        ></IonProgressBar>
                    </>
                ) : (
                    /* Mesaj de așteptare până începe fetch-ul */
                    !downloadError && <h2 style={{ marginTop: '50px' }}>Preparing download...</h2>
                )}

                {downloadError && (
                    <div style={{ marginTop: '30px' }}>
                        <IonText color="danger">
                            <p>Failed to download questions.</p>
                        </IonText>
                        <IonButton onClick={() => startDownload()}>Retry</IonButton>
                    </div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default Downloader;