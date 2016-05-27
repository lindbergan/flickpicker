package com.typeof.flickpicker.database.sql.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.database.sql.CoreEntityFactory;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.database.sql.tables.UserTable;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public class SQLUserDAO extends SQLDAO implements UserDAO {
    private final SQLiteDatabase db;
    public SQLUserDAO(Context ctx) {
        super(ctx);
        SQLiteDatabaseHelper dbhelper = SQLiteDatabaseHelper.getInstance(ctx);
        db = dbhelper.getWritableDatabase();
    }

    /**
     * Method for saving a user record in the database
     *
     * @param user  The User object to be saved in the database
     * @return      the userId given to the saved User object
     *
     */
    public long saveUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(UserTable.UserEntry.COLUMN_NAME_USERNAME, user.getUsername());
        cv.put(UserTable.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
        cv.put(UserTable.UserEntry.COLUMN_NAME_SCORE, user.getScore());
        return this.save(user, "users", cv);
    }

    /**
     * Method for fetching a user from the database
     *
     * @param   userId the requested user's ID
     * @return  the user corresponding to specified userID
     * @throws  DatabaseRecordNotFoundException
     */
    public User getUserById(long userId) {
        try {
            Cursor c = this.find(userId, UserTable.UserEntry.TABLE_NAME);
            c.moveToFirst();
            User user = CoreEntityFactory.createUserFromCursor(c);
            c.close();
            return user;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }

    /**
     * Method for searching for a user via a search string
     *
     * @param   column specifies what column to search in
     * @param   searchString the search string
     * @return  a list of users with usernames matching the searchstring
     */
    public List<User> searchUser(String column, String searchString) {
        List<User> results = new ArrayList<>();
        Cursor c = super.search(UserTable.UserEntry.TABLE_NAME, column, searchString);

        while (c.moveToNext()) {
            User u = CoreEntityFactory.createUserFromCursor(c);
            if (!u.getUsername().equalsIgnoreCase(App.getCurrentUser().getUsername())) {
                results.add(CoreEntityFactory.createUserFromCursor(c));
            }
        }

        c.close();
        return results;
    }

    /**
     * Method for deleting a user record from the database
     *
     * @param user  the user to delete from the database
     * @return int  that represents the number of records deleted
     */
    @Override
    public int deleteUser(User user) {

        return super.delete(user, UserTable.UserEntry.TABLE_NAME);
    }

    /**
     * Returns all users from the database
     *
     * @return List of users
     */
    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        String query = "SELECT * FROM " + UserTable.UserEntry.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        do {
            User newUser = CoreEntityFactory.createUserFromCursor(c);
            allUsers.add(newUser);
        } while (c.moveToNext());

        return allUsers;
    }
}
