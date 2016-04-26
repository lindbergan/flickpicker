package com.typeof.flickpicker.core;

public class User implements DatabaseObject {

    private final String TABLENAME = "USERS";
    private long id = 0;
    private String username;
    private int score = 0;
    private String password;

    public User(long id, String username, String password, int score) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.score = score;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public void setId(long id) { this.id = id; }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}//User
