package com.typeof.flickpicker.core;

public class User {

    private final String TABLENAME = "USERS";
    private long id;
    private String username;
    private int score;
    private String password;

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.score = 0;
    }

    //---------GETTERS-------------

    public String getTableName() {
        return TABLENAME;
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

    //---------SETTERS-------------

    public void setScore(int score) {
        this.score = score;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}//User
