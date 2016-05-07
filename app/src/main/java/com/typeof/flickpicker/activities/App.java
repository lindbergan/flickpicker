package com.typeof.flickpicker.activities;

import android.app.Application;
import android.content.Context;

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
    private static String databaseType;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // Fetch the database type from the AndroidManifest.xml file
        databaseType = MetaData.getMetaData(mContext, "database_type");
        Database db = getDatabase();
        db.seedDatabase();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Database db = getDatabase();
        db.dropTables();
    }

    public static MovieDAO getMovieDAO() {
        switch (databaseType) {
            case "sql":
                return new SQLMovieDAO(mContext);
            default:
                return new SQLMovieDAO(mContext);
        }
    }

    public static UserDAO getUserDAO() {
        switch (databaseType) {
            case "sql":
                return new SQLUserDAO(mContext);
            default:
                return new SQLUserDAO(mContext);
        }
    }

    public static RatingDAO getRatingDAO() {
        switch (databaseType) {
            case "sql":
                return new SQLRatingDAO(mContext);
            default:
                return new SQLRatingDAO(mContext);
        }
    }

    public static PlaylistDAO getPlaylistDAO() {
        switch (databaseType) {
            case "sql":
                return new SQLPlaylistDAO(mContext);
            default:
                return new SQLPlaylistDAO(mContext);
        }
    }

    public static FriendDAO getFriendDAO() {
        switch (databaseType) {
            case "sql":
                return new SQLFriendDAO(mContext);
            default:
                return new SQLFriendDAO(mContext);
        }
    }

    public static Database getDatabase() {

        switch (databaseType) {
            case "sql":
                return new SQLDatabase(mContext);
            default:
                return new SQLDatabase(mContext);
        }
    }
}
