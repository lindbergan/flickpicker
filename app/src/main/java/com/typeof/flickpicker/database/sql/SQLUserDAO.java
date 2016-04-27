package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.typeof.flickpicker.User;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.UserDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public class SQLUserDAO extends SQLDAO implements UserDAO {

    public SQLUserDAO(Context ctx) {
        super(ctx);
    }

    /**
     * Method for creating User object from specific record in User database
     *
     * @param c Cursor
     * @return new User created from cursor
     */
    public User createUserFromCursor(Cursor c){
        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_ID));
        String username = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_USERNAME));
        String password = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_PASSWORD));
        int score = c.getInt(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_SCORE));

        User u = new User(username, password);
        u.setId(id);
        u.setScore(score);

        return u;
    }

    /**
     * Method for saving a user record in the database
     *
     * @param user The User object to be saved in the database
     * @return the userId given to the saved User object
     *
     */
    public long saveUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(UserTable.UserEntry.COLUMN_NAME_USERNAME, user.getUsername());
        cv.put(UserTable.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
        cv.put(UserTable.UserEntry.COLUMN_NAME_SCORE, user.getScore());
        return this.save(user, user.getTableName(), cv);
    }

    /**
     * Method for fetching a user from the database
     *
     * @param userId the requested user's ID
     * @return the user corresponding to specified userID
     * @throws DatabaseRecordNotFoundException
     */
    public User getUserById(long userId) {
        try {
            Cursor c = this.find(userId, UserTable.UserEntry.TABLE_NAME);
            User user = this.createUserFromCursor(c);
            c.close();
            return user;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }

    /**
     * Method for searching for a user via a search string
     *
     * @param column specifies what column to search in
     * @param searchString the search string
     * @return a list of users with usernames matching the searchstring
     */
    public List<User> searchUser(String column, String searchString) {
        List<User> results = new ArrayList<>();
        Cursor c = super.search(UserTable.UserEntry.TABLE_NAME, column, searchString);

        while (c.moveToNext()) {
            results.add(createUserFromCursor(c));
        }

        c.close();
        return results;
    }

    /**
     * Method for deleting a user record from the database
     *
     * @param user the user to delete from the database
     * @return int that represents the number of records deleted
     */
    @Override
    public int deleteUser(User user) {

        return super.delete(user, UserTable.UserEntry.TABLE_NAME);
    }
}
