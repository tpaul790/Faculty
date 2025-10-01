package ro.mpp2025;

public interface IServices {
    Player logIn(Player user, IObserver player) throws ServiceException;
    void logOut(Player user, IObserver client) throws ServiceException;

    Configuration getConfiguration() throws ServiceException;

//    void saveGame(Game game);
}
