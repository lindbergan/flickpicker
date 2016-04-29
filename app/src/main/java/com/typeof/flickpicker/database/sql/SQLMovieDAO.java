package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class SQLMovieDAO extends SQLDAO implements MovieDAO {

    public SQLMovieDAO(Context ctx) {
        super(ctx);
    }

    public Movie findMovie(long id) {
        try {
            Cursor c = super.find(id, "movies");
            Movie movie = createMovieFromCursor(c);
            c.close();
            return movie;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }

    public Movie createMovieFromCursor(Cursor c) {
        c.moveToFirst();
        String title = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        int year = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_YEAR));
        return new Movie(id, title, year);
    }

    public long saveMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        return super.save(movie, "movies", values);
    }

    public void updateMovie(Movie movie, ContentValues values) {
        super.update(movie, values, "movies");
    }

    public int deleteMovie(Movie movie) {
        return super.delete(movie, "movies");
    }

    public List<Movie> searchMovieBy(String column, String searchString) {
        List<Movie> results = new ArrayList<>();
        Cursor c = super.search("movies", column, searchString);

        while (c.moveToNext()) {
            results.add(createMovieFromCursor(c));
        }

        c.close();

        return results;
    }
}
