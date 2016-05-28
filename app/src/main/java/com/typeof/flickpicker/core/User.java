package com.typeof.flickpicker.core;


/**
 * User
 *
 * A class that specifies what a User database object/core entity is.
 * The class holds information about the name of the user, the user's password as well as score.
 */

public class User implements DatabaseObject {

    private long id = 0;
    private final String username;
    private int score;
    private String password;

    /**
     * Constructs a user object that initially sets id to zero and score to null.
     * All other properties of the user object are set by the parameters
     * @param username the name of the user
     * @param password the user's password
     */

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void updateScore() {
        score = score + 10;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
