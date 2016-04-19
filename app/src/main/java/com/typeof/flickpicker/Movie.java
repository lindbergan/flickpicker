package com.typeof.flickpicker;
public class Movie {

    private long id;
    private String title;
    private String description;
    private String genre;
    private int numberOfVotes;
    private double communityRating;

    public Movie(long id, String title, String description, String genre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
    }

    public Movie(String title, String description, String genre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
    }

    public Movie(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Movie(String title) {
        this.title = title;
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

