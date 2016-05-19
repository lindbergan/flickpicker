package com.typeof.flickpicker.database.sql;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;

import java.util.ArrayList;
import java.util.List;



/**
 * FlickPicker
 * Group 22
 * Created on 19/05/16.
 */
public class CoreEntityFactory {



    /**
     * Method for creating Friend object from specific record in User database
     *
     * @param c Cursor
     * @return new Friend relation object created from cursor
     */
    public static Friend createFriendFromCursor(Cursor c){

        long id = c.getLong(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_ID));
        long userId1 = c.getLong(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_USER1ID));
        long userId2 = c.getLong(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_USER2ID));
        double disMatchValue = c.getDouble(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_DISMATCH));
        int numOfMoviesBothSeen = c.getInt(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_NUMBER_OF_MOVIES_BOTH_SEEN));

        Friend friendRelation = new Friend(userId1, userId2);
        friendRelation.setId(id);
        friendRelation.setDisMatch(disMatchValue);
        friendRelation.setNmbrOfMoviesBothSeen(numOfMoviesBothSeen);

        return friendRelation;

    }


    /**
     * Method for creating Movie object from specific record in User database
     *
     * @param c Cursor
     * @return new Movie object created from cursor
     */
    public static Movie createMovieFromCursor(Cursor c) {

        String title = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        int year = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_YEAR));
        double rating = c.getDouble(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING));
        int votes = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_VOTES));
        String genre = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_GENRE));
        String poster = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_POSTER));
        String description = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION));

        Movie movieObject = new Movie(id, title, year);
        movieObject.setCommunityRating(rating);
        movieObject.setNumberOfVotes(votes);
        movieObject.setGenre(genre);
        movieObject.setPoster(poster);
        movieObject.setDescription(description);

        return movieObject;
    }


    /**
     * Method for creating Playlist object from specific record in User database
     *
     * @param c Cursor
     * @return new Playlist object created from cursor
     */
    public static Playlist createPlaylistFromCursor(Cursor c) {

        Gson gson = new Gson();
        long id = c.getLong(c.getColumnIndex("id"));
        String title = c.getString(c.getColumnIndex("title"));
        long userId = c.getLong(c.getColumnIndex("user_id"));
        String moviesListJSON = c.getString(c.getColumnIndex("movies_list"));
        List<Number> moviesFromDB = gson.fromJson(moviesListJSON, new TypeToken<ArrayList<Number>>() {
        }.getType());

        Playlist playlistObject = new Playlist(title, userId, moviesFromDB);
        playlistObject.setId(id);

        return playlistObject;

    }

    /**
     * Method for creating Rating object from specific record in User database
     *
     * @param c Cursor
     * @return new Rating object created from cursor
     */
    public static Rating createRatingFromCursor(Cursor c) {

        long id = c.getLong(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
        double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        long movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        long userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_USERID));

        Rating ratingObject = new Rating(rating,movieId,userId);
        ratingObject.setId(id);
        return ratingObject;

    }


    /**
     * Method for creating User object from specific record in User database
     *
     * @param c Cursor
     * @return new User created from cursor
     */
    public static User createUserFromCursor(Cursor c){

        long id = c.getLong(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_ID));
        String username = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_USERNAME));
        String password = c.getString(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_PASSWORD));
        int score = c.getInt(c.getColumnIndex(UserTable.UserEntry.COLUMN_NAME_SCORE));

        User userObject = new User(username, password);
        userObject.setId(id);
        userObject.setScore(score);

        return userObject;
        
    }

}
