package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.User;
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
    private SQLiteDatabase db;

    public SQLFriendDAO(Context ctx) {
        super(ctx);
        sql = new SQLUserDAO(ctx);
        SQLiteDatabaseHelper dbhelper = new SQLiteDatabaseHelper(ctx);
        db = dbhelper.getWritableDatabase();
    }

    /**
     * Adds the friends values to user1id and user2id columns
     * Saves the friend to the database
     * @param f
     * @return
     */

    @Override
    public long addFriend(Friend f) {
        ContentValues values = new ContentValues();
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER1ID, f.getUserIdOne());
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER2ID, f.getGetUserIdTwo());
        return super.save(f, FriendTable.FriendEntry.TABLE_NAME, values);
    }

    /**
     * A super SQL-question made by: SebbeTheMan
     * If cursor count is 0 --> returns a empty list
     * Otherwise adds all users and returns the list
     * @param id
     * @return
     */

    @Override
    public List<User> getFriendsFromUserId(long id) {
        List<User> userFriends = new ArrayList<>();

        String query = "SELECT * FROM " + FriendTable.FriendEntry.TABLE_NAME +
        " INNER JOIN " + UserTable.UserEntry.TABLE_NAME + " " +
                "ON " + FriendTable.FriendEntry.TABLE_NAME + "." + FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " = " + UserTable.UserEntry.TABLE_NAME+ "." +UserTable.UserEntry.COLUMN_NAME_ID + " " +
        " WHERE "  + FriendTable.FriendEntry.COLUMN_NAME_USER1ID + " = ? ";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(id)});

        c.moveToFirst();

        if (c.getCount() < 1) return userFriends;

        try {
            do {
                userFriends.add(sql.createUserFromCursor(c));
            } while (c.moveToNext());
        } finally {
            c.close();
        }

        return userFriends;
    }

    /**
     * Removes the friend values from the database
     * @param userId1
     * @param userId2
     * @return
     */

    @Override
    public long removeFriend(long userId1, long userId2) {

        return db.delete(FriendTable.FriendEntry.TABLE_NAME,
                FriendTable.FriendEntry.COLUMN_NAME_USER1ID + " = ? AND " + FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " = ?",
                new String[]{String.valueOf(userId1), String.valueOf(userId2)});
    }
}
