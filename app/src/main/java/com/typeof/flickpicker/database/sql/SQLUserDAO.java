package com.typeof.flickpicker.database.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.typeof.flickpicker.User;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.UserDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public class SQLUserDAO extends SQLDAO implements UserDAO {

    private SQLiteDatabase db;

    public SQLUserDAO(Context ctx) {
        super(ctx);
        //SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(ctx);
        //db = mDbHelper.getWritableDatabase();
    }

//    public User find(long id) {
//        Cursor c = db.rawQuery("SELECT * FROM users WHERE id = ? LIMIT 1", new String[]{String.valueOf(id)});
//        if (c.getCount() < 1) {
//            return null;
//        }
//
//        User user = createUserFromDatabaseData(c);
//        c.close();
//
//        return user;
//    }

    public User createUserFromDatabaseData(Cursor c){
        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_ID));
        String username = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_USERNAME));
        String password = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_PASSWORD));
         return new User(id, username, password);
    }

    @Override
    public long saveUser(User user) {
        return 0;
    }

    @Override
    public User getUserById(long userId) {
        return null;
    }

    @Override
    public int deleteUser(User user) {
        return 0;
    }

/*
    public long save(User user) {
    }

    private void update(User user, ContentValues values) {
    }

    public long delete(User user) {
    }

    public List<User> search(String searchString) {
    }

*/
}
