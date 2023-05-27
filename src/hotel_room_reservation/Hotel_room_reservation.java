package hotel_room_reservation;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Hotel_room_reservation {

    public static final String RESERVATIONS_FILE_NAME = "reservations.txt";
    public static Scanner scanner = new Scanner(System.in);
    public static String host = "Razan";
    public static String passWord = "0559945643";
    

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
            e.getMessage();
        }
    }

    public static void viewRooms() throws SQLException {

        // Establish a connection to the database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);
        Statement st = con.createStatement();
        ResultSet result = st.executeQuery("SELECT * FROM Room");

        ArrayList<String> p = new ArrayList<String>();
        p.add("    Room Number                 Availability\n");
        p.add("____________________________________\n");

        while (result.next()) {

            if (result.getString(2).equalsIgnoreCase("true")) {
                p.add("              " + result.getInt(1) + "                            Available\n");

            } else {
                p.add("              " + result.getInt(1) + "                            Not Available\n");

            }

        }

        String total = "";
        for (String element : p) {
            total += element;
        }

        // Display the room availability information
        JOptionPane.showMessageDialog(null, total);

        // Close the database connections and result set
        result.close();
        st.close();
        con.close();
    }

    public static void makeReservation() throws ReservationException, FileNotFoundException, IOException, SQLException {

        // Create a PrintWriter to write reservation details to a file
        PrintWriter Writer = new PrintWriter(new FileWriter(RESERVATIONS_FILE_NAME, true));
        Writer.println("--------------RESERVATIONS REPORT--------------");

        // Establish a connection to the database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        String name = JOptionPane.showInputDialog("Enter your name: ");
        String roomNumberstring = JOptionPane.showInputDialog("Enter the room number you want to reserve: ");
        int roomNumber = Integer.parseInt(roomNumberstring);

        ResultSet result = st.executeQuery("SELECT * FROM Room");

        while (result.next()) {

            if (roomNumber == result.getInt(1)) {

                if (result.getString(2).equalsIgnoreCase("true")) {
                    String numberOfNightsString = JOptionPane.showInputDialog("Enter the number of nights you want to reserve: ");
                    int numberOfNights = Integer.parseInt(numberOfNightsString);

                    // Insert reservation details into the database
                    PreparedStatement preQueryStat = con.prepareStatement("INSERT INTO Reservation VALUES (?, ?, ?)");

                    preQueryStat.setString(1, name);
                    preQueryStat.setInt(2, roomNumber);
                    preQueryStat.setInt(3, numberOfNights);
                    preQueryStat.executeUpdate();

                    // Update the room availability in the database
                    preQueryStat = con.prepareStatement("UPDATE Room SET Availability = 'false' WHERE ID = ?");
                    preQueryStat.setInt(1, roomNumber);
                    preQueryStat.executeUpdate();

                    // Process payment for the reservation
                    Payment(numberOfNights);
                    JOptionPane.showMessageDialog(null, "Reservation made successfully!");

                    // Write reservation details to the reservations file
                    Writer.println("\nName of the customer is: " + name + "\nRoom number is: " + roomNumber + "\nReservation for " + numberOfNights + " Nights");
                    Writer.flush();

                } else {
                    JOptionPane.showMessageDialog(null, "Room " + roomNumber + " is already reserved!");
                    throw new ReservationException("Room " + roomNumber + " is already reserved!");

                }

            }

            if (!(roomNumber > 100 && roomNumber < 111)) {
                JOptionPane.showMessageDialog(null, "Room " + roomNumber + " not found!");
                throw new ReservationException("Room " + roomNumber + " not found!");
            }
        }
        Writer.close();

    }

    public static void viewReservations() throws SQLException {

        // Establish a connection to the database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);

        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String name = JOptionPane.showInputDialog("Enter your name: ");

        // Retrieve reservations for the given name from the database
        PreparedStatement preQueryStat = con.prepareStatement("SELECT * FROM Reservation WHERE name = ?");
        preQueryStat.setString(1, name);

        ResultSet result = preQueryStat.executeQuery();

        ArrayList<String> p = new ArrayList<String>();
        p.add("Name    Room Number    Number of Days\n");

        String s0 = null;
        while (result.next()) {
            s0 = result.getString(1);
            p.add(s0 + "                        ");
            p.add(result.getInt(2) + "                              ");
            p.add(result.getInt(3) + "\n");
        }

        if (s0 == null) {
            JOptionPane.showMessageDialog(null, "No reservations found!");
            

        } else {

            String total = "";
            for (String element : p) {
                total += element;
            }

            // Display the reservations for the given name
            JOptionPane.showMessageDialog(null, total);

        }

    }

    public static void cancelReservation() throws ReservationException, SQLException, IOException {
        
            PrintWriter Writer = new PrintWriter(new FileWriter(RESERVATIONS_FILE_NAME, true));
        
        // Establish a connection to the database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", host, passWord);
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        String name = JOptionPane.showInputDialog("Enter your name: ");
        String roomNumberString = JOptionPane.showInputDialog("Enter the room number of the reservation you want to cancel: ");
        int roomNumber = Integer.parseInt(roomNumberString);

        ResultSet result = st.executeQuery("SELECT * FROM Reservation WHERE Name = '" + name + "' AND Room_no = " + roomNumber);

        String s0 = null;
        while (result.next()) {
            s0 = result.getString(1);
        }

        if (s0 == null) {
            JOptionPane.showMessageDialog(null, "Reservation not found for room " + roomNumber + "!");
            throw new ReservationException("Reservation not found for room " + roomNumber + "!");

        } else {

            // Delete the reservation from the database
            PreparedStatement preQueryStat = con.prepareStatement("DELETE FROM Reservation WHERE Name = ? AND Room_no = ?");
            preQueryStat.setString(1, name);
            preQueryStat.setInt(2, roomNumber);
            preQueryStat.executeUpdate();
            
            Writer.println("\nThe room number " + roomNumber + " of the customer " + name + " has been canceled.");
            Writer.flush();
            // Update the room availability in the database
            preQueryStat = con.prepareStatement("UPDATE Room SET Availability = 'true' WHERE ID = ?");
            preQueryStat.setInt(1, roomNumber);
            preQueryStat.executeUpdate();

            JOptionPane.showMessageDialog(null, "Reservation cancelled successfully!");

        }
        Writer.close();

    }

    public static void Payment(int nights) {
        int CostOfOneNight = 350;
        double amount = 0.0;
        if (nights > 1) {

            amount = CostOfOneNight * nights;
        } else {

            amount = CostOfOneNight;
        }

        try {

            String payment = JOptionPane.showInputDialog("The amount of payment is (" + amount + ") Ryal: ");
            int pay = Integer.parseInt(payment);

            if (pay != amount) {

                JOptionPane.showMessageDialog(null, "You entered the wrong amount! Please try again.");
                Payment(nights);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number.");
            Payment(nights);
        } catch (InputMismatchException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number.");
            Payment(nights);
        }
    }
}
