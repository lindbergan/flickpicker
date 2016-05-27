package com.typeof.flickpicker.database.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.database.sql.tables.FriendTable;
import com.typeof.flickpicker.database.sql.tables.MovieTable;
import com.typeof.flickpicker.database.sql.tables.PlaylistTable;
import com.typeof.flickpicker.database.sql.tables.RatingTable;
import com.typeof.flickpicker.database.sql.tables.SQLTable;
import com.typeof.flickpicker.database.sql.tables.UserTable;

/**
 * SQLDatabase
 * Creates, Deletes and Seeds the database
 */
public class SQLDatabase implements Database {

    private final SQLiteDatabase db;

    public SQLDatabase(Context ctx) {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(ctx);
        this.db = mDbHelper.getWritableDatabase();
    }

    @Override
    public void setUpTables() {

        //-----Movie-----
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());

        //-----User-----
        db.execSQL(UserTable.UserEntry.getSQLCreateTableQuery());

        //-----Rating-----
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());

        //-----Playlist-----
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLCreateTableQuery());

        //-----Friend-----
        db.execSQL(FriendTable.FriendEntry.getSQLCreateTableQuery());
    }

    @Override
    public void dropTables() {
        //-----Movie-----
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());

        //-----User------
        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());

        //-----Rating-----
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());

        //-----Playlist----
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLDropTableQuery());

        //-----Friend-----
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());
    }

    /**
     * Seed method
     */
    public void seedDatabase() {

        UserDAO userDAO = App.getUserDAO();

        User u1 = new User("Adrian", "admin");
        User u2 = new User("Sebastian", "admin");
        User u3 = new User("Jonathan", "admin");
        User u4 = new User("Jonatan", "admin");
        User u5 = new User("Terminator", "H4x0r");
        User u6 = new User("xXx_1337_xXx", "H4x0r");
        User u7 = new User("xXx_Pr0_Snajpur_xXx", "H4x0r");
        User u8 = new User("1337_L33t_1337", "H4x0r");
        User u9 = new User("dr4g0nsl4j3r_killer_swe", "H4x0r");
        User u10 = new User("pro_killer1337^^:P_swe", "H4x0r");
        User u11 = new User("[ElitE_PlAyEr-1337}_SWE", "H4x0r");

        userDAO.saveUser(u1);
        userDAO.saveUser(u2);
        userDAO.saveUser(u3);
        userDAO.saveUser(u4);
        userDAO.saveUser(u5);
        userDAO.saveUser(u6);
        userDAO.saveUser(u7);
        userDAO.saveUser(u8);
        userDAO.saveUser(u9);
        userDAO.saveUser(u10);
        userDAO.saveUser(u11);

    }

    /**
     * Combines SQL Queries from the tables
     * @return  boolean whether the movie table has been created
     */
    public boolean hasBeenCreated() {
        SQLTable movieTable = new MovieTable();

        Cursor c;
        String query = movieTable.hasBeenCreatedSQLQuery();

        try {
            c = db.rawQuery(query, null);
        } catch (SQLiteException e ) {
            e.printStackTrace();
            return false;
        }

        c.moveToFirst();

        boolean created = false;
        if (c.getCount() > 0) {
            created = true;
        }

        c.close();

        return created;
    }

}
