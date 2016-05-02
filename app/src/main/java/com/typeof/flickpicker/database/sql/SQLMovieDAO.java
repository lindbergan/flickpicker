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
            c.moveToFirst();
            Movie movie = createMovieFromCursor(c);
            c.close();
            return movie;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }

    public Movie createMovieFromCursor(Cursor c) {
        String title = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        int year = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_YEAR));
        double rating = c.getDouble(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING));
        Movie m = new Movie(id, title, year);
        m.setCommunityRating(rating);
        return m;
    }

    public long saveMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_YEAR, movie.getYear());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION, movie.getDescription());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_GENRE, movie.getGenre());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_VOTES, movie.getNumberOfVotes());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING, movie.getCommunityRating());

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
        c.moveToFirst();

        try {
            do {
                results.add(createMovieFromCursor(c));
            } while(c.moveToNext());
        } finally {
            c.close();
        }
        return results;
    }
}
