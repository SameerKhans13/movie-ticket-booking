package models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Auditorium {
    private String auditoriumId;
    private String name;
    private String screenType;
    private Map<String, Seat> seats;
    private List<Show> shows;

    public Auditorium(String auditoriumId, String name, String screenType) {
        this.auditoriumId = auditoriumId;
        this.name = name;
        this.screenType = screenType;
        this.seats = new ConcurrentHashMap<>();
        this.shows = Collections.synchronizedList(new ArrayList<>());
    }

    public void addSeat(Seat seat) {
        seats.put(seat.getSeatId(), seat);
    }

    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }

    public void addShow(Show show) throws Exception {
        // Check for overlapping shows
        for (Show existingShow : shows) {
            if (showsOverlap(show, existingShow)) {
                throw new Exception("Show times overlap in this auditorium");
            }
        }
        shows.add(show);
    }

    private boolean showsOverlap(Show show1, Show show2) {
        return show1.getStartTime() < show2.getEndTime() && 
               show1.getEndTime() > show2.getStartTime();
    }

    public List<Seat> getAvailableSeats(Show show) {
        List<Seat> available = new ArrayList<>();
        for (Seat seat : seats.values()) {
            if (seat.getShow().getShowId().equals(show.getShowId()) && seat.isAvailable()) {
                available.add(seat);
            }
        }
        return available;
    }

    public String getAuditoriumId() {
        return auditoriumId;
    }

    public String getName() {
        return name;
    }

    public String getScreenType() {
        return screenType;
    }
}
