package hotel_room_reservation;
import java.io.*;
import java.util.*;
public class Hotel_room_reservation {
   private static final String ROOMS_FILE_NAME = "rooms.ser";
    private static final String RESERVATIONS_FILE_NAME = "reservations.ser";
    private static List<Room> rooms;
    private static List<Reservation> reservations;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            rooms = loadRoomsFromFile();
            reservations = loadReservationsFromFile();
            displayMenu();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayMenu() {
        int choice = 0;
        do {
            System.out.println("1. View Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. View Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline character
                switch (choice) {
                    case 1:
                        viewRooms();
                        break;
                    case 2:
                        makeReservation();
                        break;
                    case 3:
                        viewReservations();
                        break;
                    case 4:
                        cancelReservation();
                        break;
                    case 5:
                        saveDataToFile();
                        break;
                    default:
                        System.out.println("Invalid choice! Try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Try again.");
                scanner.nextLine(); // consume the invalid input
            } catch (ReservationException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 5);
    }

    private static void viewRooms() {
        System.out.println("Room Number\tAvailability");
        for (Room room : rooms) {
            System.out.println(room.getRoomNumber() + "\t\t" + (room.isAvailable() ? "Available" : "Not Available"));
        }
    }

    private static void makeReservation() throws ReservationException {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the room number you want to reserve: ");
        int roomNumber = scanner.nextInt();
        Room room = findRoom(roomNumber);
    if (room == null) {
        throw new ReservationException("Room " + roomNumber + " not found!");
    }
    if (!room.isAvailable()) {
        throw new ReservationException("Room " + roomNumber + " is already reserved!");
    }
    System.out.print("Enter the number of nights you want to reserve: ");
    int numberOfNights = scanner.nextInt();
    Reservation reservation = new Reservation(name, roomNumber, numberOfNights);
    reservations.add(reservation);
    room.setAvailable(false);
    System.out.println("Reservation made successfully!");
}

private static void viewReservations() {
    if (reservations.isEmpty()) {
        System.out.println("No reservations found!");
    } else {
        System.out.println("Name\tRoom Number\tNumber of Nights");
        for (Reservation reservation : reservations) {
            System.out.println(reservation.getName() + "\t" + reservation.getRoomNumber() + "\t\t" + reservation.getNumberOfNights());
        }
    }
}

private static void cancelReservation() throws ReservationException {
    System.out.print("Enter the room number of the reservation you want to cancel: ");
    int roomNumber = scanner.nextInt();
    Reservation reservation = findReservation(roomNumber);
    if (reservation == null) {
        throw new ReservationException("Reservation not found for room " + roomNumber + "!");
    }
    Room room = findRoom(roomNumber);
    if (room == null) {
        throw new ReservationException("Room " + roomNumber + " not found!");
    }
    reservations.remove(reservation);
    room.setAvailable(true);
    System.out.println("Reservation cancelled successfully!");
}

private static Room findRoom(int roomNumber) {
    for (Room room : rooms) {
        if (room.getRoomNumber() == roomNumber) {
            return room;
        }
    }
    return null;
}

private static Reservation findReservation(int roomNumber) {
    for (Reservation reservation : reservations) {
        if (reservation.getRoomNumber() == roomNumber) {
            return reservation;
        }
    }
    return null;
}

private static void saveDataToFile() throws IOException {
    try (ObjectOutputStream roomsOut = new ObjectOutputStream(new FileOutputStream(ROOMS_FILE_NAME));
         ObjectOutputStream reservationsOut = new ObjectOutputStream(new FileOutputStream(RESERVATIONS_FILE_NAME))) {
        roomsOut.writeObject(rooms);
        reservationsOut.writeObject(reservations);
        System.out.println("Data saved to file successfully!");
    }
}

private static List<Room> loadRoomsFromFile() throws IOException, ClassNotFoundException {
    File file = new File(ROOMS_FILE_NAME);
    if (!file.exists()) {
        return createRooms();
    }
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        return (List<Room>) in.readObject();
    }
}

private static List<Reservation> loadReservationsFromFile() throws IOException, ClassNotFoundException {
    File file = new File(RESERVATIONS_FILE_NAME);
    if (!file.exists()) {
        return new ArrayList<>();
    }
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        return (List<Reservation>) in.readObject();
    }
}

private static List<Room> createRooms() {
    List<Room> rooms = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
        rooms.add(new Room(i));
    }
    return rooms;
}}