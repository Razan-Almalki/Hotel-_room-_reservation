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
import javax.swing.JOptionPane;
//import java.util.concurrent.locks.ReentrantLock;

public class Hotel_room_reservation {
//    public static final String ROOMS_FILE_NAME = "rooms.txt";

    public static final String RESERVATIONS_FILE_NAME = "reservations.txt";
//    public static List<Room> rooms;
    //public static List<Reservation> reservations;
    public static Scanner scanner = new Scanner(System.in);
//    public static ObjectOutputStream roomsOut;
//    public static ObjectOutputStream reservationsOut;
//    private static ReentrantLock bankLock = new ReentrantLock();

    public static String host = "Ruba";
    public static String passWord = "Ruba20";

    public static void main(String[] args) {

        try (
                ServerSocket server = new ServerSocket(8800)) {

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

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);
        Statement st = con.createStatement();
        JOptionPane j = new JOptionPane();
        ArrayList<String> p = new ArrayList<String>();

        //System.out.println("Room Number\tAvailability");
        //j.showMessageDialog(null, "Room Number                 Availability");
        p.add("    Room Number                 Availability\n");
        p.add("____________________________________\n");
        ResultSet result = st.executeQuery("SELECT * FROM Room");

        while (result.next()) {

            if (result.getString(2).equalsIgnoreCase("true")) {
                //System.out.println(result.getInt(1) + "\t\tAvailable");
                p.add("              " + result.getInt(1) + "                            Available\n");
                //j.showMessageDialog(null, result.getInt(1) + "\t\tAvailable");

            } else {
                //System.out.println(result.getInt(1) + "\t\tNot Available");
                p.add("              " + result.getInt(1) + "                            Not Available\n");
                //j.showMessageDialog(null, result.getInt(1) + "\t\tNot Available");
            }

        }

        String total = "";
        for (String element : p) {
            total += element;
        }

        j.showMessageDialog(null, total);

        System.out.println("");
        result.close();
        st.close();
        con.close();
    }

    public static void makeReservation() throws ReservationException, FileNotFoundException, IOException, SQLException {
//        
//        bankLock.lock();
//        try {
        PrintWriter Writer = new PrintWriter(new FileWriter(RESERVATIONS_FILE_NAME, true));
        Writer.println("--------------RESERVATIONS REPORT--------------");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);

        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String name = JOptionPane.showInputDialog("Enter your name: ");
        // System.out.print("Enter your name: ");
        // String name = scanner.nextLine();
        String roomNumberstring = JOptionPane.showInputDialog("Enter the room number you want to reserve: ");
        int roomNumber = Integer.parseInt(roomNumberstring);
        //System.out.print("Enter the room number you want to reserve: ");
        // int roomNumber = scanner.nextInt();
        //  int numberOfNights = 0;

        ResultSet result = st.executeQuery("SELECT * FROM Room");

        while (result.next()) {

            if (roomNumber == result.getInt(1)) {

                if (result.getString(2).equalsIgnoreCase("true")) {
                    String numberOfNightsString = JOptionPane.showInputDialog("Enter the number of nights you want to reserve: ");
                    int numberOfNights = Integer.parseInt(numberOfNightsString);
                    // System.out.print("Enter the number of nights you want to reserve: ");
                    // numberOfNights = scanner.nextInt();
                    PreparedStatement preQueryStat = con.prepareStatement("INSERT INTO Reservation VALUES (?, ?, ?)");

                    preQueryStat.setString(1, name);
                    preQueryStat.setInt(2, roomNumber);
                    preQueryStat.setInt(3, numberOfNights);
                    preQueryStat.executeUpdate();

                    preQueryStat = con.prepareStatement("UPDATE Room SET Availability = 'false' WHERE ID = ?");
                    preQueryStat.setInt(1, roomNumber);
                    preQueryStat.executeUpdate();

                    Payment(numberOfNights);
                    JOptionPane.showMessageDialog(null, "Reservation made successfully!");
                    // System.out.println("Reservation made successfully!");
                    Writer.println("\nName of the customer is: " + name + "\nRoom number is: " + roomNumber + "\nReservation for " + numberOfNights + " Nights");
                    Writer.flush();
                } else {
                    JOptionPane.showMessageDialog(null, "Room " + roomNumber + " is already reserved!");
                    throw new ReservationException("Room " + roomNumber + " is already reserved!");

                }
            }

            if (!(result.getInt(1) > 100 && result.getInt(1) < 111)) {
                JOptionPane.showMessageDialog(null, "Room " + result.getInt(1) + " not found!");
                throw new ReservationException("Room " + result.getInt(1) + " not found!");
            }
        }
//        } finally {    
//            bankLock.unlock();    
//        }
    }

    public static void viewReservations() throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);

        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String name = JOptionPane.showInputDialog("Enter your name: ");
        // System.out.print("Enter your name: ");
        // String name = scanner.nextLine();
        ArrayList<String> p = new ArrayList<String>();
        PreparedStatement preQueryStat = con.prepareStatement("SELECT * FROM Reservation WHERE name = ?");
        preQueryStat.setString(1, name);

        ResultSet result = preQueryStat.executeQuery();

        if (result == null) {
            JOptionPane.showMessageDialog(null, "No reservations found!");
            // System.out.println("No reservations found!");
        } else {
            //JOptionPane.showMessageDialog(null,"Name\tRoom Number\tNumber of Days");
            //System.out.println("Name\tRoom Number\tNumber of Days");
            p.add("Name    Room Number    Number of Days\n");

            while (result.next()) {

//                JOptionPane.showMessageDialog(null,result.getString(1) + "\t");
//                 JOptionPane.showMessageDialog(null,result.getString(2) + "\t\t");
//                  JOptionPane.showMessageDialog(null,result.getString(3) );
                p.add(result.getString(1) + "                        ");
                p.add(result.getInt(2) + "                              ");
                p.add(result.getInt(3) + "\n");
//                System.out.print(result.getString(1) + "\t");
//                System.out.print(result.getInt(2) + "\t\t");
//                System.out.print(result.getInt(3));
                //System.out.println();
            }

            System.out.println("");
        }
        String total = "";
        for (String element : p) {
            total += element;
        }

        JOptionPane.showMessageDialog(null, total);

    }

    public static void cancelReservation() throws ReservationException, SQLException {

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);

        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String name = JOptionPane.showInputDialog("Enter your name: ");
        //System.out.print("Enter your name: ");
        //String name = scanner.nextLine();
        String roomNumberString = JOptionPane.showInputDialog("Enter the room number of the reservation you want to cancel: ");
        int roomNumber = Integer.parseInt(roomNumberString);
        //System.out.print("Enter the room number of the reservation you want to cancel: ");
        // int roomNumber = scanner.nextInt();

        ResultSet result = st.executeQuery("SELECT * FROM Reservation WHERE Name = '" + name + "' AND Room_no = " + roomNumber);

        if (result == null ) {

            JOptionPane.showMessageDialog(null, "Reservation not found for room " + roomNumber + "!");
            throw new ReservationException("Reservation not found for room " + roomNumber + "!");
        } else {

            PreparedStatement preQueryStat = con.prepareStatement("DELETE FROM Reservation WHERE Name = ? AND Room_no = ?");
            preQueryStat.setString(1, name);
            preQueryStat.setInt(2, roomNumber);
            preQueryStat.executeUpdate();
            preQueryStat = con.prepareStatement("UPDATE Room SET Availability = 'true' WHERE ID = ?");
            preQueryStat.setInt(1, roomNumber);
            preQueryStat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Reservation cancelled successfully!");
            //System.out.println("Reservation cancelled successfully!");
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

        // System.out.print("The amount of payment is (" + amount + ") Ryal: ");
        String payment = JOptionPane.showInputDialog("The amount of payment is (" + amount + ") Ryal: ");
        int pay = Integer.parseInt(payment);
        try {

            //Scanner scanner = new Scanner(System.in);
            //int pay = scanner.nextInt();
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
