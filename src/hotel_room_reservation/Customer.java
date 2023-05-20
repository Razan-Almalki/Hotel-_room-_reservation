package hotel_room_reservation;
import static hotel_room_reservation.Hotel_room_reservation.loadReservationsFromFile;
import static hotel_room_reservation.Hotel_room_reservation.loadRoomsFromFile;
import java.io.IOException;
import java.net.Socket;
public class Customer {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8800;
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            Hotel_room_reservation.rooms = loadRoomsFromFile();
            Hotel_room_reservation.reservations = loadReservationsFromFile();
            Hotel_room_reservation.displayMenu();
        } catch (ClassNotFoundException | IOException ex) {
           ex.printStackTrace();
        }

    }
}
