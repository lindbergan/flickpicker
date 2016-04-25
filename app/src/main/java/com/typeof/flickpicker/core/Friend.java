package com.typeof.flickpicker.core;

public class Friend {

    private final String TABLENAME = "FRIENDS";

    private int id;
    private long userIdOne;
    private long getUserIdTwo;

    public Friend(int id, long userIdOne, long getUserIdTwo) {
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

    public long getUserIdOne() {
        return userIdOne;
    }

    public long getGetUserIdTwo() {
        return getUserIdTwo;
    }
}//Friend
