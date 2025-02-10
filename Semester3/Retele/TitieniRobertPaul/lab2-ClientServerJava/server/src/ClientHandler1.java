import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler1 extends AbstractClientHandler{
    ClientHandler1(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(super.getSocket().getInputStream());
            // Citirea mesajului de la client
            int n = in.readInt();
            int[] sir = new int[n];
            int s = 0;
            for (int i = 0; i < n; i++) {
                sir[i] = in.readInt();
                s += sir[i];
            }

            // Răspuns către client
            DataOutputStream out = new DataOutputStream(super.getSocket().getOutputStream());
            out.writeInt(s);

            // Închidem conexiunea
            in.close();
            out.close();
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
