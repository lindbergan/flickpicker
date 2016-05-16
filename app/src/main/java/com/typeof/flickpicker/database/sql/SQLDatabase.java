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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public void seedDatabase() {

        Movie m1 = new Movie("M1", 1995);
        Movie m2 = new Movie("M2", 1996);
        Movie m3 = new Movie("M3", 1997);
        Movie m4 = new Movie("M4", 1998);
        Movie m5 = new Movie("M5", 1999);
        Movie m6 = new Movie("M6", 2000);
        Movie m7 = new Movie("M7", 2001);
        Movie m8 = new Movie("M8", 2002);
        Movie m9 = new Movie("M9", 2003);
        Movie m10 = new Movie("M10", 2003);
        Movie m11 = new Movie("M11", 2004);
        Movie m12 = new Movie("M12", 2005);
        Movie m13 = new Movie("M13", 2006);

        MovieDAO movieDAO = App.getMovieDAO();
        movieDAO.saveMovie(m1);
        movieDAO.saveMovie(m2);
        movieDAO.saveMovie(m3);
        movieDAO.saveMovie(m4);
        movieDAO.saveMovie(m5);
        movieDAO.saveMovie(m6);
        movieDAO.saveMovie(m7);
        movieDAO.saveMovie(m8);
        movieDAO.saveMovie(m9);
        movieDAO.saveMovie(m10);
        movieDAO.saveMovie(m11);
        movieDAO.saveMovie(m12);
        movieDAO.saveMovie(m13);

        User u1 = new User("U1", "P1");
        User u2 = new User("U2", "P2");
        User u3 = new User("U3", "P3");
        User u4 = new User("U4", "P4");
        User u5 = new User("U5", "P5");
        User u6 = new User("U6", "P6");

        UserDAO userDAO = App.getUserDAO();
        userDAO.saveUser(u1);
        userDAO.saveUser(u2);
        userDAO.saveUser(u3);
        userDAO.saveUser(u4);
        userDAO.saveUser(u5);
        userDAO.saveUser(u6);

        long currentUserId = App.getCurrentUser().getId();

        Rating r1 = new Rating(1.0, m1.getId(), u1.getId());
        Rating r2 = new Rating(2.0, m2.getId(), u2.getId());
        Rating r3 = new Rating(3.0, m3.getId(), u3.getId());
        Rating r4 = new Rating(4.0, m4.getId(), u4.getId());
        Rating r5 = new Rating(5.0, m5.getId(), u5.getId());
        Rating r6 = new Rating(1.0, m6.getId(), u6.getId());
        Rating r7 = new Rating(2.0, m7.getId(), u1.getId());
        Rating r8 = new Rating(3.0, m8.getId(), u2.getId());
        Rating r9 = new Rating(5.0, m9.getId(), u3.getId());
        Rating r10 = new Rating(1.0, m10.getId(), currentUserId);
        Rating r11 = new Rating(1.0, m10.getId(), currentUserId);
        Rating r12 = new Rating(1.0, m10.getId(), currentUserId);
        Rating r13 = new Rating(1.0, m10.getId(), currentUserId);

        RatingDAO ratingDAO = App.getRatingDAO();
        ratingDAO.saveRating(r1);
        ratingDAO.saveRating(r2);
        ratingDAO.saveRating(r3);
        ratingDAO.saveRating(r4);
        ratingDAO.saveRating(r5);
        ratingDAO.saveRating(r6);
        ratingDAO.saveRating(r7);
        ratingDAO.saveRating(r8);
        ratingDAO.saveRating(r9);
        ratingDAO.saveRating(r10);
        ratingDAO.saveRating(r11);
        ratingDAO.saveRating(r12);
        ratingDAO.saveRating(r13);

        FriendDAO friendDAO = App.getFriendDAO();
        friendDAO.addFriend(new Friend(currentUserId, u1.getId()));
        friendDAO.addFriend(new Friend(currentUserId, u2.getId()));
        friendDAO.addFriend(new Friend(currentUserId, u3.getId()));
        friendDAO.addFriend(new Friend(currentUserId, u4.getId()));
        friendDAO.addFriend(new Friend(currentUserId, u5.getId()));

        PlaylistDAO playlistDAO = App.getPlaylistDAO();
        playlistDAO.savePlaylist(new Playlist("Watchlist", currentUserId));

    }

    public void clearDatabase() {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
    }

}
