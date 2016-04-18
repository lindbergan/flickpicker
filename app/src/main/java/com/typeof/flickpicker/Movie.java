package com.typeof.flickpicker;

public class Movie extends DatabaseObject {

    private final String TABLENAME = "MOVIES";
    private int id;
    private String title;
    private String description;
    private String genre;
    private int numberOfVotes;
    private double communityRating;

    public Movie(int id, String title, String description, String genre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
    }

    //------------------GETTERS-----------------------

    public String getTableName() {
        return TABLENAME;
    }

    public int getId() {
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

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public void setCommunityRating(double communityRating) {
        this.communityRating = communityRating;
    }
}//Movie
