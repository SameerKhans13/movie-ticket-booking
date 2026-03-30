package models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Theater {
    private String theaterId;
    private String name;
    private String address;
    private String city;
    private Map<String, Auditorium> auditoriums;

    public Theater(String theaterId, String name, String address, String city) {
        this.theaterId = theaterId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.auditoriums = new ConcurrentHashMap<>();
    }

    public String getAddress() {
        return address;
    }

    public void addAuditorium(Auditorium auditorium) {
        auditoriums.put(auditorium.getAuditoriumId(), auditorium);
    }

    public Auditorium getAuditorium(String auditoriumId) {
        return auditoriums.get(auditoriumId);
    }

    public String getTheaterId() {
        return theaterId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}
