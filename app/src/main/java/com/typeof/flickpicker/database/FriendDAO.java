package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface FriendDAO {

    public boolean addFriend(long userId1, long userId2);
    public List<User> getFriendsFromUserId(long id);
    public boolean removeFriend();

}
