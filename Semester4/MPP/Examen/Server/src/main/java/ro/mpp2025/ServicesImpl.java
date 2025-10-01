package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.interfaces.IConfigurationRepo;
import ro.mpp2025.interfaces.IPlayerRepo;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements IServices {
    private static final Logger logger = LogManager.getLogger(ServicesImpl.class);
    //repo-uri
    private IPlayerRepo playerRepo;
    private IConfigurationRepo configurationRepo;

    private Map<String, IObserver> loggedPlayers;

    public ServicesImpl(IPlayerRepo playerRepo, IConfigurationRepo configurationRepo) {
        this.playerRepo = playerRepo;
        this.configurationRepo = configurationRepo;
        this.loggedPlayers = new HashMap<>();
    }


    @Override
    public Player logIn(Player player, IObserver client) throws ServiceException {
        logger.info("Trying to login with player: " + player.getNickname());
        Optional<Player> emp = playerRepo.findBy(player.getNickname());
        if (emp.isPresent()) {
            if(loggedPlayers.containsKey(emp.get().getNickname())) {
                throw new ServiceException("Player already logged in");
            }
            loggedPlayers.put(emp.get().getNickname(),client);
            return emp.get();
        }else
            throw new ServiceException("Player not found");
    }

    @Override
    public void logOut(Player player, IObserver client) throws ServiceException {
        IObserver localClient = loggedPlayers.remove(player.getNickname());
        if(localClient == null)
            throw new ServiceException("User not logged in");
    }

    @Override
    public Configuration getConfiguration() throws ServiceException {
        List<Configuration> configurations = configurationRepo.findAll();
        int n = configurationRepo.size();
        int index = (int) (Math.random() * n);
        return configurations.get(index);
    }

//    private static final int DEFAULT_THREADS = 3;
//    public void notifyLoggedPlayers(Game game) throws ServiceException {
//        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_THREADS);
//        for(IObserver client : loggedPlayers.values()) {
//            executor.execute(() -> {
//                try {
//                    client.gameFinished(game);
//                } catch (ServiceException e) {
//                    logger.error("Error notifying the other players" + e.getMessage());
//                }
//            });
//        }
//        executor.shutdown();
//    }
}
