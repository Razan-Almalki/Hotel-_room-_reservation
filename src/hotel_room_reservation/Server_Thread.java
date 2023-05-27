package hotel_room_reservation;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/*this class represent a server thread that handles a client connection.
The constructor takes a Socket object representing the incoming connection 
and initializes the writer and reader objects to communicate with the client.*/
public class Server_Thread implements Runnable {

    private Socket incoming;
    private PrintWriter writer;
    private Scanner reader;

    public Server_Thread(Socket incomingS) throws IOException {
        incoming = incomingS;
        writer = new PrintWriter(incomingS.getOutputStream(), true);
        reader = new Scanner(incomingS.getInputStream());
    }

    @Override
    public void run() {
        System.out.println("Welcome :)");
        System.out.println("Client connect via: " + incoming.getLocalAddress());

    }
}
