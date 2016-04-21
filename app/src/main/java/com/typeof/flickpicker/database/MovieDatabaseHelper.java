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
        try {
            Cursor c = super.find(id, "movies");
            Movie movie = createMovieCursor(c);
            c.close();
            return movie;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
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
        super.update(movie, values);
    }

    public long delete(Movie movie) {
        super.delete(movie, "movies");
        return movie.getId();
    }

    public List<Movie> search(String searchString) {
        List<Movie> results = new ArrayList<>();
        Cursor c = super.search("movies", "title", searchString);

        while (c.moveToNext()) {
            long movieId = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
            results.add(createMovieCursor(c));
        }

        c.close();

        return results;
    }
}
