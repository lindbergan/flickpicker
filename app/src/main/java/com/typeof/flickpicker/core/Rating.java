package com.typeof.flickpicker.core;

public class Rating implements DatabaseObject {

    private long id = 0;
    private double rating;
    private long movieId;
    private long userId;

    public Rating(double rating, long movieId, long userId) {
        this.rating = rating;
        this.movieId = movieId;
        this.userId = userId;
    }

    //----------GETTERS--------------

    public void setId(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public long getMovieId() {
        return movieId;
    }

    public long getUserId() {
        return userId;
    }

    public void updateRating(double newRating){
        this.rating = newRating;
    }

}//Rating
