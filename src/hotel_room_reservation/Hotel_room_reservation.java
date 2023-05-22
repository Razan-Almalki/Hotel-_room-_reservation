package hotel_room_reservation;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Hotel_room_reservation {
    public static final String ROOMS_FILE_NAME = "rooms.txt";
    public static final String RESERVATIONS_FILE_NAME = "reservations.txt";
    public static List<Room> rooms;
    public static List<Reservation> reservations;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Connection con = null;
        try {

            String ConnectionURL = "jdbc:mysql://localhost:3306";

            con = DriverManager.getConnection(ConnectionURL, "Razan", "0559945643");

            Statement st = con.createStatement();

            st.executeUpdate("CREATE DATABASE " + "HotelDatabase");

            System.out.println("1 row(s) affacted");

            con.close();
        } catch (SQLException s) {
            System.out.println("SQL statement is not executed!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //////////////////////////////////////////////////////////
        
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


    public static void displayMenu() {
        int choice = 0;
        do {
            System.out.println("1. View Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. View Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline character
                switch (choice) {
                    case 1:
                        viewRooms();
                        break;
                    case 2:
                        makeReservation();
                        break;
                    case 3:
                        viewReservations();
                        break;
                    case 4:
                        cancelReservation();
                        break;
                    case 5:
                        saveDataToFile();
                        break;
                    default:
                        System.out.println("Invalid choice! Try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Try again.");
                scanner.nextLine(); // consume the invalid input
            } catch (ReservationException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 5 && choice < 5);
    }

    public static void viewRooms() {
        System.out.println("Room Number\tAvailability");
        for (Room room : rooms) {
            System.out.println(room.getRoomNumber() + "\t\t" + (room.isAvailable() ? "Available" : "Not Available"));
        }
    }

    public static void makeReservation() throws ReservationException {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the room number you want to reserve: ");
        int roomNumber = scanner.nextInt();
        Room room = findRoom(roomNumber);
        if (room == null) {
            throw new ReservationException("Room " + roomNumber + " not found!");
        }
        if (!room.isAvailable()) {
            throw new ReservationException("Room " + roomNumber + " is already reserved!");
        }
        System.out.print("Enter the number of nights you want to reserve: ");
        int numberOfNights = scanner.nextInt();
        Reservation reservation = new Reservation(name, roomNumber, numberOfNights);
        reservations.add(reservation);
        room.setAvailable(false);
        Payment(numberOfNights);
        System.out.println("Reservation made successfully!");
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
        try (ObjectOutputStream roomsOut = new ObjectOutputStream(new FileOutputStream(ROOMS_FILE_NAME));
                ObjectOutputStream reservationsOut = new ObjectOutputStream(new FileOutputStream(RESERVATIONS_FILE_NAME))) {
            roomsOut.writeObject(rooms);
            reservationsOut.writeObject(reservations);
            System.out.println("Data saved to file successfully!");
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