package hotel_room_reservation;

/*The class has three private fields:
name (customer's name), roomNumber (room number for the reservation)
and numberOfNights (number of nights for the reservation) */

class Reservation {

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
