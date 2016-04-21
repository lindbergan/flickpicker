package com.typeof.flickpicker.core;

public class Rating implements CoreEntity {

    //private final String TABLENAME = "RATINGS";
    private long id;
    private double rating;
    private int movieId;
    private int userId;

    public Rating(long id, double rating, int movieId, int userId) {
        this.id = id;
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

    public int getMovieId() {
        return movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void updateRating(double newRating){
        this.rating = newRating;
    }
}//Rating
