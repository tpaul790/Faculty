package ro.mpp2025;

import ro.mpp2025.Game;

public interface IObserver {
    void gameFinished(Game game) throws ServiceException;
}

