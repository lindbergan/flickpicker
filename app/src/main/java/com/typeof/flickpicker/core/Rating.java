package com.typeof.flickpicker.core;

public class Rating {

    private final String TABLENAME = "RATINGS";
    private int id;
    private double rating;
    private int movieId;
    private int userId;

    public Rating(int id, double rating, int movieId, int userId) {
        this.id = id;
        this.rating = rating;
        this.movieId = movieId;
        this.userId = userId;
    }

    //----------GETTERS--------------

    public String getTableName() {
        return TABLENAME;
    }

    public int getId() {
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
