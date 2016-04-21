package com.typeof.flickpicker.core;

public class Friend {

    private final String TABLENAME = "FRIENDS";

    private int id;
    private int userIdOne;
    private int getUserIdTwo;

    public Friend(int id, int userIdOne, int getUserIdTwo) {
        this.id = id;
        this.userIdOne = userIdOne;
        this.getUserIdTwo = getUserIdTwo;
    }

    //-----------GETTERS-------------

    public String getTableName() {
        return TABLENAME;
    }

    public int getId() {
        return id;
    }

    public int getUserIdOne() {
        return userIdOne;
    }

    public int getGetUserIdTwo() {
        return getUserIdTwo;
    }
}//Friend
