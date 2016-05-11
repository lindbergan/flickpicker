package com.typeof.flickpicker.activities;

import android.app.Application;
import android.content.Context;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.database.sql.SQLDatabase;
import com.typeof.flickpicker.database.sql.SQLFriendDAO;
import com.typeof.flickpicker.database.sql.SQLMovieDAO;
import com.typeof.flickpicker.database.sql.SQLPlaylistDAO;
import com.typeof.flickpicker.database.sql.SQLRatingDAO;
import com.typeof.flickpicker.database.sql.SQLUserDAO;
import com.typeof.flickpicker.utils.MetaData;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-03.
 */
public class App extends Application {

    private static Context mContext;
    private static User mCurrentUser;
    private static String databaseType;
    private static MovieDAO sMovieDAO;
    private static UserDAO sUserDAO;
    private static RatingDAO sRatingDAO;
    private static PlaylistDAO sPlaylistDAO;
    private static FriendDAO sFriendDAO;
    private static Database sDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // Fetch the database type from the AndroidManifest.xml file
        databaseType = MetaData.getMetaData(mContext, "database_type");
        setupDAO();

        Database db = getDatabase();
        db.dropTables();
        db.setUpTables();

        mCurrentUser = new User("pelle", "password");
        getUserDAO().saveUser(mCurrentUser);

        db.seedDatabase();
    }

    private void setupDAO() {
        switch (databaseType) {
            case "sql":
                sMovieDAO = new SQLMovieDAO(mContext);
                sUserDAO = new SQLUserDAO(mContext);
                sRatingDAO = new SQLRatingDAO(mContext);
                sPlaylistDAO = new SQLPlaylistDAO(mContext);
                sFriendDAO = new SQLFriendDAO(mContext);
                sDatabase = new SQLDatabase(mContext);
            default:
                sMovieDAO = new SQLMovieDAO(mContext);
                sUserDAO = new SQLUserDAO(mContext);
                sRatingDAO = new SQLRatingDAO(mContext);
                sPlaylistDAO = new SQLPlaylistDAO(mContext);
                sFriendDAO = new SQLFriendDAO(mContext);
                sDatabase = new SQLDatabase(mContext);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Database db = getDatabase();
        db.dropTables();
    }

    public static User getCurrentUser() {return mCurrentUser;}

    public static MovieDAO getMovieDAO() {
        return sMovieDAO;
    }

    public static UserDAO getUserDAO() {
        return sUserDAO;
    }

    public static RatingDAO getRatingDAO() {
        return sRatingDAO;
    }

    public static PlaylistDAO getPlaylistDAO() {
        return sPlaylistDAO;
    }

    public static FriendDAO getFriendDAO() {
        return sFriendDAO;
    }

    public static Database getDatabase() {
        return sDatabase;
    }
}
