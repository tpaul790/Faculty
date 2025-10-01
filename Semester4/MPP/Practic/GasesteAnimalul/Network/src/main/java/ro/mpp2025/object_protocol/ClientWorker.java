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
                Thread.sleep(10);
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
        if(request instanceof LogInRequest) {
            logger.debug("Login request ...");
            LogInRequest logReq = (LogInRequest) request;
            Player player = logReq.getPlayer();
            try {
                player = server.logIn(player, this);
                return new LogInResponse(player);
            } catch (ServiceException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof GetAllGamesRequest){
            logger.debug("GetAllFlights request ...");
            try{
                Game[] games = server.findAllGames();

                return new GetAllGamesResponse(games);
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof GetAllConfigurationsRequest){
            logger.debug("GetAllConfigurationsRequest request ...");
            try{
                Configuration[] confs = server.findAllConfigurations();
                return new GetAllConfigurationsResponse(confs);
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof SaveGameRequest saveGameRequest){
            logger.debug("BuyTicketRequest request ...");
            Game game = saveGameRequest.getGame();
            try{
                server.finishGame(game);
                return new OkResponse();
            }catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void gameFinished(Game game) throws ServiceException {
        try{
            sendResponse(new GameFinishedResponse(game));
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }
}
