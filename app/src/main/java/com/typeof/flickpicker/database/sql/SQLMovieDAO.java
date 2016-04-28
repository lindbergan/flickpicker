package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.User;
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

    private SQLiteDatabase db;

    public SQLMovieDAO(Context ctx) {
        super(ctx);
        SQLiteDatabaseHelper dbhelper = new SQLiteDatabaseHelper(ctx);
        db = dbhelper.getWritableDatabase();
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

    @Override
    public int numOfFriendsHasSeenMovie(long movieId, long userId) {

        List<User> friends = getFriendsFromUserId(userId);

        String query = "SELECT * FROM " + FriendTable.FriendEntry.TABLE_NAME + " INNER JOIN " + RatingTable.RatingEntry.TABLE_NAME + " ON " +
                FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " = " + RatingTable.RatingEntry.COLUMN_NAME_USERID
                + " WHERE " + FriendTable.FriendEntry.COLUMN_NAME_USER1ID + " = ? AND WHERE "
                + RatingTable.RatingEntry.COLUMN_NAME_MOVIEID + " = ?";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(movieId)});

        c.moveToFirst();

        while (c.moveToNext()) {
            friends.add(createUserFromCursor(c));
        }

        c.close();

        return friends.size();
    }

    public List<User> getFriendsFromUserId(long id) {
        List<User> userFriends = new ArrayList<>();

        String query = "SELECT * FROM " + FriendTable.FriendEntry.TABLE_NAME +
                " INNER JOIN " + UserTable.UserEntry.TABLE_NAME + " " +
                "ON " + FriendTable.FriendEntry.TABLE_NAME + "." + FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " = " + UserTable.UserEntry.TABLE_NAME+ "." +UserTable.UserEntry.COLUMN_NAME_ID + " " +
                " WHERE "  + FriendTable.FriendEntry.COLUMN_NAME_USER1ID + " = ? ";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(id)});

        c.moveToFirst();

        if (c.getCount() < 1) return userFriends;

        try {
            do {
                userFriends.add(createUserFromCursor(c));
            } while (c.moveToNext());
        } finally {
            c.close();
        }

        return userFriends;
    }

    public User createUserFromCursor(Cursor c){
        long id = c.getLong(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_ID));
        String username = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_USERNAME));
        String password = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_PASSWORD));
        int score = c.getInt(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_SCORE));

        User u = new User(username, password);
        u.setId(id);
        u.setScore(score);

        return u;
    }

}
