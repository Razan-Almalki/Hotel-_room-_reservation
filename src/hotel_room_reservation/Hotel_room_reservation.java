package hotel_room_reservation;

import java.io.*;
import java.net.*;
import java.util.*;

public class Hotel_room_reservation {

    public static final String ROOMS_FILE_NAME = "rooms.txt";
    public static final String RESERVATIONS_FILE_NAME = "reservations.txt";
    public static List<Room> rooms;
    public static List<Reservation> reservations;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
        try (ServerSocket server = new ServerSocket(8800)) {
            System.out.println("Server waiting Connection...");
            while (true) {
                Socket s = server.accept();
                Runnable r = new Server_Thread(s);
                Thread t1 = new Thread(r);
                t1.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayMenu() {
        
        int choice = 0;
        
        do {
            System.out.println("1. View Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        viewRooms();
                        break;
                    case 2:
                        makeReservation();
                        break;
                    case 3:
                        cancelReservation();
                        break;
                    case 4:
                        saveDataToFile();
                        break;
                    default:
                        while(choice >4 || choice <1){
                        System.out.println("Invalid input!");
                        displayMenu();
                        }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Try again.");
                scanner.nextLine();
                
            } catch (ReservationException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 4 && choice < 4);
    }
    public static List<Room> createRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            rooms.add(new Room(i));
        }
        return rooms;
    }
    
    public static void viewRooms() {
        System.out.println("Room Number\tAvailability");
        for (Room room : rooms) {
            System.out.println(room.getRoomNumber() + "\t\t" + (room.isAvailable() ? "Available" : "Not Available"));
        }
    }

    public static void makeReservation() throws ReservationException {
        System.out.print("Enter your name: ");
        scanner.nextLine();
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
        Payment(numberOfNights);
        System.out.println("Reservation made successfully!");
    }

    public static void cancelReservation() throws ReservationException {
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

    public static Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public static Reservation findReservation(int roomNumber) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoomNumber() == roomNumber) {
                return reservation;
            }
        }
        return null;
    }

    public static void saveDataToFile() throws IOException {
        try (ObjectOutputStream roomsOut = new ObjectOutputStream(new FileOutputStream(ROOMS_FILE_NAME));
                ObjectOutputStream reservationsOut = new ObjectOutputStream(new FileOutputStream(RESERVATIONS_FILE_NAME))) {
            roomsOut.writeObject(rooms);
            reservationsOut.writeObject(reservations);
            System.out.println("Data saved to file successfully!");
        }
    }

    public static List<Room> loadRoomsFromFile() throws IOException, ClassNotFoundException {
      
      
            return createRooms();
        
       
    }

    public static List<Reservation> loadReservationsFromFile() throws IOException, ClassNotFoundException {
       
            return new ArrayList<>();
        
    }

 

    public static void Payment(int nights) {
        int CostOfOneNight = 350;
        double amount = 0.0;
        if (nights > 1) {
            amount = CostOfOneNight * nights;
        } else {
            amount = CostOfOneNight;
        }
        System.out.print("The amount of payment is (" + amount + ") Ryal: ");
        try {
            Scanner scanner = new Scanner(System.in);
            int pay = scanner.nextInt();
            if (pay <= 0 || pay != amount) {
                System.out.println("You entered the wrong amount! Please try again.");
                Payment(nights);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            Payment(nights);
        }
    }
}
