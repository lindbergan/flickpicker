package com.typeof.flickpicker;

public class User extends DatabaseObject {

    private final String TABLENAME = "USERS";
    private int id;
    private String userName;
    private int score;
    private String password;

    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.score = 0;
    }

    //---------GETTERS-------------

    public String getTableName() {
        return TABLENAME;
    }

    public int getId() {
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
