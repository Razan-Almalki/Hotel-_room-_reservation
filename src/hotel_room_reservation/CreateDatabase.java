package hotel_room_reservation;

import java.io.*;
import java.sql.*;

public class CreateDatabase {

    public static void main(String[] args) {
        
        try {

            String ConnectionURL = "jdbc:mysql://localhost:3306";

            Connection con = DriverManager.getConnection(ConnectionURL, "root", "Qazwsx12!");

            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            st.executeUpdate("CREATE DATABASE " + "HotelDatabase");

            System.out.println("database is create");
            
            con.close();
            
        } catch (SQLException s) {
            
            System.out.println("SQL statement is not executed!");
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
}