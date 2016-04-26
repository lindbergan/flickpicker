package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.typeof.flickpicker.User;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.UserDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public class SQLUserDAO extends SQLDAO implements UserDAO {

    public SQLUserDAO(Context ctx) {
        super(ctx);
        //SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(ctx);
        //db = mDbHelper.getWritableDatabase();
    }


    public User createUserFromCursor(Cursor c){
        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_ID));
        String username = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_USERNAME));
        String password = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_PASSWORD));
        return new User(id, username, password);
    }

    @Override
    public long saveUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(UserTable.UserEntry.COLUMN_NAME_ID, user.getId());
        cv.put(UserTable.UserEntry.COLUMN_NAME_USERNAME, user.getUsername());
        cv.put(UserTable.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
        cv.put(UserTable.UserEntry.COLUMN_NAME_SCORE, user.getScore());
        return this.save(user, user.getTableName(), cv);
    }

    @Override
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

    @Override
    public int deleteUser(User user) {
        return super.delete(user, UserTable.UserEntry.TABLE_NAME);
    }

/*
    private void update(User user, ContentValues values) {
    }

    public List<User> search(String searchString) {
    }

*/
}
