package com.typeof.flickpicker.database.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FlickPicker.db";
    public static final int DATABASE_VERSION = 1;
    public static SQLiteDatabaseHelper instance;

    private SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteDatabaseHelper getInstance(Context ctx) {
        if (instance == null) {
            instance = new SQLiteDatabaseHelper(ctx.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        // Begin with dropping the old tables
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLDropTableQuery());
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLCreateTableQuery());
        db.execSQL(FriendTable.FriendEntry.getSQLCreateTableQuery());
        db.execSQL(UserTable.UserEntry.getSQLCreateTableQuery());
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLDropTableQuery());
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());
        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
