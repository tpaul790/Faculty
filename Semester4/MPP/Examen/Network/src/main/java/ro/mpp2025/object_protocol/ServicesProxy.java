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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesProxy implements IServices {
    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private static Logger logger = LogManager.getLogger(ServicesProxy.class);
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses= new LinkedBlockingQueue<>();
    }

    private void sendRequest(Request request)throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }
    }

    private Response readResponse() {
        Response response=null;
        try{
            response=qresponses.take();
        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private void initializeConnection() {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(UpdateResponse update){
        //raspunsurile de tip updateResponse - pentru observer
    }

    @Override
    public Player logIn(Player user, IObserver player) throws ServiceException {
        initializeConnection();
        sendRequest(new LogInRequest(user));
        Response response=readResponse();

        if (response instanceof LogInResponse logInResponse) {
            this.client=player;
            return logInResponse.getPlayer();
        }
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ServiceException(err.getMessage());
        }
        return null;
    }

    @Override
    public void logOut(Player player, IObserver client) throws ServiceException {
        sendRequest(new LogOutRequest(player));
        Response response=readResponse();
        closeConnection();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServiceException(err.getMessage());
        }
    }

    @Override
    public Configuration getConfiguration() throws ServiceException {
        sendRequest(new GetConfigurationRequest());
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServiceException(err.getMessage());
        }
        GetConfigurationResponse resp=(GetConfigurationResponse) response;
        return resp.getConfiguration();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    logger.debug("response received {}",response);
                    if (response instanceof UpdateResponse updateResponse) {
                        handleUpdate(updateResponse);
                    }else{
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }
                } catch (IOException|ClassNotFoundException e) {
                    logger.error("Reading error "+e);
                }
            }
        }
    }


}
