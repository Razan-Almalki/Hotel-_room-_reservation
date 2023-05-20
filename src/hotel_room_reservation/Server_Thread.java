package hotel_room_reservation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server_Thread implements Runnable {
    private Socket incoming;
    private PrintWriter writer;
    private Scanner reader;
    public Server_Thread(Socket incomingS) throws IOException {
        incoming = incomingS;
        writer=new PrintWriter(incomingS.getOutputStream(), true);             
        reader=new Scanner (incomingS.getInputStream());     
    } 

    @Override
    public void run() {
        System.out.println("Welcome :)");
        System.out.println("Client connect via: " + incoming.getLocalAddress());
      
    }

}
