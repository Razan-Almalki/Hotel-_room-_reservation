package hotel_room_reservation;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Hotel_room_reservation {
    public static final String ROOMS_FILE_NAME = "rooms.txt";
    public static final String RESERVATIONS_FILE_NAME = "reservations.txt";
    public static List<Room> rooms;
    public static List<Reservation> reservations;
    public static Scanner scanner = new Scanner(System.in);
    public static ObjectOutputStream roomsOut;
    public static ObjectOutputStream reservationsOut;
                
    public static void main(String[] args) {
        
        try (ServerSocket server = new ServerSocket(8800)) {
            System.out.println("Server waiting Connection...");
            while (true) {
                 
                Socket so = server.accept();
                Runnable r = new Server_Thread(so);
                Thread t1 = new Thread(r);
                t1.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewRooms() throws SQLException {
        System.out.println("Room Number\tAvailability");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase","root", "Qazwsx12!");
            
            Statement st = con.createStatement();
            
            ResultSet result = st.executeQuery("SELECT * FROM Room");
            
            while (result.next()) {
                
                if (result.getString(2).equalsIgnoreCase("true")) {
                    
                    System.out.println(result.getInt(1) + "\t\tAvailable");
                }
                else
                    System.out.println(result.getInt(1) + "\t\tNot Available");
            }
            
            System.out.println("");
            result.close();
            st.close();
            con.close();
    }

    public static void makeReservation() throws ReservationException, FileNotFoundException, IOException, SQLException {
        
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", "root", "Qazwsx12!");
            
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the room number you want to reserve: ");
        int roomNumber = scanner.nextInt();
        int numberOfNights = 0;
        
        ResultSet result = st.executeQuery("SELECT * FROM Room");
        
        while (result.next()) {
            
            if (roomNumber == result.getInt(1)) {
                
                if (result.getString(2).equalsIgnoreCase("true")) {
                    
                    System.out.print("Enter the number of nights you want to reserve: ");
                    numberOfNights = scanner.nextInt();
                    PreparedStatement preQueryStat_reservation = con.prepareStatement("INSERT INTO Reservation VALUES (?, ?, ?)");

                    preQueryStat_reservation.setString(1, name);
                    preQueryStat_reservation.setInt(2, roomNumber);
                    preQueryStat_reservation.setInt(3, numberOfNights);

                    preQueryStat_reservation.executeUpdate();
                    
                    preQueryStat_reservation = con.prepareStatement("UPDATE Room SET Availability='false' WHERE ID='" + roomNumber + "'");
                    
                    preQueryStat_reservation.executeUpdate();
                    
                    Payment(numberOfNights);
                    System.out.println("Reservation made successfully!");
                }
                
                else
                    throw new ReservationException("Room " + roomNumber + " is already reserved!");
            }
            
            if (!(roomNumber > 0 && roomNumber < 11)) {
                
                throw new ReservationException("Room " + roomNumber + " not found!");
            }
        }
    }

    public static void viewReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found!");
        } else {
            System.out.println("Name\tRoom Number\tNumber of Nights");
            for (Reservation reservation : reservations) {
                System.out.println(reservation.getName() + "\t" + reservation.getRoomNumber() + "\t\t" + reservation.getNumberOfNights());
            }
        }
    }

    public static void cancelReservation() throws ReservationException {
        System.out.print("Enter the room number of the reservation you want to cancel: ");
        int roomNumber = scanner.nextInt();
        Reservation reservation = findReservation(roomNumber);
        if (reservation == null) {
            throw new ReservationException("Reservation not found for room " + roomNumber + "!");
        }
        Room room = findRoom(roomNumber);
        if (room == null) {
            throw new ReservationException("Room " + roomNumber + " not found!");
        }
        reservations.remove(reservation);
        room.setAvailable(true);
        System.out.println("Reservation cancelled successfully!");
    }

    public static Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public static Reservation findReservation(int roomNumber) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoomNumber() == roomNumber) {
                return reservation;
            }
        }
        return null;
    }

    public static void saveDataToFile() throws IOException {
        try {
            System.out.println("Data saved to file successfully!");
        } finally {
            roomsOut.close();
            reservationsOut.close();
        }
    }

    public static List<Room> loadRoomsFromFile() throws IOException, ClassNotFoundException {
        
        File file = new File(ROOMS_FILE_NAME);
        if (!file.exists()) {
            return createRooms();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Room>) in.readObject();
        }
    }

    public static List<Reservation> loadReservationsFromFile() throws IOException, ClassNotFoundException {
        File file = new File(RESERVATIONS_FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Reservation>) in.readObject();
        }
    }

    public static List<Room> createRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            rooms.add(new Room(i));
        }
        return rooms;
    }

    public static void Payment(int nights) {
        int CostOfOneNight = 350;
        double amount = 0.0;
        if (nights > 1) {
            amount = CostOfOneNight * nights;
        } else {
            amount = CostOfOneNight;
        }
        System.out.print("The amount of payment is (" + amount + ") Ryal: ");
        try {
            Scanner scanner = new Scanner(System.in);
            int pay = scanner.nextInt();
            if (pay <= 0 || pay != amount) {
                System.out.println("You entered the wrong amount! Please try again.");
                Payment(nights);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            Payment(nights);
        }
        

    }
    
    public static void DBStore() {
        Connection conn = null;
        Statement s = null;
        try {
            String Table = "CREATE TABLE Room "
                    + "(RoomID INT, "
                    + "isAvailable BOOL";
            s = conn.createStatement();
            s.executeUpdate(Table);
            
            String Records = "INSERT INTO Room "
                    + "(RoomID, isAvailable) VALUES "
                    + "(101, true), "
                    + "(102, true), "
                    + "(103, true), "
                    + "(104, true), "
                    + "(105, true), "
                    + "(106, true), "
                    + "(107, true), "
                    + "(108, true), "
                    + "(109, true), "
                    + "(110, true) ";
            
            s.executeUpdate(Records);

            s.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }       
}