package services;

import models.Seat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SeatService {
    private Map<String, Long> seatLocks; // seatId -> lock expiry time
    private static final long LOCK_TIMEOUT = 5 * 60 * 1000; // 5 minutes

    public SeatService() {
        this.seatLocks = new ConcurrentHashMap<>();
    }

    public synchronized void lockSeat(Seat seat) {
        long expiryTime = System.currentTimeMillis() + LOCK_TIMEOUT;
        seatLocks.put(seat.getSeatId(), expiryTime);
        System.out.println("Seat " + seat.getSeatId() + " locked for 5 minutes");
    }

    public synchronized void unlockSeat(Seat seat) {
        seatLocks.remove(seat.getSeatId());
        System.out.println("Seat " + seat.getSeatId() + " unlocked");
    }

    public boolean isSeatLocked(Seat seat) {
        Long expiryTime = seatLocks.get(seat.getSeatId());
        if (expiryTime == null) {
            return false;
        }
        if (System.currentTimeMillis() > expiryTime) {
            seatLocks.remove(seat.getSeatId());
            return false;
        }
        return true;
    }
}
