package com.typeof.flickpicker.core;

public class User implements DatabaseObject{

    private long id;
    private String userName;
    private int score;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.score = 0;
    }

    /**
     * Getters
     */

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Setters
     */

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
