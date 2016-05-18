package com.typeof.flickpicker.database.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.utils.OMDBParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class SQLDatabase implements Database {

    private SQLiteDatabase db;

    public SQLDatabase(Context ctx) {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(ctx);
        this.db = mDbHelper.getWritableDatabase();
    }

    @Override
    public void setUpTables() {

        //-----Movie-----
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());

        //-----User-----
        db.execSQL(UserTable.UserEntry.getSQLCreateTableQuery());

        //-----Rating-----
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());

        //-----Playlist-----
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLCreateTableQuery());

        //-----Friend-----
        db.execSQL(FriendTable.FriendEntry.getSQLCreateTableQuery());
    }

    @Override
    public void dropTables() {
        //-----Movie-----
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());

        //-----User------
        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());

        //-----Rating-----
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());

        //-----Playlist----
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLDropTableQuery());

        //-----Friend-----
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());
    }

    public void seedDatabase(Context ctx) {

    }

    public void requestMoviesFromOMDB(Context ctx){
        OMDBParser omdbParser = new OMDBParser(ctx, App.getMovieDAO());
        omdbParser.execute();
        try {
            List<Movie> movies = omdbParser.get();
            MovieDAO movieDAO = App.getMovieDAO();
            for(Movie m : movies) {
                movieDAO.saveMovie(m);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean hasBeenSeeded() {
        MovieDAO movieDAO = App.getMovieDAO();
        return movieDAO.tableExists() && movieDAO.getNumberOfMovies() >= OMDBParser.numberOfMovies;
    }
}
