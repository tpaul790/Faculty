package ro.mpp2025.object_protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.*;
import ro.mpp2025.object_protocol.requests.*;
import ro.mpp2025.object_protocol.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ClientWorker.class);

    public ClientWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException|ClassNotFoundException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error "+e);
        }
    }

    private void sendResponse(Response response) throws IOException {
        logger.debug("sending response {}",response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private Object handleRequest(Request request) {
        if(request instanceof LogInRequest logInRequest) {
            logger.debug("Login request ...");
            Player player = logInRequest.getPlayer();
            try {
                Player playerWithId = server.logIn(player, this);
                return new LogInResponse(playerWithId);
            } catch (ServiceException e) {
                connected = false; //necesar deoarece cand se apasa login se creeaza alt thread
                return new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof LogOutRequest logOutRequest) {
            logger.debug("Logout request ...");
            Player player = logOutRequest.getPlayer();
            try{
                server.logOut(player, this);
                connected=false;
                return new OkResponse();
            } catch (ServiceException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof GetConfigurationRequest) {
            logger.debug("GetConfiguration request ...");
            try{
                Configuration conf = server.getConfiguration();
                return new GetConfigurationResponse(conf);
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        //alte request uri
        return null;
    }
}
