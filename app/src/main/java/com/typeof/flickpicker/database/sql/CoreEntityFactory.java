package com.typeof.flickpicker.database.sql;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.Rating;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 19/05/16.
 */
public class CoreEntityFactory {

    public static Friend createFriendFromCursor(Cursor c){

        long id = c.getLong(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_ID));
        long userId1 = c.getLong(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_USER1ID));
        long userId2 = c.getLong(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_USER2ID));
        double disMatch = c.getDouble(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_DISMATCH));
        int nmbrOfMoviesBothSeen = c.getInt(c.getColumnIndex(FriendTable.FriendEntry.COLUMN_NAME_NUMBER_OF_MOVIES_BOTH_SEEN));

        Friend friendRelation = new Friend(userId1, userId2);
        friendRelation.setId(id);
        friendRelation.setDisMatch(disMatch);
        friendRelation.setNmbrOfMoviesBothSeen(nmbrOfMoviesBothSeen);

        return friendRelation;

    }


    public static Movie createMovieFromCursor(Cursor c) {

        String title = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        int year = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_YEAR));
        double rating = c.getDouble(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING));
        int votes = c.getInt(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_VOTES));
        String genre = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_GENRE));
        String poster = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_POSTER));
        String description = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION));

        Movie m = new Movie(id, title, year);
        m.setCommunityRating(rating);
        m.setNumberOfVotes(votes);
        m.setGenre(genre);
        m.setPoster(poster);
        m.setDescription(description);

        return m;
    }

    public static Playlist createPlaylistFromCursor(Cursor c) {

        Gson gson = new Gson();
        long id = c.getLong(c.getColumnIndex("id"));
        String title = c.getString(c.getColumnIndex("title"));
        long userId = c.getLong(c.getColumnIndex("user_id"));
        String moviesListJSON = c.getString(c.getColumnIndex("movies_list"));
        List<Number> moviesFromDB = gson.fromJson(moviesListJSON, new TypeToken<ArrayList<Number>>() {
        }.getType());

        Playlist playlist = new Playlist(title, userId, moviesFromDB);
        playlist.setId(id);

        return playlist;

    }

    public static Rating createRatingFromCursor(Cursor c) {

        long id = c.getLong(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
        double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        long movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        long userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_USERID));

        Rating createdRating = new Rating(rating,movieId,userId);
        createdRating.setId(id);
        return createdRating;
    }

}
