package hotel_room_reservation;

import java.sql.*;
import java.util.ArrayList;

public class Create_table {

    public static ArrayList<Room> rooms = new ArrayList<Room>();

    public static void main(String[] args) {

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDatabase", Hotel_room_reservation.host, Hotel_room_reservation.passWord);

            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String table = "CREATE TABLE Room ("
                    + "Id INT, "
                    + "Availability VARCHAR(255))";
            st.executeUpdate(table);

            System.out.println("Table is created successfully");

            createRooms();
            for (int i = 0; i < rooms.size(); i++) {

                PreparedStatement preQueryStat = con.prepareStatement("INSERT INTO Room VALUES (?, ?)");

                preQueryStat.setInt(1, rooms.get(i).getRoomNumber());
                preQueryStat.setString(2, "" + rooms.get(i).isAvailable());

                preQueryStat.executeUpdate();
            }
            System.out.println("Record is added");

            String table2 = "CREATE TABLE Reservation ("
                    + "Name VARCHAR(255), "
                    + "Room_no INT, "
                    + "Reseved_day INT)";
            st.executeUpdate(table2);

            st.close();
            con.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void createRooms() throws SQLException {

        for (int i = 101; i <= 110; i++) {
            rooms.add(new Room(i));
        }
    }
}
