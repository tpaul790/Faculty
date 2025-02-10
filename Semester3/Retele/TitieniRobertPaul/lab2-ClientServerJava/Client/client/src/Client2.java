import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    private static Scanner scanner = new Scanner(System.in);
    private int port;
    private String serverAdrress;

    public Client2(String serverAdrress,int port) {
        this.serverAdrress = serverAdrress;
        this.port = port;
    }

    public void runClient(){
        try(Socket socket = new Socket(serverAdrress,port)){
            System.out.println("Connected to server: " + socket.getRemoteSocketAddress());
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //trimit optiunea
            out.writeInt(2);

            //citesc si trimit siurl
            System.out.print("Introduceti sirul de caractere: ");
            String str = scanner.nextLine();

            writer.println(str);

            //citesc rezultatul si il afisez
            str = reader.readLine();
            System.out.print("Sirul oglindit este: "+str);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
