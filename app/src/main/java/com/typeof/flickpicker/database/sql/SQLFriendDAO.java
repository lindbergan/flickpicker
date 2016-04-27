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

    public SQLFriendDAO(Context ctx) {
        super(ctx);
    }

    @Override
    public long addFriend(long userId1, long userId2) {

        ContentValues values = new ContentValues();
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER1ID, userId1);
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER2ID, userId2);
        Friend friend = new Friend(userId1, userId2);
        return super.save(friend, FriendTable.FriendEntry.TABLE_NAME, values);

    }

    public User createUserFromCursor(Cursor c) {
        c.moveToFirst();

        String userName = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_USERNAME));
        String password = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_PASSWORD));

        User user = new User(userName, password);
        c.close();
        return user;


    }

    @Override
    public List<User> getFriendsFromUserId(long id) {
        List<User> friends = new ArrayList<>();

        try {

            Cursor c = super.find(id, FriendTable.FriendEntry.TABLE_NAME);

            while (c.moveToNext()) {
                friends.add(createUserFromCursor(c));
            }
            c.close();

        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }

        return friends;
    }

    @Override
    public long removeFriend(long userId1, long userId2) {

        Friend friend = new Friend(userId1, userId2);

        return super.delete(friend, FriendTable.FriendEntry.TABLE_NAME);

    }
}
