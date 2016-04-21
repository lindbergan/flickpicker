package com.typeof.flickpicker.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.core.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieDatabaseHelper extends DatabaseHelper<Movie> {

    public MovieDatabaseHelper(Context ctx) {
        super(ctx);
    }

    public Movie find(long id) {
        Cursor c = super.find(id, "movies");
        Movie movie = createMovieCursor(c);
        c.close();

        return movie;
    }

    public Movie createMovieCursor(Cursor c) {
        c.moveToFirst();
        String title = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        return new Movie(id, title);
    }

    public long save(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());

        return super.save(movie, "movies", values);
    }

    public void update(Movie movie, ContentValues values) {
        String selection = MovieTable.MovieEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(movie.getId()) };

        super.update(movie, values);
    }

    public long delete(Movie movie) {
        return movie.getId();
    }

    public List<Movie> search(String searchString) {
        List<Movie> results = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM movies WHERE " + MovieTable.MovieEntry.COLUMN_NAME_TITLE + " LIKE ?", new String[]{"%" + searchString + "%"});

        while (c.moveToNext()) {
            long movieId = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
            results.add(createMovieCursor(c));
        }

        c.close();

        return results;
    }
}
