package hotel_room_reservation;
import static hotel_room_reservation.Hotel_room_reservation.loadReservationsFromFile;
import static hotel_room_reservation.Hotel_room_reservation.loadRoomsFromFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server_Thread implements Runnable{
 private Socket incoming;
 private PrintWriter writer;
 private Scanner reader;

    public Server_Thread(Socket incomingS) {
        incoming = incomingS;
    }
    @Override
    public void run() {
         try (// (4) Create input stream obj(Scanner)
                InputStream is = incoming.getInputStream();
                Scanner in = new Scanner(is);
                // (5) Create output stream obj (PrintWriter)
                OutputStream os = incoming.getOutputStream();
                PrintWriter out = new PrintWriter(os, true);) {
            out.println("Welcome all.");
            System.out.println("Client connect via: " +incoming.getLocalAddress() );
            while (in.hasNextLine()) {
                String str = in.nextLine();
                 
                System.out.println("Client says: " + str);

                if (str.trim().equals("BYE")) {
                    break;
                }

                out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
