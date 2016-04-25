package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface FriendDAO {

    long addFriend(long userId1, long userId2);
    List<User> getFriendsFromUserId(long id);
    long removeFriend(long userId1, long userId2);

}
