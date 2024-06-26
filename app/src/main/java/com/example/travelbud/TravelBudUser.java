package com.example.travelbud;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravelBudUser implements Serializable {
    private String username;
    private String email;
    private List<Trip> trips = new ArrayList<>();
    private List<TravelBudUser> friends = new ArrayList<>();
    private String key;
    private String altKey;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public TravelBudUser(String username, String email, List<Trip> trips, List<TravelBudUser> friends) {
        this.username = username;
        this.trips = trips;
        this.friends = friends;
        this.email = email;
        this.altKey = null;
    }

    public TravelBudUser() {

    }

    public String getAltKey() {
        return altKey;
    }

    public void setAltKey(String altKey) {
        this.altKey = altKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public List<Trip> getTrips() {
        return trips;
    }

    @Exclude
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<TravelBudUser> getFriends() {
        return friends;
    }

    public void setFriends(List<TravelBudUser> friends) {
        this.friends = friends;
    }


}
