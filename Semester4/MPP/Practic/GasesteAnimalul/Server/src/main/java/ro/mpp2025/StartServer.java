package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.interfaces.IAttemptRepo;
import ro.mpp2025.interfaces.IConfigurationRepo;
import ro.mpp2025.interfaces.IGameRepo;
import ro.mpp2025.interfaces.IPlayerRepo;
import ro.mpp2025.utils.AbstractServer;
import ro.mpp2025.utils.ConcurrentServer;
import ro.mpp2025.utils.ServerException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort=55555;
    private static Logger logger = LogManager.getLogger(StartServer.class);
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
            logger.info ("Server properties set. {} ", serverProps);
            //serverProps.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find server.properties "+e);
            logger.debug ("Looking into directory {} ", (new File(".")).getAbsolutePath());
            return;
        }
        IAttemptRepo aRepo = new AttemptRepo();
        IConfigurationRepo cRepo = new ConfigurationRepo();
        IGameRepo gRepo = new GameRepo();
        IPlayerRepo pRepo = new PlayerRepo();

        IServices services = new ServicesImpl(aRepo,cRepo,gRepo,pRepo);

        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            logger.error("Wrong  Port Number"+nef.getMessage());
            logger.debug("Using default port "+defaultPort);
        }
        logger.debug("Starting server on port: "+chatServerPort);
        AbstractServer server = new ConcurrentServer(chatServerPort, services);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server" + e.getMessage());
        }
    }
}
