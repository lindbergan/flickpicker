package com.typeof.flickpicker.core;

public class Friend implements DatabaseObject {

    private long id;
    private long userIdOne;
    private long getUserIdTwo;
    private double disMatch = 0;
    private int nmbrOfMoviesBothSeen = 0;

    public Friend(long userIdOne, long getUserIdTwo) {
        this.userIdOne = userIdOne;
        this.getUserIdTwo = getUserIdTwo;
    }

    /**
     * Getters
     */

    public long getId() {
        return id;
    }

    public long getUserIdOne() {
        return userIdOne;
    }

    public long getGetUserIdTwo() {
        return getUserIdTwo;
    }

    public double getDisMatch(){

        return disMatch;
    }
    public int getNmbrOfMoviesBothSeen() {
        return nmbrOfMoviesBothSeen;
    }

    /**
     * Setters
     */

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void setDisMatch(double match){
        this.disMatch = match;
    }

    public void setNmbrOfMoviesBothSeen(int nmbrOfMoviesBothSeen) {
        this.nmbrOfMoviesBothSeen = nmbrOfMoviesBothSeen;
    }
}
