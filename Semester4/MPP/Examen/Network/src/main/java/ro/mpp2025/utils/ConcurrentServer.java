package ro.mpp2025.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.IServices;
import ro.mpp2025.object_protocol.ClientWorker;
import java.net.Socket;


public class ConcurrentServer extends AbstractConcurrentServer {
    private IServices server;
    private static Logger logger = LogManager.getLogger(ConcurrentServer.class);

    public ConcurrentServer(int port, IServices server) {
        super(port);
        this.server = server;
        logger.info("Concurrent Server created");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker=new ClientWorker(server, client);
        return new Thread(worker);
    }
}
