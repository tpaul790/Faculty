import java.io.*;
import java.net.Socket;

public class ClientHandler2 extends AbstractClientHandler{
    ClientHandler2(Socket socket) {
        super(socket);
    }
    @Override
    public void run() {
        try{
            PrintWriter writer = new PrintWriter(super.getSocket().getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));

            //citesc sirul de la client
            String sir = reader.readLine();

            //il inversez si il trimit inapoi
            StringBuilder sb = new StringBuilder(sir);
            sir = sb.reverse().toString();
            writer.println(sir);

            writer.close();
            reader.close();
        }catch (IOException e){
            System.out.print(e.getMessage());
        }
    }
}
