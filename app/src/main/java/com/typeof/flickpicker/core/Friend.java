package com.typeof.flickpicker.core;

public class Friend implements DatabaseObject {

    private long id;
    private long userIdOne;
    private long getUserIdTwo;
    private double match = 0;

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

    public double getMatch(){

        return match;
    }

    /**
     * Setters
     */

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void setMatch(double match){
        this.match = match;
    }
}
