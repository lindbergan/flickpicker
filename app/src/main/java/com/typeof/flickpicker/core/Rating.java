package com.typeof.flickpicker.core;

/**
 * Rating
 *
 * A class that specifies what a Rating database object/core entity is.
 * The class holds information about the value of the rating as well as which user and movie it
 * refers to.
 */

public class Rating implements DatabaseObject {

    private long id = 0;
    private double rating;
    private long movieId;
    private long userId;

    /**
     * Constructs a rating object that initially sets id to zero.
     * All other properties of the rating object are set by the parameters
     * @param rating the rating value
     * @param movieId the id of the movie that the rating refers to
     * @param userId the id of the user that the rating refers to
     */

    public Rating(double rating, long movieId, long userId) {
        this.rating = rating;
        this.movieId = movieId;
        this.userId = userId;
    }

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

    /**
     * A method that updates the rating value
     * @param newRating the new rating value
     */

    public void updateRating(double newRating){
        this.rating = newRating;
    }
}
