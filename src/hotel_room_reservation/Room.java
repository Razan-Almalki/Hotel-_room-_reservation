package hotel_room_reservation;

/*This class represents a hotel room. It has two private fields: roomNumber (representing the room number) 
and isAvailable (representing the availability status of the room). 
The constructor initializes the room number and sets the availability status to true by default.
The class provides getter methods to access the room number and availability status,
and a setter method to update the availability status.*/
class Room {

    private int roomNumber;
    private boolean isAvailable;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;

    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
