package com.typeof.flickpicker.database.sql;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.typeof.flickpicker.core.Friend;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FlickPicker.db";
    public static final int DATABASE_VERSION = 1;

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());
        db.execSQL(FriendTable.FriendEntry.getSQLCreateTableQuery());
        db.execSQL(UserTable.UserEntry.getSQLCreateTableQuery());
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());
        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
