import React from 'react';
import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import ZonePage from './pages/ZonePage';
import InventoryPage from './pages/InventoryPage';
import { useInventory } from './hooks/useInventory';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

import './theme/variables.css';

setupIonicReact();

const App: React.FC = () => {
    const { saveZone, isZoneLocked } = useInventory();

    return (
        <IonApp>
            <IonReactRouter>
                <IonRouterOutlet>
                    <Route exact path="/zone">
                        <ZonePage onSetZone={saveZone} isLocked={isZoneLocked} />
                    </Route>
                    <Route exact path="/audit">
                        {isZoneLocked ? <InventoryPage /> : <Redirect to="/zone" />}
                    </Route>
                    <Route exact path="/">
                        <Redirect to={isZoneLocked ? "/audit" : "/zone"} />
                    </Route>
                </IonRouterOutlet>
            </IonReactRouter>
        </IonApp>
    );
};

export default App;