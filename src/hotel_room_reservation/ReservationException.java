package hotel_room_reservation;

class ReservationException extends Exception {

    public ReservationException(String message) {
        super(message);
    }
}
/*There is no check to prevent the user from inputting a room number that does not exist when making a reservation or canceling a reservation. The system should check for this and throw a ReservationException if the input is invalid.

There is no check to prevent the user from inputting a non-integer value when making or canceling a reservation. The system should check for this and throw an InputMismatchException if the input is not an integer.

There is no check to prevent the user from inputting a non-numeric value when saving data to the CSV files. The system should check for this and throw a NumberFormatException if the input is not numeric.*/
 //There is no check to prevent the user from inputting a negative number of nights when making a reservation. The system should check for this and throw a ReservationException if the input is negative.