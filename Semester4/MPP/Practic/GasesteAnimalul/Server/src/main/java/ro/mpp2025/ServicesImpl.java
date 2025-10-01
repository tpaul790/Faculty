package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.interfaces.IAttemptRepo;
import ro.mpp2025.interfaces.IConfigurationRepo;
import ro.mpp2025.interfaces.IGameRepo;
import ro.mpp2025.interfaces.IPlayerRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements IServices {
    private static final Logger logger = LogManager.getLogger(ServicesImpl.class);
    private IAttemptRepo attemptRepo;
    private IConfigurationRepo configurationRepo;
    private IGameRepo gameRepo;
    private IPlayerRepo playerRepo;
    private Map<String, IObserver> loggedPlayers;

    public ServicesImpl(IAttemptRepo attemptRepo, IConfigurationRepo configurationRepo, IGameRepo gameRepo, IPlayerRepo playerRepo) {
        this.attemptRepo = attemptRepo;
        this.configurationRepo = configurationRepo;
        this.gameRepo = gameRepo;
        this.playerRepo = playerRepo;
        this.loggedPlayers = new HashMap<>();
    }


    @Override
    public Player logIn(Player user, IObserver player) throws ServiceException {
        logger.info("Trying to login with player: " + user.getNickname());
        Optional<Player> emp = playerRepo.findBy(user.getNickname());
        if (emp.isPresent()) {
            if(loggedPlayers.containsKey(emp.get().getNickname())) {
                throw new ServiceException("User already logged in");
            }
            loggedPlayers.put(emp.get().getNickname(),player);
            return emp.get();
        }else
            throw new ServiceException("User not found");
    }

    @Override
    public void finishGame(Game game) throws ServiceException {
        gameRepo.save(game);
        notifyLoggedPlayers(game);
    }

    private static final int DEFAULT_THREADS = 3;
    public void notifyLoggedPlayers(Game game) throws ServiceException {
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_THREADS);
        for(IObserver client : loggedPlayers.values()) {
            executor.execute(() -> {
                try {
                    client.gameFinished(game);
                } catch (ServiceException e) {
                    logger.error("Error notifying the other empoyees" + e.getMessage());
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public Game[] findAllGames() throws ServiceException {
        List<Game> games = (List<Game>)gameRepo.findAll();
        return games.toArray(new Game[games.size()]);
    }

    @Override
    public Configuration[] findAllConfigurations() throws ServiceException {
        List<Configuration> conf = (List<Configuration>)configurationRepo.findAll();
        return conf.toArray(new Configuration[conf.size()]);
    }
}
