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


                        //-----Movie-----

        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());
                        //---RATING---

        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());


        //
        // Create a new map of values, where column names are the keys
        ContentValues firstRatingValues = new ContentValues();
        firstRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_ID, 5);
        firstRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, 3.0);
        firstRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, 3);
        firstRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, 2);

        long firstNewRowIdRating = db.insert(
                RatingTable.RatingEntry.TABLE_NAME,
                RatingTable.RatingEntry.COLUMN_NAME_NULLABLE,
                firstRatingValues);

        ContentValues SecondRatingValues = new ContentValues();
        SecondRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_ID, 4);
        SecondRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, 3.0);
        SecondRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, 2);
        SecondRatingValues.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, 1);

        long secondNewRowIdRating = db.insert(
                RatingTable.RatingEntry.TABLE_NAME,
                RatingTable.RatingEntry.COLUMN_NAME_NULLABLE,
                SecondRatingValues);

    }


    public void clearDatabase() {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());

    }

}
