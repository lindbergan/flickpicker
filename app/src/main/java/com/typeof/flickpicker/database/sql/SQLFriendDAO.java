package com.typeof.flickpicker.database.sql;

import android.content.Context;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.FriendDAO;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public class SQLFriendDAO extends SQLDAO implements FriendDAO {

    public SQLFriendDAO(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean addFriend(long userId1, long userId2) {
        return false;
    }

    @Override
    public List<User> getFriendsFromUserId(long id) {
        return null;
    }

    @Override
    public boolean removeFriend(long userId1, long userId2) {
        return false;
    }
}
