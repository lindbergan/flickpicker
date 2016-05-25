package com.typeof.flickpicker.database.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite Database Helper
 * Creates and fetches the local SQLite Database placed in the file system.
 * Used as a singleton.
 */
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
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {

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
