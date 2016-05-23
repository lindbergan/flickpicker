package com.typeof.flickpicker.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Playlist
 *
 * A class that specifies what a Playlist database object/core entity is.
 * The class holds information about the user the playlist belongs to, it's title and what movies
 * it refers to.
 */

public class Playlist implements DatabaseObject {

    private long id = 0;
    private String title;
    private List<Number> movieIds;
    private long userId;

    /**
     * Constructs a playlist object that initially sets id to zero and movieIds to null.
     * All other properties of the playlist object are set by the parameters
     * @param title the title that will be assigned to the playlist object
     * @param userId the id of the user that will be assigned to the userId variable
     */

    public Playlist(String title, long userId) {
        this.title = title;
        this.userId = userId;
        movieIds = new ArrayList<>();
    }

    /**
     * Constructs a playlist object that initially sets id to zero and movieIds to null.
     * All other properties of the playlist object are set by the parameters
     * @param title the title that will be assigned to the playlist object
     * @param userId the id of the user that will be assigned to the userId variable
     * @param movieIds the list of movieids that will be assigned to the movieIds variable
     */

    public Playlist(String title, long userId, List<Number> movieIds) {
        this.title = title;
        this.userId = userId;
        this.movieIds = movieIds;
    }

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public List<Number> getMovieIds() {
        return this.movieIds;
    }

    public String getTitle(){
        return title;
    }

    /**
     * a method for adding elements to the list of movieIds
     * @param movieId the movieId to be added to the list
     */

    public void add(long movieId) {
        this.movieIds.add(movieId);
    }

    /**
     * a method for removing elements in the list of movieIds
     * @param movieId the movieId to be removed from the list
     */

    public void remove(long movieId) {
        movieIds.remove(movieIds.indexOf(movieId));
    }
}
