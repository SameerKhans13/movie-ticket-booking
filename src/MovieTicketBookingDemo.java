import models.*;
import enums.*;
import implementations.*;
import services.*;
import java.util.*;

public class MovieTicketBookingDemo {
    public static void main(String[] args) {
        try {
            System.out.println("=== Movie Ticket Booking System ===\n");

            // Initialize services
            SeatService seatService = new SeatService();
            BookingService bookingService = new BookingService(seatService);
            ShowService showService = new ShowService();

            // Create theater
            Theater theater = new Theater("TH1", "PVR Cinemas", "123 Main St", "Delhi");
            System.out.println("✓ Theater created: " + theater.getName());

            // Create auditorium
            Auditorium auditorium = new Auditorium("AUD1", "Screen 1", "IMAX 2D");
            theater.addAuditorium(auditorium);
            System.out.println("✓ Auditorium created: " + auditorium.getName() + "\n");

            // Create movie
            Movie movie = new Movie("MOV1", "Inception", 148, "Sci-Fi");
            System.out.println("✓ Movie added: " + movie.getTitle() + " (" + movie.getDuration() + " min)\n");

            // Create show
            long startTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // Tomorrow
            long endTime = startTime + (2 * 60 * 60 * 1000); // 2 hours duration
            Show show = new Show("SHO1", movie, auditorium, startTime, endTime, 500.0);
            auditorium.addShow(show);
            System.out.println("✓ Show created with base price: ₹" + show.getBasePrice() + "\n");

            // Create seats
            System.out.println("Creating seats:");
            for (char row = 'A'; row <= 'C'; row++) {
                for (int seatNum = 1; seatNum <= 5; seatNum++) {
                    SeatType type = (row == 'A') ? SeatType.SILVER : 
                                   (row == 'B') ? SeatType.GOLD : SeatType.PLATINUM;
                    Seat seat = new Seat(row + "" + seatNum, type, row, seatNum, show);
                    auditorium.addSeat(seat);
                }
            }
            System.out.println("✓ Total seats created: 15 (5 Silver, 5 Gold, 5 Platinum)\n");

            // Create customer
            Customer customer1 = new Customer("CUST1", "John Doe", "john@email.com", "9876543211");
            System.out.println("✓ Customer registered: " + customer1.getName() + "\n");

            // Display pricing
            System.out.println("--- Seat Pricing (Base Price: ₹500) ---");
            System.out.println("Silver: ₹" + (500 * SeatType.SILVER.getMultiplier()));
            System.out.println("Gold: ₹" + (500 * SeatType.GOLD.getMultiplier()));
            System.out.println("Platinum: ₹" + (500 * SeatType.PLATINUM.getMultiplier()) + "\n");

            // Get available seats
            List<Seat> availableSeats = auditorium.getAvailableSeats(show);
            System.out.println("Available seats: " + availableSeats.size() + "\n");

            // Select seats for booking
            List<Seat> selectedSeats = new ArrayList<>();
            selectedSeats.add(availableSeats.get(0));  // A1 - Silver
            selectedSeats.add(availableSeats.get(5));  // B1 - Gold

            System.out.println("--- Creating Booking ---");
            Booking booking = bookingService.createBooking(customer1, show, selectedSeats);
            System.out.println("✓ Booking created: " + booking.getBookingId());
            System.out.println("  Seats: " + selectedSeats.get(0).getSeatId() + 
                              ", " + selectedSeats.get(1).getSeatId());
            System.out.println("  Total Price: ₹" + booking.getTotalPrice() + "\n");

            // Apply pricing strategy (Weekend surge)
            System.out.println("--- Applying Weekend Surge Pricing ---");
            showService.updateShowPricing(show, new WeekendSurge());
            double newPrice = booking.getTotalPrice() * 1.2;
            System.out.println("✓ New total with 20% surge: ₹" + newPrice + "\n");

            // Process payment
            System.out.println("--- Processing Payment ---");
            Payment payment = new Payment("PAY1", booking.getTotalPrice());
            payment.setPaymentMethod(new CreditCardPayment("1234567890123456", "123"));
            
            boolean paymentSuccess = bookingService.confirmBooking(booking, payment);
            if (paymentSuccess) {
                System.out.println("✓ Payment successful!");
                System.out.println("✓ Booking confirmed: " + booking.getBookingId());
                System.out.println("  Status: " + booking.getStatus() + "\n");
            } else {
                System.out.println("✗ Payment failed\n");
            }

            // Display booking summary
            System.out.println("--- Booking Summary ---");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Customer: " + booking.getCustomer().getName());
            System.out.println("Movie: " + booking.getShow().getMovie().getTitle());
            System.out.println("Theater: " + theater.getName());
            System.out.println("Auditorium: " + auditorium.getName());
            System.out.println("Seats: " + selectedSeats.get(0).getSeatId() + 
                              " (" + selectedSeats.get(0).getSeatType() + "), " +
                              selectedSeats.get(1).getSeatId() + 
                              " (" + selectedSeats.get(1).getSeatType() + ")");
            System.out.println("Total Amount: ₹" + booking.getTotalPrice());
            System.out.println("Status: " + booking.getStatus() + "\n");

            // Try to book same seat (concurrency test)
            System.out.println("--- Concurrency Test ---");
            Customer customer2 = new Customer("CUST2", "Jane Smith", "jane@email.com", "9876543212");
            List<Seat> conflictSeats = new ArrayList<>();
            conflictSeats.add(availableSeats.get(0));  // Try to book A1 again
            try {
                Booking booking2 = bookingService.createBooking(customer2, show, conflictSeats);
                System.out.println("✗ Should have prevented double booking!");
            } catch (Exception e) {
                System.out.println("✓ Double-booking prevented!");
            }

            System.out.println("\n=== Demo Completed Successfully ===");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
