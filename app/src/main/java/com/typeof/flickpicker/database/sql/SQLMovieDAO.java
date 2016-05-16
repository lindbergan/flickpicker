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
     * @param id
     * @return
     */

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

    /**
     * Collects information about a movie and samples them variables
     * Then returns a newly created movie
     * @param c
     * @return
     */

    public Movie createMovieFromCursor(Cursor c) {
        String title = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        int year = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_YEAR));
        double rating = c.getDouble(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING));
        int votes = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_VOTES));
        Movie m = new Movie(id, title, year);
        m.setCommunityRating(rating);
        m.setNumberOfVotes(votes);
        return m;
    }

    /**
     * Creates a map (ContentValues)
     * Puts information in the movie columns
     * Saves the movie in the movies table
     * @param movie
     * @return
     */

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

    /**
     * Creates a result array
     * Create a cursor of all movies that contains the searchString
     * Adds all movies to the result array
     * Returns the result array
     * @param column
     * @param searchString
     * @return
     */

    public List<Movie> searchMovieBy(String column, String searchString) {
        List<Movie> results = new ArrayList<>();
        Cursor c = super.search("movies", column, searchString);
        c.moveToFirst();

        if(c.getCount() != 0) {
            try {
                do {
                    results.add(createMovieFromCursor(c));
                } while (c.moveToNext());
            } finally {
                c.close();
            }
        }
        return results;
    }

    /**
     * Same functionality as createMovieFromCursor
     * @param c
     * @return
     */

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

    /**
     * Creates a friends list
     * Friends join ratings table
     * Looks for friends that have seen the movie
     * Where friends.user2id = ratings.user1id
     * Adds all friends to the list
     * @param movieId
     * @param userId
     * @return
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


        while (c.moveToNext()) {
            friends.add(createUserFromCursor(c));
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
        return getCommunityFeedback(max,sqlString);
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
                    Movie movie = createMovieFromCursor(c);
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

    public List<Movie> getUsersMovieCollection(int max, long userId) {

        //1)SQL: find all ratings by user (sorted by "created at") and save to a list
        List<Rating> userRatings = getUserRatings(max,userId);

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
        try {
            do {
                Rating rating = createRatingFromCursor(c);
                userRatings.add(rating);
            } while(c.moveToNext());
        } finally {
            c.close();
        }
        return userRatings;
    }

    public Rating createRatingFromCursor(Cursor c) {

        long id = c.getLong(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
        double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        long movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        long userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));

        Rating createdRating = new Rating(rating,movieId,userId);
        createdRating.setId(id);
        return createdRating;
    }


}
