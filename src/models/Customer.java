package models;

import java.util.*;

public class Customer extends User {
    private List<Booking> bookings;

    public Customer(String userId, String name, String email, String phone) {
        super(userId, name, email, phone);
        this.bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }
}
