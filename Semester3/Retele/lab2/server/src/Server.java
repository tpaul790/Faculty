import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int maxThreads = 10;
    public static final int port = 1234;

    public static void runServer() {
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
        while(true) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                int op = in.readInt();
                System.out.println("S-a conectat clientul: C" + op );

                //in functie de ce client e voi crea handlerul potrivit si ii execut functia run folosind executorul
                AbstractClientHandler handler = null;
                if(op == 1) {
                    handler = new ClientHandler1(clientSocket);
                }else if(op == 2) {
                    handler = new ClientHandler2(clientSocket);
                }else{
                    System.out.println("Nu putem rezolva aceasta problema!");
                }
                if(handler != null)
                    executor.execute(handler);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
