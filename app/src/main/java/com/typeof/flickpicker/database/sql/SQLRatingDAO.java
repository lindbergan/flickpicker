package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.RatingDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */

public class SQLRatingDAO extends SQLDAO implements RatingDAO {

    private SQLMovieDAO sqlMovieDAO;
    private SQLiteDatabase db;


    public SQLRatingDAO(Context ctx) {
        super(ctx);
        sqlMovieDAO = new SQLMovieDAO(ctx);
        db = getDatabase();
    }

    public List<Rating> getMovieRatings(long movieId){

        List<Rating> ratingsForMovie = new ArrayList<Rating>();
        Cursor movieIdCursor = searchRatingBy("movieId", String.valueOf(movieId));
        movieIdCursor.moveToFirst();


        for(int i = 0; i < movieIdCursor.getCount(); i++){
            ratingsForMovie.add(createRatingFromCursor(movieIdCursor));
            movieIdCursor.moveToNext();
        }

        movieIdCursor.close();
        return ratingsForMovie;

    }

    public long saveRating(double rating, long movieId, long userId) {

        // determine if rating already exists (same movie, same user) - if not :  createRating() is called else updateRating()
        Cursor cursor = db.rawQuery("SELECT * FROM ratings " +
                "WHERE " + RatingTable.RatingEntry.COLUMN_NAME_MOVIEID + " = ? " +
                "AND " + RatingTable.RatingEntry.COLUMN_NAME_USERID + " = ?",
                new String[]{String.valueOf(movieId), String.valueOf(userId)});

        if(cursor.getCount() == 0){
            //Update movieTable with new communityRating and create rating
            cursor.close();
            setMovieTableRating(movieId, rating);
            return createRating(rating,movieId,userId);
        }
        else {
            //Update movieTable with new communityRating and update rating
            setMovieTableRating(movieId, rating);
            return updateRating(rating, cursor);
        }
    }

    private long createRating(double rating, long movieId, long userId){

        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, movieId);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, userId);
        return super.save(new Rating(rating,movieId,userId), "ratings", values);
    }

    private long updateRating(double newRating,Cursor cursor){

        cursor.moveToFirst();

        //get previous values from cursor
        long id = cursor.getLong(cursor.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
        double rating = cursor.getDouble(cursor.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        int movieId = cursor.getInt(cursor.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        int userId = cursor.getInt(cursor.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));

        //create a RatingObject of those values to give to supers update()
        Rating ratingObject = new Rating(rating,movieId,userId);
        ratingObject.setId(id);

        //save new value via supers update()
        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_ID, id);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, newRating);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, movieId);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, userId);

        super.save(ratingObject, "ratings",values);
        cursor.close();
        return ratingObject.getId();

    }

    private double setMovieTableRating(long movieId, double rating){
        Movie movie = sqlMovieDAO.findMovie(movieId);
        double newCommunityRating = calculateCommunityRating(movie,rating);
        movie.setCommunityRating(newCommunityRating);
        int oldVotes = movie.getNumberOfVotes();
        double getCommunityRating = movie.getCommunityRating();
        movie.setNumberOfVotes(oldVotes+1);
        sqlMovieDAO.saveMovie(movie);

        return newCommunityRating;
    }

    public double calculateCommunityRating(Movie movie, double rating){
        //expression for calculating new communityRating
        return (movie.getNumberOfVotes()*movie.getCommunityRating()+rating)/(movie.getNumberOfVotes()+1);
    }

    public Cursor searchRatingBy(String column, String searchString){
        return super.search("ratings",column, searchString);
    }

    public Rating createRatingFromCursor(Cursor c) {

        long id = c.getLong(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
        double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        int movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        int userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));

        Rating createdRating = new Rating(rating,movieId,userId);
        createdRating.setId(id);
        return createdRating;
    }

    public Rating findRating(long id){
        Cursor cursor = super.find(id,"ratings");
        cursor.moveToFirst();
        Rating ratingToReturn = createRatingFromCursor(cursor);
        cursor.close();
        return ratingToReturn;
    }

    public int removeRating(long id){

        Rating ratingToDelete = findRating(id);
        return super.delete(ratingToDelete,"ratings");
    }

    public List<Movie> getCommunityTopPicks(int max){
        String sqlString = MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING + " " + "DESC";
        return getCommunityFeedback(max,sqlString);
    }


    public List<Movie> getMostDislikedMovies(int max){
        String sqlString = MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING + " " + "ASC";
        return getCommunityFeedback(max,sqlString);
    }

    public List<Movie> getTopRecommendedMoviesThisYear(int max){
        String sqlString = MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING + " AND " + MovieTable.MovieEntry.COLUMN_NAME_YEAR + " LIKE \'" + "2016" + "\'" + " DESC";
        return getCommunityFeedback(max, sqlString);
    }

    private List<Movie> getCommunityFeedback(int max, String requestedSorting){

        //Query the database of sorting the movieTable by "requestedSorting" and return corresponding cursor
        Cursor c = db.rawQuery("SELECT * FROM movies ORDER BY "+ requestedSorting + " LIMIT " + String.valueOf(max),null);
        List<Movie> sortedMovies = new ArrayList<Movie>();
        c.moveToFirst();

        int cc = c.getCount();

        for (int i = 0; i < max; i++) {
            //get the top or bottom ratings and save their movieId:s. Find movie based on id and save it to ratedMovies
            Movie movie = sqlMovieDAO.createMovieFromCursor(c);
            String t = movie.getTitle();
            double r = movie.getCommunityRating();
            sortedMovies.add(movie);
            c.moveToNext();
        }
        c.close();
        return sortedMovies;
    }


    /*
    public Rating find(long id) {
        try {
            Cursor c = super.find(id, "ratings");
            Rating rating = createRatingCursor(c);
            c.close();
            return rating;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }

    public Rating createRatingCursor(Cursor c) {
        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        Double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        int movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        int userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_USERID));
        Rating r = new Rating(rating, movieId,userId);
        r.setId(id);
        return r;
    }

    public long save(Rating rating) {
        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating.getRating());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, rating.getMovieId());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, rating.getUserId());

        return super.save(rating, "ratings", values);
    }

    /*
    public void update(Rating rating, ContentValues values) {
        super.update(rating, values);
    }
    */

    /*

    public void update(Rating rating, ContentValues values) {
        super.update(rating, values, "ratings");
    }

    public long delete(Rating rating) {
        super.delete(rating, "ratings");
        return rating.getId();
    }

    /*
    public List<Rating> getRatingFromUser(){

    }

    public List<Rating> getRatingFromMovie(){

    }
    */

}
