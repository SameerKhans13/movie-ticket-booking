package models;

import interfaces.PricingStrategy;

public class Show {
    private String showId;
    private Movie movie;
    private Auditorium auditorium;
    private long startTime;  // Unix timestamp
    private long endTime;    // Unix timestamp
    private double basePrice;
    private PricingStrategy pricingStrategy;

    public Show(String showId, Movie movie, Auditorium auditorium, 
                long startTime, long endTime, double basePrice) {
        this.showId = showId;
        this.movie = movie;
        this.auditorium = auditorium;
        this.startTime = startTime;
        this.endTime = endTime;
        this.basePrice = basePrice;
    }

    public double getTicketPrice(enums.SeatType seatType) {
        double seatTypePrice = basePrice * seatType.getMultiplier();
        if (pricingStrategy != null) {
            return pricingStrategy.getPrice(seatTypePrice);
        }
        return seatTypePrice;
    }

    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }

    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
