package services;

import models.*;
import exceptions.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {
    private Map<String, Booking> bookings;
    private SeatService seatService;

    public BookingService(SeatService seatService) {
        this.bookings = new ConcurrentHashMap<>();
        this.seatService = seatService;
    }

    public Booking createBooking(Customer customer, Show show, List<Seat> selectedSeats) 
            throws SeatAlreadyBookedException {
        // Lock seats
        for (Seat seat : selectedSeats) {
            seatService.lockSeat(seat);
        }

        // Create booking
        Booking booking = new Booking(
            UUID.randomUUID().toString(),
            customer,
            selectedSeats,
            show
        );

        bookings.put(booking.getBookingId(), booking);
        customer.addBooking(booking);
        return booking;
    }

    public boolean confirmBooking(Booking booking, Payment payment) 
            throws SeatAlreadyBookedException {
        // Process payment
        if (!payment.processPayment()) {
            // Release locked seats
            for (Seat seat : booking.getSeats()) {
                seatService.unlockSeat(seat);
            }
            return false;
        }

        // Book seats
        for (Seat seat : booking.getSeats()) {
            seat.bookSeat();
        }

        booking.confirmBooking();
        booking.setPayment(payment);
        return true;
    }

    public void cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            booking.cancelBooking();
        }
    }

    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }
}
