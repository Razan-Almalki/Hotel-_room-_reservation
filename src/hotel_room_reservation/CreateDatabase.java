package hotel_room_reservation;

import java.sql.*;

public class CreateDatabase {

    public static void main(String[] args) {

        try {

            String ConnectionURL = "jdbc:mysql://localhost:3306";

            // Establish a connection to the MySQL server
            Connection con = DriverManager.getConnection(ConnectionURL, Hotel_room_reservation.host, Hotel_room_reservation.passWord);

            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // Create a new database named "HotelDatabase"
            st.executeUpdate("CREATE DATABASE " + "HotelDatabase");

            System.out.println("database is create");

            con.close();

        } catch (SQLException s) {
            s.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
