package hotel_room_reservation;

import java.io.Serializable;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int roomNumber;
    private int numberOfNights;

    public Reservation(String name, int roomNumber, int numberOfNights) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.numberOfNights = numberOfNights;
    }

    public String getName() {
        return name;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }
}