package com.typeof.flickpicker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.database.sql.MovieTable;
import com.typeof.flickpicker.database.sql.RatingTable;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class DatabaseSeed {

    private SQLiteDatabase db;

    public DatabaseSeed(Context ctx) {

        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(ctx);
        this.db = mDbHelper.getWritableDatabase();
    }

    public void seedDatabase() {

        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_ID, 5);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, "Shawshank Redemption");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION, "Derp");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_GENRE, "Drama");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_VOTES, 10);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING, 5.5);

        long newRowId;
        newRowId = db.insert(
                MovieTable.MovieEntry.TABLE_NAME,
                MovieTable.MovieEntry.COLUMN_NAME_NULLABLE,
                values);

        //---RATING---

        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());

        //
        // Create a new map of values, where column names are the keys
        ContentValues ratingValues = new ContentValues();
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_ID, 5);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, 4.0);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, 3);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, 2);

        long newRowIdRating = db.insert(
                RatingTable.RatingEntry.TABLE_NAME,
                RatingTable.RatingEntry.COLUMN_NAME_NULLABLE,
                ratingValues);

    }

    public void clearDatabase() {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());

    }

}
