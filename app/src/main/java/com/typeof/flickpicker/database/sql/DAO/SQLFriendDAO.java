package com.typeof.flickpicker.database.sql.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.sql.CoreEntityFactory;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.database.sql.tables.FriendTable;
import com.typeof.flickpicker.database.sql.tables.RatingTable;
import com.typeof.flickpicker.database.sql.tables.UserTable;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */

public class SQLFriendDAO extends SQLDAO implements FriendDAO {

    private SQLiteDatabase db;
    private SQLMovieDAO mMovieDAO;

    public SQLFriendDAO(Context ctx) {
        super(ctx);
        SQLiteDatabaseHelper dbhelper = SQLiteDatabaseHelper.getInstance(ctx);
        db = dbhelper.getWritableDatabase();
        mMovieDAO = new SQLMovieDAO(ctx);
    }

    /**
     * Adds the friends values to user1id and user2id columns
     * Saves the friend to the database
     *
     * @param f     Friend entity
     * @return      id of friend added
     */

    @Override
    public long addFriend(Friend f) {
        ContentValues values = new ContentValues();
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER1ID, f.getUserIdOne());
        values.put(FriendTable.FriendEntry.COLUMN_NAME_USER2ID, f.getGetUserIdTwo());
        values.put(FriendTable.FriendEntry.COLUMN_NAME_DISMATCH, f.getDisMatch());
        values.put(FriendTable.FriendEntry.COLUMN_NAME_NUMBER_OF_MOVIES_BOTH_SEEN, f.getNmbrOfMoviesBothSeen());
        return super.save(f, FriendTable.FriendEntry.TABLE_NAME, values);
    }

    /**
     * A super SQL-question made by: SebbeTheMan
     * If cursor count is 0 --> returns a empty list
     * Otherwise adds all users and returns the list
     *
     * @param id - given user id
     * @return List of friends belonging to the user
     */
    @Override
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
                userFriends.add(CoreEntityFactory.createUserFromCursor(c));
            } while (c.moveToNext());
        } finally {
            c.close();
        }

        return userFriends;
    }

    /**
     * Removes the friend values from the database
     *
     * @param userId1 - first user
     * @param userId2 - second user
     * @return - number of rows affected by the database
     */
    @Override
    public long removeFriend(long userId1, long userId2) {
        return db.delete(FriendTable.FriendEntry.TABLE_NAME,
                FriendTable.FriendEntry.COLUMN_NAME_USER1ID + " = ? AND " + FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " = ?",
                new String[]{String.valueOf(userId1), String.valueOf(userId2)});
    }

    /**
     * Returns list of Ratings belonging to the friends of the given user
     *
     * @param userId - given user id
     * @return List of ratings
     */
    @Override
    public List<Rating> getFriendsLatestActivities(long userId) {

        List<Rating> ratings = new ArrayList<>();

        String ratingsTable = RatingTable.RatingEntry.TABLE_NAME;
        String friendsTable = FriendTable.FriendEntry.TABLE_NAME;
        String ratingCreatedAt = ratingsTable + "." +RatingTable.RatingEntry.COLUMN_NAME_CREATED_AT;
        String friendsUserId1 = friendsTable + "." + FriendTable.FriendEntry.COLUMN_NAME_USER1ID;
        String friendsUserId2 = friendsTable + "." + FriendTable.FriendEntry.COLUMN_NAME_USER2ID;
        String ratingsUserId = ratingsTable + "." + RatingTable.RatingEntry.COLUMN_NAME_USERID;

        String query = "SELECT * FROM " + ratingsTable + " " +
                "LEFT JOIN friends ON " + friendsUserId2 + " = " + ratingsUserId + " " +
                "WHERE " + friendsUserId1 + " = " + String.valueOf(userId) + " " +
                "ORDER BY " + ratingCreatedAt + " DESC";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        if (c.getCount() != 0) {
            try {
                do {
                    ratings.add(CoreEntityFactory.createRatingFromCursor(c));
                } while (c.moveToNext());
            } finally {
                c.close();
            }
        }

        return ratings;
    }

    /**
     * This algorithm goes through every movie a user has seen and compares the users rating
     * with his/hers friends ratings. Every friend gets a mismatch value based on how similar
     * the friend and the user rates movies. A lower mismatch value gives the friend a higher
     * priority when movies are recommended to the user.
     *
     * @param rating - Rating
     */
    @Override
    public void updateFriendMatches(Rating rating){

        long currentUserId = rating.getUserId();
        int desiredSizeIfList = 100;

        List<Movie> usersMovieCollection = mMovieDAO.getMovieCollectionFromUserId(desiredSizeIfList, currentUserId);
        List<User> usersFriends = getFriendsFromUserId(currentUserId);
        String[] movieIds = extractMovieIdsFromMovies(usersMovieCollection);

        for (int i = 0; i<usersFriends.size(); i++) {

            //check all friends
            User currentFriend = usersFriends.get(i);
            Friend currentFriendShip = getFriendRelation(currentUserId, currentFriend.getId());
            double totalDismatch = 0; //default
            int nmbrOfMovieBothSeen = 0; //default

            long currentFriendsId = usersFriends.get(i).getId();

            for (int j = 0; j < usersMovieCollection.size(); j++) {

                //compare to user's movies

                String query = "SELECT e.movieId AS MOVIEID, e.userId AS USER, e.rating USERRATING, m.userId AS FRIEND, m.rating AS FRIENDRATING FROM ratings e " +
                        "INNER JOIN ratings m ON e.userId LIKE " + currentUserId + " AND e.movieId = ? " +
                        "AND m.userId like " + currentFriendsId + " AND m.movieId = ? ";

                Cursor c = db.rawQuery(query, new String[]{movieIds[j],movieIds[j]});

                if (c.getCount() != 0) {
                    c.moveToFirst();
                    nmbrOfMovieBothSeen++;
                    totalDismatch = calculateNewMismatchValue(c, totalDismatch, nmbrOfMovieBothSeen);
                    c.close();
                }
            }

            //set the updated values to the friendRelation && save it
            double disMatch = totalDismatch/nmbrOfMovieBothSeen;

            currentFriendShip.setNmbrOfMoviesBothSeen(nmbrOfMovieBothSeen);
            currentFriendShip.setDisMatch(disMatch);
            addFriend(currentFriendShip);

        }
    }

    /**
     * Calculates the mismatch value
     *
     * @param c                         Cursor
     * @param oldTotalMismatchValue     Previous mismatch value
     * @param nmbrOfMovieBothSeen       Number of movies both of the users has seen
     * @return                          The new mismatch value
     */
    public double calculateNewMismatchValue(Cursor c, double oldTotalMismatchValue, int nmbrOfMovieBothSeen){
        double usersRating = c.getLong(2);
        double friendRating = c.getLong(4);

        double newDiff = Math.abs(usersRating - friendRating);
        return oldTotalMismatchValue + newDiff;
    }

    /**
     * Return the movie ids from list of movies
     *
     * @param movies    List of movie objects
     * @return          String array of movie ids
     */
    public String[] extractMovieIdsFromMovies(List<Movie> movies){
        //extract the ids
        String[] movieIds = new String[movies.size()];

        for (int i = 0; i < movies.size(); i++){

            long movieId = movies.get(i).getId();
            String movieIdAsString = String.valueOf(movieId);
            movieIds[i] = movieIdAsString;
        }

        return movieIds;
    }

    /**
     * Get the Friend entity that two users share
     *
     * @param userId1   Id of user 1
     * @param userId2   Id of user 2
     * @return          Friend object
     */
    public Friend getFriendRelation(long userId1, long userId2){
        String query = "SELECT * FROM " + FriendTable.FriendEntry.TABLE_NAME +
                " WHERE " + FriendTable.FriendEntry.COLUMN_NAME_USER1ID + " LIKE "
                + userId1 + " AND " + FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " LIKE " + userId2;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        Friend friendRelation = CoreEntityFactory.createFriendFromCursor(c);
        c.close();

        return friendRelation;
    }


    /**
     * Looks for friend relation with another user
     *
     * @param user2Id   Other users id
     * @return          boolean
     */
    public boolean isFriend(long user2Id) {
        String query = "SELECT * FROM " + FriendTable.FriendEntry.TABLE_NAME + " WHERE " + FriendTable.FriendEntry.COLUMN_NAME_USER1ID
                + " = ? AND " + FriendTable.FriendEntry.COLUMN_NAME_USER2ID + " = ?";
        long currentUserId = App.getCurrentUser().getId();
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(currentUserId), String.valueOf(user2Id)});
        c.moveToFirst();
        if (c.getCount() == 1) return true;
        c.close();
        return false;
    }
}
