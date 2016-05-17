package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface FriendDAO {

    long addFriend(Friend f);
    List<User> getFriendsFromUserId(long id);
    long removeFriend(long userId1, long userId2);
    List<Rating> getFriendsLatestActivities(long userId);
    boolean isFriend(long user2Id);
}
