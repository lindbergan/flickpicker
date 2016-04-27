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


    public SQLRatingDAO(Context ctx) {
        super(ctx);
        sqlMovieDAO = new SQLMovieDAO(ctx);
    }

    public List<Rating> getMovieRatings(long movieId){

        List<Rating> ratingsForMovie = new ArrayList<Rating>();
        Cursor movieIdCursor = searchRatingBy("movieId","5");
        movieIdCursor.moveToFirst();


        for(int i = 0; i < movieIdCursor.getCount(); i++){
            ratingsForMovie.add(createRatingFromCursor(movieIdCursor));
            movieIdCursor.moveToNext();
        }

        movieIdCursor.close();
        return ratingsForMovie;

    }

    public long saveRating(double rating, long movieId, long userId) {

        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, movieId);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, userId);
        return super.save(new Rating(rating,movieId,userId), "ratings", values);
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
        return getCommunityAllTime(max,"DESC");
    }


    public List<Movie> getMostDislikedMovies(int max){
        return getCommunityAllTime(max,"ASC");
    }

    public List<Movie> getCommunityAllTime(int max, String decOrInc){

        //Query the database of sorting the "int max" number of movies with the highest or lowest (decOrInc) rating from the rating table at the top of the table
        Cursor c = getDatabase().rawQuery("SELECT * FROM ratings ORDER BY rating " + decOrInc + " LIMIT " + String.valueOf(max),null); //DESC
        List<Movie> sortedMovies = new ArrayList<Movie>();
        c.moveToFirst();

        for (int i = 0; i < max; i++) {

            //get the top or bottom ratings and save their movieId:s. Find movie based on id and save it to ratedMovies
            Rating rating = createRatingFromCursor(c);
            long movieId = rating.getMovieId();
            sortedMovies.add(sqlMovieDAO.findMovie(movieId));
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
