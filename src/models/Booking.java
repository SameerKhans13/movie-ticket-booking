package models;

import enums.BookingStatus;
import java.util.*;

public class Booking {
    private String bookingId;
    private Customer customer;
    private List<Seat> seats;
    private Show show;
    private long bookingTime;
    private BookingStatus status;
    private double totalPrice;
    private Payment payment;

    public Booking(String bookingId, Customer customer, List<Seat> seats, Show show) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.seats = new ArrayList<>(seats);
        this.show = show;
        this.bookingTime = System.currentTimeMillis();
        this.status = BookingStatus.PENDING;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        double total = 0;
        for (Seat seat : seats) {
            total += show.getTicketPrice(seat.getSeatType());
        }
        return total;
    }

    public void confirmBooking() {
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancelBooking() {
        this.status = BookingStatus.CANCELLED;
        for (Seat seat : seats) {
            seat.releaseSeat();
        }
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Seat> getSeats() {
        return new ArrayList<>(seats);
    }

    public Show getShow() {
        return show;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public long getBookingTime() {
        return bookingTime;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
