package com.typeof.flickpicker.core;
public class Movie implements DatabaseObject {

    private long id = 0;
    private String title;
    private int year;
    private String description;
    private String genre;
    private int numberOfVotes;
    private double communityRating;

    public Movie(long id, String title, String description, int year, String genre) {
        this.id = id;
        this.year = year;
        this.title = title;
        this.description = description;
        this.genre = genre;
    }

    public Movie(String title, String description, int year, String genre) {
        this.title = title;
        this.year = year;
        this.description = description;
        this.genre = genre;
    }

    public Movie(long id, String title,int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public Movie(String title, int year) {
        this.title = title;
        this.year = year;
    }

    //------------------GETTERS-----------------------

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public double getCommunityRating() {
        return communityRating;
    }

    public int getYear() {
        return year;
    }


//----------------------SETTERS-----------------------

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public void setCommunityRating(double communityRating) {
        this.communityRating = communityRating;
    }
}//Movie

