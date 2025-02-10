import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {
    private Scanner scanner = new Scanner(System.in);
    private int port;
    private String serverAdress;

    public Client1(String serverAdress, int port) {
        this.serverAdress = serverAdress;
        this.port = port;
    }

    public void runClient(){
        try(Socket socket = new Socket(serverAdress,port)){
            System.out.println("Connected to " + socket.getRemoteSocketAddress());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(1);
            System.out.print("Introduceti numarul n: ");
            int n=0;
            try {
                n = scanner.nextInt();
            }catch (Exception e){
                System.out.print("Numar Invalid!");
                return;
            }
            out.writeInt(n);

            System.out.print("Introduceti cele n numere: ");
            int[] sir = new int[n];
            for(int i = 0 ; i < n ; i++){
                try {
                    sir[i] = scanner.nextInt();
                }catch (Exception e){
                    System.out.print("S-a detectat ceva nenumeric in sir!");
                    return;
                }
                out.writeInt(sir[i]);
            }

            DataInputStream in = new DataInputStream(socket.getInputStream());
            int sum = in.readInt();

            System.out.println("Suma este: "+sum);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
