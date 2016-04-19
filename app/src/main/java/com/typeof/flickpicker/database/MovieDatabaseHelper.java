package com.typeof.flickpicker.database;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.Movie;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieDatabaseHelper {
    private SQLiteDatabase db;

    public MovieDatabaseHelper(Context ctx) {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(ctx);
        db = mDbHelper.getWritableDatabase();
    }

    public Movie find(int id) {
        String[] projection = {
                MovieTable.MovieEntry.COLUMN_NAME_ID,
                MovieTable.MovieEntry.COLUMN_NAME_TITLE
        };

        String selection = MovieTable.MovieEntry.COLUMN_NAME_ID;
        String selectionArgs[] = {String.valueOf(id)};

        Cursor c = db.rawQuery("SELECT * FROM movies WHERE id = ?", new String[]{String.valueOf(id)});

        c.moveToLast();
        String movieTitle = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));

        c.close();

        return new Movie(id, movieTitle);

    }

    public boolean save(Movie movie) {
        return true;
    }

    public int delete(Movie movie) {
        return movie.getId();
    }
}
