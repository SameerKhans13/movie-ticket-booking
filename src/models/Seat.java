package models;

import enums.SeatType;
import exceptions.SeatAlreadyBookedException;

public class Seat {
    private String seatId;
    private SeatType seatType;
    private char row;
    private int number;
    private Show show;
    private boolean isAvailable;

    public Seat(String seatId, SeatType seatType, char row, int number, Show show) {
        this.seatId = seatId;
        this.seatType = seatType;
        this.row = row;
        this.number = number;
        this.show = show;
        this.isAvailable = true;
    }

    public synchronized void bookSeat() throws SeatAlreadyBookedException {
        if (!isAvailable) {
            throw new SeatAlreadyBookedException("Seat " + seatId + " is already booked");
        }
        this.isAvailable = false;
    }

    public synchronized void releaseSeat() {
        this.isAvailable = true;
    }

    public String getSeatId() {
        return seatId;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public char getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public Show getShow() {
        return show;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
