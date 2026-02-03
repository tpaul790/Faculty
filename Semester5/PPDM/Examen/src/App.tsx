import React from 'react';
import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import Home from './pages/Home';
import EditTask from './pages/EditTask';
import { TaskProvider } from './hooks/TaskContext';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

setupIonicReact();

const App: React.FC = () => (
    <IonApp>
        {/* Învelim aplicația în Provider pentru a avea acces la starea globală */}
        <TaskProvider>
            <IonReactRouter>
                <IonRouterOutlet>
                    <Route exact path="/home">
                        <Home />
                    </Route>
                    <Route path="/edit/:id">
                        <EditTask />
                    </Route>
                    <Route exact path="/">
                        <Redirect to="/home" />
                    </Route>
                </IonRouterOutlet>
            </IonReactRouter>
        </TaskProvider>
    </IonApp>
);

export default App;