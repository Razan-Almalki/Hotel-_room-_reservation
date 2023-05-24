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
//import java.util.concurrent.locks.ReentrantLock;

public class Hotel_room_reservation {
//    public static final String ROOMS_FILE_NAME = "rooms.txt";
//    public static final String RESERVATIONS_FILE_NAME = "reservations.txt";
//    public static List<Room> rooms;
//    public static List<Reservation> reservations;
    public static Scanner scanner = new Scanner(System.in);
//    public static ObjectOutputStream roomsOut;
//    public static ObjectOutputStream reservationsOut;
//    private static ReentrantLock bankLock = new ReentrantLock();

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
        
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", "root", "Qazwsx12!");

        Statement st = con.createStatement();
        
        System.out.println("Room Number\tAvailability");
        
        ResultSet result = st.executeQuery("SELECT * FROM Room");
        
        while (result.next()) {

            if (result.getString(2).equalsIgnoreCase("true")) {

                System.out.println(result.getInt(1) + "\t\tAvailable");
            } else {
                
                System.out.println(result.getInt(1) + "\t\tNot Available");
            }
        }

        System.out.println("");
        result.close();
        st.close();
        con.close();
    }

    public static void makeReservation() throws ReservationException, FileNotFoundException, IOException, SQLException {
//        
//        bankLock.lock();
//        try {

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
                        PreparedStatement preQueryStat = con.prepareStatement("INSERT INTO Reservation VALUES (?, ?, ?)");

                        preQueryStat.setString(1, name);
                        preQueryStat.setInt(2, roomNumber);
                        preQueryStat.setInt(3, numberOfNights);

                        preQueryStat.executeUpdate();

                        preQueryStat = con.prepareStatement("UPDATE Room SET Availability = 'false' WHERE ID = ?");
                        preQueryStat.setInt(1, roomNumber);
                        preQueryStat.executeUpdate();

                        Payment(numberOfNights);
                        System.out.println("Reservation made successfully!");
                    } else {
                        
                        throw new ReservationException("Room " + roomNumber + " is already reserved!");
                    }
                }

                if (!(result.getInt(1) > 0 && result.getInt(1) < 11)) {

                    throw new ReservationException("Room " + result.getInt(1) + " not found!");
                }
            }
//        } finally {    
//            bankLock.unlock();    
//        }
    }

    public static void viewReservations() throws SQLException {
        
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", "root", "Qazwsx12!");
            
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        PreparedStatement preQueryStat = con.prepareStatement("SELECT * FROM Reservation WHERE name = ?");
        preQueryStat.setString(1, name);
        
        ResultSet result = preQueryStat.executeQuery();
        
        if (result == null) {
            
            System.out.println("No reservations found!");
        } 
        
        else {
            
            System.out.println("Name\tRoom Number\tNumber of Days");
            
            while(result.next()) {
                
                System.out.print(result.getString(1) + "\t"); 
                System.out.print(result.getInt(2) + "\t\t");
                System.out.print(result.getInt(3)); 
                System.out.println();
            }
            
            System.out.println("");
        }
    }

    public static void cancelReservation() throws ReservationException, SQLException {
        
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", "root", "Qazwsx12!");
            
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter the room number of the reservation you want to cancel: ");
        int roomNumber = scanner.nextInt();
        
        ResultSet result = st.executeQuery("SELECT * FROM Reservation WHERE Name = '" + name + "' AND Room_no = " + roomNumber);
        
        if (result == null) {
            
            throw new ReservationException("Reservation not found for room " + roomNumber + "!");
        } else {
            
            PreparedStatement preQueryStat = con.prepareStatement("DELETE FROM Reservation WHERE Name = ? AND Room_no = ?");
            preQueryStat.setString(1, name);
            preQueryStat.setInt(2, roomNumber);
            preQueryStat.executeUpdate();
            preQueryStat = con.prepareStatement("UPDATE Room SET Availability = 'true' WHERE ID = ?");
            preQueryStat.setInt(1, roomNumber);
            preQueryStat.executeUpdate();
            System.out.println("Reservation cancelled successfully!");
        }
        
        
    }

//    public static Room findRoom(int roomNumber) {
//        for (Room room : rooms) {
//            if (room.getRoomNumber() == roomNumber) {
//                return room;
//            }
//        }
//        return null;
//    }
//
//    public static Reservation findReservation(int roomNumber) {
//        for (Reservation reservation : reservations) {
//            if (reservation.getRoomNumber() == roomNumber) {
//                return reservation;
//            }
//        }
//        return null;
//    }
//
//    public static void saveDataToFile() throws IOException {
//        try {
//            System.out.println("Data saved to file successfully!");
//        } finally {
//            roomsOut.close();
//            reservationsOut.close();
//        }
//    }
//
//    public static List<Room> loadRoomsFromFile() throws IOException, ClassNotFoundException {
//        
//        File file = new File(ROOMS_FILE_NAME);
//        if (!file.exists()) {
////            return createRooms();
//        }
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
//            return (List<Room>) in.readObject();
//        }
//    }
//
//    public static List<Reservation> loadReservationsFromFile() throws IOException, ClassNotFoundException {
//        File file = new File(RESERVATIONS_FILE_NAME);
//        if (!file.exists()) {
//            return new ArrayList<>();
//        }
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
//            return (List<Reservation>) in.readObject();
//        }
//    }

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
}