package com.typeof.flickpicker.core;

/**
 * Friend
 *
 * A class that specifies what a Friend database object/core entity is. The object describes a
 * one-way relation where one user can be a friend to another, but not necessarily the other way around.
 * The class holds information about which user follow which, how many movies they both seen as well
 * as how good of a match their taste in movies are.
 */

public class Friend implements DatabaseObject {

    private long id;
    private long userIdOne;
    private long getUserIdTwo;
    private double mismatch = 0;
    private int nmbrOfMoviesBothSeen = 0;


    /**
     * Constructs a friend object that initially sets nmbrOfMoviesBothSeen and mismatch to 0.
     * @param userIdOne the id of the first user (follower)
     * @param getUserIdTwo the id of the second user (followed)
     */
    public Friend(long userIdOne, long getUserIdTwo) {
        this.userIdOne = userIdOne;
        this.getUserIdTwo = getUserIdTwo;
    }

    public long getId() {
        return id;
    }

    public long getUserIdOne() {
        return userIdOne;
    }

    public long getGetUserIdTwo() {
        return getUserIdTwo;
    }

    public double getMismatch(){
        return mismatch;
    }

    public int getNmbrOfMoviesBothSeen() {
        return nmbrOfMoviesBothSeen;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void setMismatch(double mismatch){
        this.mismatch = mismatch;
    }

    public void setNmbrOfMoviesBothSeen(int nmbrOfMoviesBothSeen) {
        this.nmbrOfMoviesBothSeen = nmbrOfMoviesBothSeen;
    }
}
