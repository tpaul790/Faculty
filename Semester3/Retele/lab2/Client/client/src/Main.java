import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Client1 client1 = new Client1(args[0], Integer.parseInt(args[1]));
        client1.runClient();
    }
}