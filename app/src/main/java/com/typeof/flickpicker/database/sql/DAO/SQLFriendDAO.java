package com.typeof.flickpicker.database.sql.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.activities.App;
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
     * @param f - Friend entity
     * @return - friend added
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
     *
     * @param rating - Rating
     */
    @Override
    public void updateFriendMatches(Rating rating){

        long currentUserId = rating.getUserId();
        int desiredSizeIfList = 100; //maybe getUsersMovieCollection() really shouldnt have a "int max" as an argument...

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
                    totalDismatch = calculateNewMatchValue(c, totalDismatch, nmbrOfMovieBothSeen);
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

    public double calculateNewMatchValue(Cursor c, double oldTotalDisMatchValue, int nmbrOfMovieBothSeen){

        //testing data - delete when confirmed working... "<--" marks where the delete ends
        int testINt = c.getCount();
        int testColCount = c.getColumnCount();
        String[] allColumnNames = c.getColumnNames();
        String movieId = c.getColumnName(0);
        // <--

        double usersRating = c.getLong(2);
        double friendRating = c.getLong(4);

        double newDiff = Math.abs(usersRating - friendRating);
        double newTotalDisMatchValue = oldTotalDisMatchValue + newDiff;

        return newTotalDisMatchValue;
    }

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


    @Override
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
