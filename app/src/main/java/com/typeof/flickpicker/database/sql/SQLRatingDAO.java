package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
    
    public long saveRating(Rating rating){


        if(rating.getId() != 0) {

            //that means rating previously exists. Need to find old rating before writing new
            // rating to database

            String query = "SELECT " + RatingTable.RatingEntry.COLUMN_NAME_RATING + " FROM ratings " +
                    " WHERE " + RatingTable.RatingEntry.COLUMN_NAME_MOVIEID + " = " +
                    String.valueOf(rating.getMovieId()) + " AND " +
                    RatingTable.RatingEntry.COLUMN_NAME_USERID + " = " +
                    String.valueOf(rating.getUserId());

            Cursor c = db.rawQuery(query, null);

            if(c.getCount() != 0) {
                c.moveToFirst();
                Rating r = createRatingFromCursor(c);
                double oldRatingValue = r.getRating();
                c.close();
                setMovieTableRating(rating.getMovieId(), oldRatingValue, rating.getRating());
            }
            else{
                c.close();
                double oldValue = 0;
                setMovieTableRating(rating.getMovieId(), oldValue, rating.getRating());
            }



        }
        else{
            setMovieTableRating(rating.getMovieId(), 0, rating.getRating());
        }

        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating.getRating());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, rating.getMovieId());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, rating.getUserId());

        return super.save(rating, "ratings", values);
    }

    private double setMovieTableRating(long movieId, double oldRating, double newRating){

        Movie movie = sqlMovieDAO.findMovie(movieId);

        double newCommunityRating = calculateCommunityRating(movie,oldRating, newRating);
        movie.setCommunityRating(newCommunityRating);

        if(oldRating == 0){
            int oldNumberOfVotes = movie.getNumberOfVotes();
            int newNumberOfVotes = oldNumberOfVotes +1;
            movie.setNumberOfVotes(newNumberOfVotes);
        }

        sqlMovieDAO.saveMovie(movie);
        return newCommunityRating;
    }

    public double calculateCommunityRating(Movie movie, double oldRating, double newRating){

        //expression for calculating new communityRating
        if (oldRating != 0){
            return (movie.getNumberOfVotes()*movie.getCommunityRating() - oldRating + newRating) / (movie.getNumberOfVotes());
        }
        else{
            return (movie.getNumberOfVotes()*movie.getCommunityRating() + newRating) / (movie.getNumberOfVotes()+1);
        }
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
        return super.delete(ratingToDelete, "ratings");
    }


}
