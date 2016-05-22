package com.typeof.flickpicker.core;

import java.util.ArrayList;
import java.util.List;

public class Playlist implements DatabaseObject {

    private long id = 0;
    private String title;
    private List<Number> movieIds;
    private long userId;

    public Playlist(String title, long userId) {
        this.title = title;
        this.userId = userId;
        movieIds = new ArrayList<>();
    }

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

    public void add(long movieId) {
        this.movieIds.add(movieId);
    }

    public void remove(long movieId) {
        getMovieIds().remove(Long.valueOf(movieId));
    }

}//Playlist
