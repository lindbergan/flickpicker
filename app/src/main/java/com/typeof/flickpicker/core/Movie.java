package com.typeof.flickpicker.core;


/**
 * Movie
 *
 * A class that specifies what a Movie database object/core entity is.
 * The class holds information about the features of a movie, and what separates it from others.
 */

public class Movie implements DatabaseObject {

    private long id = 0;
    private String title = "";
    private int year = 0;
    private String description = "";
    private String genre = "";
    private String poster;
    private int numberOfVotes = 0;
    private double communityRating = 0;

    /**
     * Constructs a movie object that initially sets numberOfVotes and communityRating to zero.
     * All other properties of the movie object are set by the parameters
     * @param id the id that will be assigned to the movie object
     * @param title the title that will be assigned to the movie object
     * @param description the description that will be assigned to the movie object
     * @param year the year that will be assigned to the movie object
     * @param genre the genre that will be assigned to the movie object
     */

    public Movie(long id, String title, String description, int year, String genre) {
        this.id = id;
        this.year = year;
        this.title = title;
        this.description = description;
        this.genre = genre;
    }

    /**
     * Constructs a movie object that initially sets id, numberOfVotes and communityRating to zero.
     * All other properties of the movie object are set by the parameters
     * @param title the title that will be assigned to the movie object
     * @param description the description that will be assigned to the movie object
     * @param year the year that will be assigned to the movie object
     * @param genre the genre that will be assigned to the movie object
     * @param poster the poster that will be assigned to the movie object
     */

    public Movie(String title, String description, int year, String genre, String poster) {
        this.title = title;
        this.year = year;
        this.description = description;
        this.genre = genre;
        this.poster = poster;
    }

    /**
     * Constructs a movie object that initially sets description and genre to an empty string,
     * poster to null and numberOfVotes and communityRating to zero.
     * All other properties of the movie object are set by the parameters
     * @param id the id that will be assigned to the movie object
     * @param title the title that will be assigned to the movie object
     * @param year the year that will be assigned to the movie object
     */

    public Movie(long id, String title,int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    /**
     * Constructs a movie object that initially sets description and genre to an empty string,
     * poster to null and id, numberOfVotes and communityRating to zero.
     * All other properties of the movie object are set by the parameters
     * @param title the title that will be assigned to the movie object
     * @param year the year that will be assigned to the movie object
     */

    public Movie(String title, int year) {
        this.title = title;
        this.year = year;
    }


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

    /**
     * Two movies are considered equal if their ids are the same
     * @param otherObject the object that is compared to this
     * @return returns true if this and otherObject has the same ids
     */

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

        //else they are of the same class
        Movie other = (Movie) otherObject;
        return other.getId() == this.getId();
    }

    /**
     * A Movie object's hashcode is determined by its id
     * @return returns the hashcode of the movie
     */

    @Override
    public int hashCode() {
        return (int) id;
    }
}

