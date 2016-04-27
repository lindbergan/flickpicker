package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.FriendDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */

public class SQLFriendDAO extends SQLDAO implements FriendDAO {

    private SQLUserDAO sql;

    public SQLFriendDAO(Context ctx) {
        super(ctx);
        sql = new SQLUserDAO(ctx);
    }

    @Override
    public long addFriend(Friend f) {

        ContentValues values = new ContentValues();
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER1ID, f.getUserIdOne());
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER2ID, f.getGetUserIdTwo());
        return super.save(f, FriendTable.FriendEntry.TABLE_NAME, values);

    }

    @Override
    public List<User> getFriendsFromUserId(long id) {
        List<User> userFriends = new ArrayList<>();

        Cursor c = super.search(FriendTable.FriendEntry.TABLE_NAME, FriendTable.FriendEntry.COLUMN_NAME_USER1ID,
                Long.toString(id));

        while (c.moveToNext()) {
            userFriends.add(sql.createUserFromCursor(c));
        }
        c.close();

        return userFriends;
    }

    @Override
    public long removeFriend(long userId1, long userId2) {

        Friend friend = new Friend(userId1, userId2);

        return super.delete(friend, FriendTable.FriendEntry.TABLE_NAME);

    }
}
