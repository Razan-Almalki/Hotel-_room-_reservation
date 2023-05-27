package hotel_room_reservation;

/*The ReservationException class is used to handle exceptions specific to hotel room reservations in your application.
1-if the required room or reservation is not found .
2-if the room is already reserved!
 */
class ReservationException extends Exception {

    public ReservationException() {
        super();
    }

    public ReservationException(String message) {
        super(message);
    }
}
