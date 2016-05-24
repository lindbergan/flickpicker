package com.typeof.flickpicker.application;

import android.app.Application;
import android.content.Context;

import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.database.sql.SQLDatabase;
import com.typeof.flickpicker.database.sql.DAO.SQLFriendDAO;
import com.typeof.flickpicker.database.sql.DAO.SQLMovieDAO;
import com.typeof.flickpicker.database.sql.DAO.SQLPlaylistDAO;
import com.typeof.flickpicker.database.sql.DAO.SQLRatingDAO;
import com.typeof.flickpicker.database.sql.DAO.SQLUserDAO;
import com.typeof.flickpicker.utils.MetaData;

/**
 * App
 *
 * The Applications main class.
 * Sets up the database and the access to the DAO objects.
 *
 * The class loads settings about the database from the AndroidManifest.xml file.
 * The DAO and the Database is created based on the settings in that file.
 *
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
        initDatabase();

        setupDAO();

        if(!sDatabase.hasBeenCreated()) {
            createDatabase();
        }

        setupCurrentUser();
    }

    private static void createCurrentUser() {
        mCurrentUser = new User("AdminU", "AdminP");
        getUserDAO().saveUser(mCurrentUser);
    }

    public static void setupCurrentUser() {
        try {
            mCurrentUser = getUserDAO().getUserById(1);
        } catch (DatabaseRecordNotFoundException e) {
            createCurrentUser();
        }
    }

    private void initDatabase() {
        switch (databaseType) {
            case "sql":
                sDatabase = new SQLDatabase(mContext);
                break;
            default:
                sDatabase = new SQLDatabase(mContext);
        }
    }

    public static void createDatabase() {
        sDatabase.dropTables();
        sDatabase.setUpTables();
    }

    private void setupDAO() {
        switch (databaseType) {
            case "sql":
                sMovieDAO = new SQLMovieDAO(mContext);
                sUserDAO = new SQLUserDAO(mContext);
                sRatingDAO = new SQLRatingDAO(mContext);
                sPlaylistDAO = new SQLPlaylistDAO(mContext);
                sFriendDAO = new SQLFriendDAO(mContext);

            default:
                sMovieDAO = new SQLMovieDAO(mContext);
                sUserDAO = new SQLUserDAO(mContext);
                sRatingDAO = new SQLRatingDAO(mContext);
                sPlaylistDAO = new SQLPlaylistDAO(mContext);
                sFriendDAO = new SQLFriendDAO(mContext);

        }
    }

    /**
     * The static methods are used in controllers. The method returns the Interface version
     * of the DAO. Because of this, the controller does not care what type of database that's
     * being used by the application.
     *
     */
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
