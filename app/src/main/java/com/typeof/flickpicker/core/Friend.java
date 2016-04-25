package com.typeof.flickpicker.core;

public class Friend implements DatabaseObject {

    private long id;
    private long userIdOne;
    private long getUserIdTwo;

    public Friend(long id, long userIdOne, long getUserIdTwo) {
        this.id = id;
        this.userIdOne = userIdOne;
        this.getUserIdTwo = getUserIdTwo;
    }

    //-----------GETTERS-------------

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {

    }

    public long getUserIdOne() {
        return userIdOne;
    }

    public long getGetUserIdTwo() {
        return getUserIdTwo;
    }
}//Friend
