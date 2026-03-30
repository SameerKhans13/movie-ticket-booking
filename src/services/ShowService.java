package services;

import models.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShowService {
    private Map<String, Show> shows;

    public ShowService() {
        this.shows = new ConcurrentHashMap<>();
    }

    public Show createShow(String showId, Movie movie, Auditorium auditorium,
                          long startTime, long endTime, double basePrice) throws Exception {
        auditorium.addShow(new Show(showId, movie, auditorium, startTime, endTime, basePrice));
        Show show = new Show(showId, movie, auditorium, startTime, endTime, basePrice);
        shows.put(showId, show);
        return show;
    }

    public List<Show> getShowsByMovie(Movie movie) {
        List<Show> result = new ArrayList<>();
        for (Show show : shows.values()) {
            if (show.getMovie().getMovieId().equals(movie.getMovieId())) {
                result.add(show);
            }
        }
        return result;
    }

    public Show getShow(String showId) {
        return shows.get(showId);
    }

    public void updateShowPricing(Show show, interfaces.PricingStrategy strategy) {
        show.setPricingStrategy(strategy);
        System.out.println("Show pricing updated to: " + strategy.getRuleName());
    }
}
