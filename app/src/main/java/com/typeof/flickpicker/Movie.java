package com.typeof.flickpicker;
public class Movie {
    private long id = 0;
    private String title;

    public Movie(String title) {
        this.title = title;
    }

    public Movie(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
