package com.typeof.flickpicker.core;
public class Movie implements DatabaseObject {

    private long id = 0;
    private String title = "";
    private int year = 0;
    private String description = "";
    private String genre = "";
    private String poster;
    private int numberOfVotes = 0;
    private double communityRating;

    public Movie(long id, String title, String description, int year, String genre) {
        this.id = id;
        this.year = year;
        this.title = title;
        this.description = description;
        this.genre = genre;
    }

    public Movie(String title, String description, int year, String genre, String poster) {
        this.title = title;
        this.year = year;
        this.description = description;
        this.genre = genre;
        this.poster = poster;
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

    public String getPoster() {
        return poster;
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

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object otherObject) {

        if(otherObject == this){
            return true;
        }
        if (otherObject == null){
            return false;
        }
        if(otherObject.getClass() != this.getClass()) {
            return false;
        }

        //else they are of teh same class - typecasting is ok
        Movie other = (Movie) otherObject;
            return other.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return (int) id;
    }
}//Movie

