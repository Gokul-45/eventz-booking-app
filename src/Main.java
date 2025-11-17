import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    private static EventDAO eventDAO = new EventDAO();
    private static ParticipantDAO participantDAO = new ParticipantDAO();
    private static Scanner sc = new Scanner(System.in);
    private static boolean isAdmin = false;

    public static void main(String[] args) {
        printWelcome();
        loginMenu();
        while (true) {
            printMainMenu();
            int choice = getIntInput("Choose option: ");
            if (isAdmin) {
                switch (choice) {
                    case 1: createEvent(); break;
                    case 2: viewEvents(); break;
                    case 3: updateEvent(); break;
                    case 4: deleteEvent(); break;
                    case 5: manageParticipants(); break;
                    case 6: searchEvents(); break;
                    case 7: exportParticipantsCSV(); break;
                    case 8: printGoodbye(); System.exit(0);
                    default: printError("Invalid option! Please try again.");
                }
            } else {
                switch (choice) {
                    case 1: viewEvents(); break;
                    case 2: bookEvent(); break;
                    case 3: searchEvents(); break;
                    case 4: cancelBooking(); break;
                    case 5: printGoodbye(); System.exit(0);
                    default: printError("Invalid option! Please try again.");
                }
            }
        }
    }

    private static void printWelcome() {
        System.out.println(ConsoleColors.CYAN_BOLD + "\n🎉 Welcome to Eventz - Local Event Booking Console App! 🎉" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "--------------------------------------------------------" + ConsoleColors.RESET);
    }

    private static void loginMenu() {
        System.out.println(ConsoleColors.PURPLE_BOLD + "\nLogin as:" + ConsoleColors.RESET);
        System.out.println("1. Admin");
        System.out.println("2. User");
        int choice = getIntInput("Choose option: ");
        isAdmin = (choice == 1);
    }

    private static void printMainMenu() {
        if (isAdmin) {
            System.out.println(ConsoleColors.PURPLE_BOLD + "\n🌈 Admin Menu 🌈" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.GREEN + "1. Create Event 🆕");
            System.out.println("2. View All Events 👀");
            System.out.println("3. Update Event ✏️");
            System.out.println("4. Delete Event ❌");
            System.out.println("5. Manage Participants 👥");
            System.out.println("6. Search Events 🔍");
            System.out.println("7. Export Participants to CSV 📄");
            System.out.println("8. Exit 🚪" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.PURPLE_BOLD + "\n🌈 User Menu 🌈" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.GREEN + "1. View All Events 👀");
            System.out.println("2. Book Event & Pay 💳");
            System.out.println("3. Search Events 🔍");
            System.out.println("4. Cancel Booking ❌");
            System.out.println("5. Exit 🚪" + ConsoleColors.RESET);
        }
    }

    private static void printGoodbye() {
        System.out.println(ConsoleColors.CYAN_BOLD + "\n🙏 Thank you for using Eventz! Have a great day! 🙌\n" + ConsoleColors.RESET);
    }

    private static void printError(String msg) {
        System.out.println(ConsoleColors.RED_BOLD + "❗ " + msg + ConsoleColors.RESET);
    }

    private static void printSuccess(String msg) {
        System.out.println(ConsoleColors.GREEN_BOLD + "✅ " + msg + ConsoleColors.RESET);
    }

    private static int getIntInput(String prompt) {
        System.out.print(ConsoleColors.YELLOW + prompt + ConsoleColors.RESET);
        while (!sc.hasNextInt()) {
            printError("Please enter a valid number!");
            sc.next();
            System.out.print(ConsoleColors.YELLOW + prompt + ConsoleColors.RESET);
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    private static void createEvent() {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n🆕 Create New Event" + ConsoleColors.RESET);
        Event event = new Event();
        System.out.print("Event Name: ");
        event.setName(sc.nextLine());
        String date;
        while (true) {
            System.out.print("Date (YYYY-MM-DD): ");
            date = sc.nextLine();
            if (isFutureDate(date)) break;
            printError("Please enter a future date!");
        }
        event.setDate(date);
        System.out.print("Location: ");
        event.setLocation(sc.nextLine());
        System.out.print("Description: ");
        event.setDescription(sc.nextLine());
        System.out.print("Ticket Price: ");
        event.setPrice(sc.nextDouble());
        System.out.print("Total Seats: ");
        event.setTotalSeats(sc.nextInt());
        event.setBookedSeats(0);
        sc.nextLine();
        eventDAO.saveEvent(event);
        printSuccess("Event Created!");
    }

    private static boolean isFutureDate(String date) {
        try {
            java.time.LocalDate input = java.time.LocalDate.parse(date);
            return input.isAfter(java.time.LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    private static void viewEvents() {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n👀 All Events" + ConsoleColors.RESET);
        List<Event> events = eventDAO.getAllEvents();
        if (events.isEmpty()) {
            printError("No events found.");
            return;
        }
        for (Event e : events) {
            System.out.println(ConsoleColors.CYAN + "\nID: " + e.getId() + " | Name: " + e.getName() + " | Date: " + e.getDate() +
                    " | Location: " + e.getLocation() + "\nDescription: " + e.getDescription() +
                    "\nTicket Price: ₹" + e.getPrice() + " | Seats: " + (e.getTotalSeats() - e.getBookedSeats()) + "/" + e.getTotalSeats() + ConsoleColors.RESET);
        }
    }

    private static void updateEvent() {
        viewEvents();
        int id = getIntInput("Enter Event ID to update: ");
        Event event = eventDAO.getEventById(id);
        if (event == null) {
            printError("Event not found!");
            return;
        }
        System.out.print("New Name (" + event.getName() + "): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) event.setName(name);
        System.out.print("New Date (" + event.getDate() + "): ");
        String date = sc.nextLine();
        if (!date.isEmpty() && isFutureDate(date)) event.setDate(date);
        System.out.print("New Location (" + event.getLocation() + "): ");
        String location = sc.nextLine();
        if (!location.isEmpty()) event.setLocation(location);
        System.out.print("New Description (" + event.getDescription() + "): ");
        String desc = sc.nextLine();
        if (!desc.isEmpty()) event.setDescription(desc);
        System.out.print("New Ticket Price (" + event.getPrice() + "): ");
        String price = sc.nextLine();
        if (!price.isEmpty()) event.setPrice(Double.parseDouble(price));
        System.out.print("New Total Seats (" + event.getTotalSeats() + "): ");
        String seats = sc.nextLine();
        if (!seats.isEmpty()) event.setTotalSeats(Integer.parseInt(seats));
        eventDAO.saveEvent(event);
        printSuccess("Event Updated!");
    }

    private static void deleteEvent() {
        viewEvents();
        int id = getIntInput("Enter Event ID to delete: ");
        eventDAO.deleteEvent(id);
        printSuccess("Event Deleted!");
    }

// ... (continue in next message)

    private static void manageParticipants() {
        viewEvents();
        int eventId = getIntInput("Enter Event ID to manage participants: ");
        Event event = eventDAO.getEventById(eventId);
        if (event == null) {
            printError("Event not found!");
            return;
        }
        while (true) {
            System.out.println(ConsoleColors.PURPLE_BOLD + "\n👥 Manage Participants for Event: " + event.getName() + ConsoleColors.RESET);
            System.out.println("1. View Participants 👀");
            System.out.println("2. Delete Participant ❌");
            System.out.println("3. Back to Main Menu 🔙");
            int choice = getIntInput("Choose option: ");
            switch (choice) {
                case 1: viewParticipants(eventId); break;
                case 2: deleteParticipant(eventId); break;
                case 3: return;
                default: printError("Invalid option!");
            }
        }
    }

    private static void viewParticipants(int eventId) {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n👀 Participants List" + ConsoleColors.RESET);
        List<Participant> participants = participantDAO.getParticipantsByEvent(eventId);
        if (participants.isEmpty()) {
            printError("No participants found.");
            return;
        }
        for (Participant p : participants) {
            if (!p.isCancelled()) {
                System.out.println(ConsoleColors.CYAN + "ID: " + p.getId() + " | Name: " + p.getName() + " | Email: " + p.getEmail() +
                        " | RSVP: " + (p.isRsvp() ? "✅ Attending" : "❌ Not Attending") + ConsoleColors.RESET);
            }
        }
    }

    private static void deleteParticipant(int eventId) {
        viewParticipants(eventId);
        int pid = getIntInput("Enter Participant ID to delete: ");
        participantDAO.deleteParticipant(pid);
        printSuccess("Participant Deleted!");
    }

    // Book Event (User)
    private static void bookEvent() {
        viewEvents();
        int eventId = getIntInput("Enter Event ID to book: ");
        Event event = eventDAO.getEventById(eventId);
        if (event == null) {
            printError("Event not found!");
            return;
        }
        int available = event.getTotalSeats() - event.getBookedSeats();
        if (available <= 0) {
            printError("Sorry, this event is SOLD OUT!");
            return;
        }
        System.out.println(ConsoleColors.YELLOW_BOLD + "\n🎟️ Booking for: " + event.getName() + ConsoleColors.RESET);
        System.out.println("Available Seats: " + available);
        int tickets;
        while (true) {
            tickets = getIntInput("How many tickets do you want to book? ");
            if (tickets > 0 && tickets <= available) break;
            printError("Please enter a valid number of tickets (1 to " + available + ")");
        }
        System.out.print("Your Name: ");
        String name = sc.nextLine();
        String email;
        while (true) {
            System.out.print("Your Email: ");
            email = sc.nextLine();
            if (isValidEmail(email)) break;
            printError("Invalid email format! Try again.");
        }

        // Payment simulation
        double totalAmount = event.getPrice() * tickets;
        System.out.println(ConsoleColors.PURPLE_BOLD + "\n💳 Payment Section" + ConsoleColors.RESET);
        System.out.println("Ticket Price: ₹" + event.getPrice());
        System.out.println("Total to pay: ₹" + totalAmount);
        System.out.print("Enter Card Number (fake): ");
        sc.nextLine();
        System.out.print("Enter CVV (fake): ");
        sc.nextLine();
        System.out.print("Enter Expiry (MM/YY): ");
        sc.nextLine();

        boolean paymentSuccess = new Random().nextBoolean();
        if (paymentSuccess) {
            printSuccess("Payment Successful! 🎉 You have booked " + tickets + " ticket(s) for the event.");
            for (int i = 0; i < tickets; i++) {
                Participant p = new Participant();
                p.setName(name);
                p.setEmail(email);
                p.setRsvp(true);
                p.setEvent(event);
                participantDAO.saveParticipant(p);
            }
            event.setBookedSeats(event.getBookedSeats() + tickets);
            eventDAO.saveEvent(event);
            System.out.println(ConsoleColors.GREEN_BOLD + "🎫 Booking Confirmed! See you at the event!" + ConsoleColors.RESET);
        } else {
            printError("Payment Failed! 😢 Please try again.");
        }
    }

    // Email validation
    private static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }

    // Search Events
    private static void searchEvents() {
        System.out.print("Enter keyword to search (name/location/date): ");
        String keyword = sc.nextLine().toLowerCase();
        List<Event> events = eventDAO.getAllEvents();
        boolean found = false;
        for (Event e : events) {
            if (e.getName().toLowerCase().contains(keyword) ||
                    e.getLocation().toLowerCase().contains(keyword) ||
                    e.getDate().toLowerCase().contains(keyword)) {
                found = true;
                System.out.println(ConsoleColors.CYAN + "\nID: " + e.getId() + " | Name: " + e.getName() + " | Date: " + e.getDate() +
                        " | Location: " + e.getLocation() + "\nDescription: " + e.getDescription() +
                        "\nTicket Price: ₹" + e.getPrice() + " | Seats: " + (e.getTotalSeats() - e.getBookedSeats()) + "/" + e.getTotalSeats() + ConsoleColors.RESET);
            }
        }
        if (!found) printError("No events found for your search.");
    }

    // Export Participants to CSV (Admin)
    private static void exportParticipantsCSV() {
        viewEvents();
        int eventId = getIntInput("Enter Event ID to export participants: ");
        List<Participant> participants = participantDAO.getParticipantsByEvent(eventId);
        if (participants.isEmpty()) {
            printError("No participants found.");
            return;
        }
        String fileName = "participants_event_" + eventId + ".csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("ID,Name,Email,RSVP,Cancelled\n");
            for (Participant p : participants) {
                writer.write(p.getId() + "," + p.getName() + "," + p.getEmail() + "," +
                        (p.isRsvp() ? "Yes" : "No") + "," + (p.isCancelled() ? "Yes" : "No") + "\n");
            }
            printSuccess("Participants exported to " + fileName);
        } catch (IOException e) {
            printError("Error exporting to CSV: " + e.getMessage());
        }
    }

    // Cancel Booking (User)
    private static void cancelBooking() {
        System.out.print("Enter your email to find your bookings: ");
        String email = sc.nextLine();
        List<Event> events = eventDAO.getAllEvents();
        List<Participant> found = new ArrayList<>();
        for (Event e : events) {
            List<Participant> participants = participantDAO.getParticipantsByEvent(e.getId());
            for (Participant p : participants) {
                if (p.getEmail().equalsIgnoreCase(email) && !p.isCancelled()) {
                    found.add(p);
                }
            }
        }
        if (found.isEmpty()) {
            printError("No active bookings found for this email.");
            return;
        }
        System.out.println("Your Bookings:");
        for (Participant p : found) {
            System.out.println("Booking ID: " + p.getId() + " | Event: " + p.getEvent().getName() + " | Date: " + p.getEvent().getDate());
        }
        int pid = getIntInput("Enter Booking ID to cancel: ");
        for (Participant p : found) {
            if (p.getId() == pid) {
                p.setCancelled(true);
                participantDAO.saveParticipant(p);
                Event event = p.getEvent();
                event.setBookedSeats(event.getBookedSeats() - 1);
                eventDAO.saveEvent(event);
                printSuccess("Booking cancelled!");
                return;
            }
        }
        printError("Invalid Booking ID.");
    }
}