package com.typeof.flickpicker;

public class User {

    private final String TABLENAME = "USERS";
    private long id;
    private String userName;
    private int score;
    private String password;

    public User(long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
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
