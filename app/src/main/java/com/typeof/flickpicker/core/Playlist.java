package com.typeof.flickpicker.core;
import com.typeof.flickpicker.DatabaseObject;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private final String TABLENAME = "PLAYLISTS";
    private int id;
    private String title;
    private List<Movie> playlist;
    private int userId;

    public Playlist(int id, String title, int userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        playlist = new ArrayList<Movie>();
    }

    public String getTableName() {
        return TABLENAME;
    }

    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void add(Movie movie){
        playlist.add(movie);
    }

    public void remove(Movie movie){
        playlist.remove(movie);
    }
}//Playlist
