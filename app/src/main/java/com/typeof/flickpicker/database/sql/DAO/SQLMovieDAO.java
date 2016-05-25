package com.typeof.flickpicker.database.sql.DAO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.sql.CoreEntityFactory;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.database.sql.tables.FriendTable;
import com.typeof.flickpicker.database.sql.tables.MovieTable;
import com.typeof.flickpicker.database.sql.tables.RatingTable;
import com.typeof.flickpicker.database.sql.tables.UserTable;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLMovieDAO
 * Data Access Object for Movies
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
     *
     * @param id    id of sought movie
     * @return      Movie instance
     * @throws      DatabaseRecordNotFoundException
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
     *
     * @param movie     Movie instance to save
     * @return          id of created database record
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
     *
     * @param movie     Movie instance
     * @return          number of rows affected in the database
     */
    public int deleteMovie(Movie movie) {
        return super.delete(movie, "movies");
    }

    /**
     * Creates a result array
     * Create a cursor of all movies that contains the searchString
     * Adds all movies to the result array
     * Returns the result array
     *
     * @param column        column to search
     * @param searchString  string to search for
     * @return              List of movies
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
     *
     * @param movieId   Id of movie seen
     * @param userId    Id of user that has seen the movie
     * @return          list of users
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


    /** Util method that return the number of friends which has seen movie
     *
     * @param movieId   given movie id
     * @param userId    given user id
     * @return          number of friends that has seen the movie
     */
    public int numOfFriendsHasSeenMovie(long movieId, long userId) {
        return getFriendsSeenMovie(movieId, userId).size();
    }


    /**
     * Runs a given query and an array of movies
     *
     * @param max   max number of movies fetched from database
     * @param query given query
     * @return      list of movies found in the database
     */
    private List<Movie> getCommunityFeedback(int max, String query){
        //Query the database of sorting the movieTable by "requestedSorting" and return corresponding cursor
        Cursor c = db.rawQuery(query, null);
        List<Movie> sortedMovies = new ArrayList<>();
        c.moveToFirst();

        if(c.getCount() != 0) {

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

        }
        return sortedMovies;
    }

    /**
     * Uses the getCommunityFeedback method to query the database
     *
     * @param max   Mac number of movies fetched
     * @return      List of movies;
     */
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

    /**
     * Returns the movies that the user has rated
     *
     * @param max       max number of movies fetched from database
     * @param userId    given user id
     * @return          list of movies
     */
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
    }

    /**
     * Returns all the ratings from a user
     *
     * @param max       max number of ratings
     * @param userId    given user id
     * @return          list of ratings
     */
    private List<Rating> getUserRatings(int max, long userId){

        //Query the database to get the necessary ratings
        String sqlString = "SELECT * FROM ratings WHERE  " + RatingTable.RatingEntry.COLUMN_NAME_USERID +
                " LIKE \'" + userId + "\' ORDER BY " + RatingTable.RatingEntry.COLUMN_NAME_CREATED_AT + " DESC LIMIT " + max;

        Cursor c = db.rawQuery(sqlString, null);

        List<Rating> userRatings = new ArrayList<>();

        c.moveToFirst();
        if (c.getCount() > 0) {
            try {
                do {
                    Rating rating = CoreEntityFactory.createRatingFromCursor(c);
                    userRatings.add(rating);
                } while(c.moveToNext());
            } finally {
                c.close();
            }
        }

        return userRatings;
    }


}
