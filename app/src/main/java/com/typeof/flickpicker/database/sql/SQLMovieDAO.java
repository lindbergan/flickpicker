package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.utils.ExecutionTimeLogger;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Iterator;
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
        SQLiteDatabaseHelper dbhelper = SQLiteDatabaseHelper.getInstance(ctx);
        db = dbhelper.getWritableDatabase();
    }

    /**
     * Finds a movie with help of movieId
     * Calls on createMovieFromCursor then returns a created movie
     * @param id - id of sought movie
     * @return Movie instance
     * @throws DatabaseRecordNotFoundException
     */
    public Movie findMovie(long id) {
        try {
            Cursor c = super.find(id, "movies");
            c.moveToFirst();
            Movie movie = CoreEntityFactory.createMovieFromCursor(c);
            c.close();
            return movie;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }


    /**
     * Creates a map (ContentValues)
     * Puts information in the movie columns
     * Saves the movie in the movies table
     * @param movie - Movie instance to save
     * @return id of created database record
     */
    public long saveMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_YEAR, movie.getYear());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION, movie.getDescription());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_GENRE, movie.getGenre());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_POSTER, movie.getPoster());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_VOTES, movie.getNumberOfVotes());
        values.put(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING, movie.getCommunityRating());

        return super.save(movie, "movies", values);
    }

    /**
     * Deletes the movie
     * @param movie - Movie instance
     * @return - number of rows affected in the database
     */
    public int deleteMovie(Movie movie) {
        return super.delete(movie, "movies");
    }

    /**
     * Creates a result array
     * Create a cursor of all movies that contains the searchString
     * Adds all movies to the result array
     * Returns the result array
     * @param column - column to search
     * @param searchString - string to search for
     * @return - List of movies
     */
    public List<Movie> searchMovieBy(String column, String searchString) {
        List<Movie> results = new ArrayList<>();

        // Uses the abstract parents search class
        // Table name is fetched from MovieTable.MovieEntry class
        Cursor c = super.search(MovieTable.MovieEntry.TABLE_NAME, column, searchString);
        c.moveToFirst();

        // Iterates over the cursor
        if(c.getCount() != 0) {
            try {
                do {
                    // Creates movie instance with the CoreEntityFactory
                    results.add(CoreEntityFactory.createMovieFromCursor(c));
                } while (c.moveToNext());
            } finally {
                c.close();
            }
        }
        return results;
    }

    /**
     * Creates a friends list
     * Friends join ratings table
     * Looks for friends that have seen the movie
     * Where friends.user2id = ratings.user1id
     * Adds all friends to the list
     * @param movieId - Id of movie seen
     * @param userId - Id of user that has seen the movie
     * @return - list of
     */
    public List<User> getFriendsSeenMovie(long movieId, long userId) {
        List<User> friends = new ArrayList<>();

        String query = "SELECT * FROM " + FriendTable.FriendEntry.TABLE_NAME + " JOIN " + RatingTable.RatingEntry.TABLE_NAME
                + " ON " + FriendTable.FriendEntry.TABLE_NAME + "." + FriendTable.FriendEntry.COLUMN_NAME_USER2ID +
                " = " + RatingTable.RatingEntry.TABLE_NAME + "." + RatingTable.RatingEntry.COLUMN_NAME_USERID +
                " JOIN " + UserTable.UserEntry.TABLE_NAME + " ON " + FriendTable.FriendEntry.TABLE_NAME + "." +
                FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " = " + UserTable.UserEntry.TABLE_NAME + "." +
                UserTable.UserEntry.COLUMN_NAME_ID + " WHERE " +
                RatingTable.RatingEntry.TABLE_NAME + "." + RatingTable.RatingEntry.COLUMN_NAME_MOVIEID
                + " = ?";


        Cursor c = db.rawQuery(query, new String[]{String.valueOf(movieId)});

        // Iterates over the cursor
        if(c.getCount() != 0) {
            try {
                while (c.moveToNext()) {
                    friends.add(CoreEntityFactory.createUserFromCursor(c));
                }
            } finally {
                c.close();
            }
        }

        c.close();

        return friends;
    }

    @Override
    public int numOfFriendsHasSeenMovie(long movieId, long userId) {
        return getFriendsSeenMovie(movieId, userId).size();
    }

    public List<Movie> getCommunityTopPicks(int max){
        String sqlString = "SELECT * FROM movies ORDER BY " + MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING + "  " + "DESC LIMIT " + max;
        return getCommunityFeedback(max, sqlString);
    }

    public List<Movie> getMostDislikedMovies(int max){
        String sqlString = "SELECT * FROM movies ORDER BY " + MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING + "  " + "ASC LIMIT " + max;
        return getCommunityFeedback(max, sqlString);
    }

    public List<Movie> getTopRecommendedMoviesThisYear(int max, int year){
        String sqlString = "SELECT * FROM movies WHERE  " + MovieTable.MovieEntry.COLUMN_NAME_YEAR +
                " LIKE \'" + year + "\' ORDER BY " + MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING + " DESC LIMIT " + max;
        return getCommunityFeedback(max, sqlString);
    }

    private List<Movie> getCommunityFeedback(int max, String query){
        //Query the database of sorting the movieTable by "requestedSorting" and return corresponding cursor
        Cursor c = db.rawQuery(query, null);
        //Log.v("Query Movies Sorted", query);
        List<Movie> sortedMovies = new ArrayList<>();
        c.moveToFirst();

        if(c.getCount() != 0) {
            ExecutionTimeLogger executionTimeLogger = new ExecutionTimeLogger();

            executionTimeLogger.startTimer();
            db.beginTransaction();
            try {
                do {
                    Movie movie = CoreEntityFactory.createMovieFromCursor(c);
                    sortedMovies.add(movie);
                } while (c.moveToNext());
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                c.close();

            }

            Log.d("EXECUTION TIME", sortedMovies.size() + "");


            executionTimeLogger.stopTimerAndLogResults();
        }
        return sortedMovies;
    }

    public List<Movie> getMovieCollectionFromUserId(int max, long userId) {

        //1)SQL: find all ratings by user (sorted by "created at") and save to a list
        List<Rating> userRatings = getUserRatings(max, userId);

        // loop through that rating list by calling findMovie() with rating.findMovie(rating.getMovieId()); and save those movies in another list
        //return that list
        List<Movie> usersMovieCollection = new ArrayList<>();

        for (Rating userRating : userRatings) {
            long movieId = userRating.getMovieId();
            usersMovieCollection.add(findMovie(movieId));
        }

        return usersMovieCollection;

        //Finally:
        //create suitable method in Seed class to have the necessary data
        //finally - test to make sure that the method works ass supposed to.
    }

        public List<Rating> getUserRatings(int max, long userId){

        //Query the database to get the necessary ratings
            //TODO: Does not have a createdAt at the moment - need to fix this in createRatingFromCursor, saverating() etc...
        String sqlString = "SELECT * FROM ratings WHERE  " + RatingTable.RatingEntry.COLUMN_NAME_USERID +
                " LIKE \'" + userId + "\' ORDER BY " + RatingTable.RatingEntry.COLUMN_NAME_CREATED_AT + " DESC LIMIT " + max;

        Cursor c = db.rawQuery(sqlString, null);

        List<Rating> userRatings = new ArrayList<>();

        c.moveToFirst();
        if (c.getCount() > 0) {
            try {
                do {
                    Rating rating = createRatingFromCursor(c);
                    userRatings.add(rating);
                } while(c.moveToNext());
            } finally {
                c.close();
            }
        }

        return userRatings;
    }

    public Rating createRatingFromCursor(Cursor c) {

        Cursor abc = c;
        int colIndex = c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID);

        long id = c.getLong(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
        double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        long movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        long userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_USERID));

        Rating createdRating = new Rating(rating,movieId,userId);
        createdRating.setId(id);
        return createdRating;
    }

    public boolean tableExists() {
        boolean tableExists = false;

        db.beginTransaction();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{MovieTable.MovieEntry.TABLE_NAME});
        try {
            cursor.moveToFirst();
            int count = cursor.getCount();
            tableExists = (count > 0);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            cursor.close();
        }

        return tableExists;

    }

    public int getNumberOfMovies() {
        db.beginTransaction();
        int count = 0;

        String query = "SELECT COUNT(*) FROM movies";
        Cursor c = db.rawQuery(query, null);

        try {
            c.moveToFirst();
            count= c.getInt(0);
            db.setTransactionSuccessful();
        } finally {
            c.close();
            db.endTransaction();
        }

        return count;
    }

}
