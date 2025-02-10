import java.net.Socket;

public abstract class AbstractClientHandler implements Runnable {
    private Socket socket;

    public AbstractClientHandler(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public abstract void run();
}
