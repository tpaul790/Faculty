package ro.mpp2025;

import java.time.LocalDateTime;
import java.util.List;

public interface IServices {
    Player logIn(Player user, IObserver player) throws ServiceException;
    void finishGame(Game game) throws ServiceException;
    Game[] findAllGames() throws ServiceException;
    Configuration[] findAllConfigurations() throws ServiceException;
}
