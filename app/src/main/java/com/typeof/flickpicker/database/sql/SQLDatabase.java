package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
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

        MovieDAO mMovieDAO = App.getMovieDAO();
        UserDAO mUserDAO = App.getUserDAO();
        RatingDAO mRatingDAO = App.getRatingDAO();
        FriendDAO mFriendDAO = App.getFriendDAO();
        PlaylistDAO mPlaylistDAO = App.getPlaylistDAO();

        long userId = App.getCurrentUser().getId();

        //create dummy-movies, set a rating directly for them and save them to the database:
        Movie GoneWithTheWind = new Movie("GoneWithTheWind", 2012);
        GoneWithTheWind.setCommunityRating(2.2);
        long movieIdGoneWithTheWind = mMovieDAO.saveMovie(GoneWithTheWind);

        Movie Jaws = new Movie("Jaws", 2016);
        Jaws.setCommunityRating(4.3);
        long movieIdJaws = mMovieDAO.saveMovie(Jaws);

        Movie JurassicPark = new Movie("JurassicPark", 2015);
        JurassicPark.setCommunityRating(3.7);
        long movieIdJurassicPark = mMovieDAO.saveMovie(JurassicPark);

        Movie Oblivion = new Movie("Oblivion", 2016);
        Oblivion.setCommunityRating(3.3);
        long movieIdOblivion = mMovieDAO.saveMovie(Oblivion);

        Movie BraveHeart = new Movie("BraveHeart", 2014);
        BraveHeart.setCommunityRating(4.1);
        long movieIdBraveHeart = mMovieDAO.saveMovie(BraveHeart);

        Movie KarateKid = new Movie("KarateKid", 2016);
        KarateKid.setCommunityRating(3.6);
        long movieIdKarateKid = mMovieDAO.saveMovie(KarateKid);

        Movie theBigLebowski = new Movie("TheBigLebowski", 1998);
        theBigLebowski.setCommunityRating(4.4);
        long movieIdTheBigLebowski = mMovieDAO.saveMovie(theBigLebowski);

        Movie rocky = new Movie("Rocky", 1976);
        rocky.setCommunityRating(3.6);
        long movieIdRocky = mMovieDAO.saveMovie(rocky);

        Movie whiplash = new Movie("Whiplash", 2014);
        whiplash.setCommunityRating(3.9);
        long movieIdWhiplash = mMovieDAO.saveMovie(whiplash);

        Movie guardianOfTheGalaxy = new Movie("Guardian of the Galaxy", 2014);
        guardianOfTheGalaxy.setCommunityRating(4.1);
        long movieIdGuardianOfTheGalaxy = mMovieDAO.saveMovie(guardianOfTheGalaxy);

        Movie birdman = new Movie("Birdman", 2014);
        birdman.setCommunityRating(3.3);
        long movieIdBirdman = mMovieDAO.saveMovie(birdman);

        Movie interstellar = new Movie("Interstellar", 2014);
        interstellar.setCommunityRating(4.6);
        long movieIdInterstellar = mMovieDAO.saveMovie(interstellar);



        // users
        //let the user rate a couple of movies and save those ratings:
        long ratingid1 = mRatingDAO.saveRating(new Rating(3.5,movieIdOblivion, userId));
        long ratingid2 = mRatingDAO.saveRating(new Rating(2.0,movieIdKarateKid, userId));
        long ratingid3 = mRatingDAO.saveRating(new Rating(4.5,movieIdBraveHeart, userId));

        //create a playlist for user and make sure that the correct movies are displayed
        List<Number> usersPlayListItems = new ArrayList<Number>();
        usersPlayListItems.add(movieIdBirdman);
        usersPlayListItems.add(movieIdGuardianOfTheGalaxy);
        usersPlayListItems.add(movieIdInterstellar);

        long playlist = mPlaylistDAO.savePlaylist(new Playlist("TrippleA", userId,usersPlayListItems));


        // Test users that will be added as friends to the main user

        User testUser1 = new User("Karin", "admin");
        User testUser2 = new User("Linn", "admin");
        User testUser3 = new User("Sara", "admin");
        User testUser4 = new User("Erik", "admin");
        User testUser5 = new User("Kent", "admin");
        User testUser6 = new User("Sibelius", "admin");

        long user1Id = mUserDAO.saveUser(testUser1);
        long user2Id = mUserDAO.saveUser(testUser2);
        long user3Id = mUserDAO.saveUser(testUser3);
        long user4Id = mUserDAO.saveUser(testUser4);
        long user5Id = mUserDAO.saveUser(testUser5);
        long user6Id = mUserDAO.saveUser(testUser6);

        // Test movie

        Movie testMovie = new Movie("Interstellar", 2014);
        long movieId = mMovieDAO.saveMovie(testMovie);

        // Test ratings

        Rating testRating1 = new Rating(5.0, movieId, user1Id);
        Rating testRating2 = new Rating(5.0, movieId, user2Id);
        Rating testRating3 = new Rating(5.0, movieId, user3Id);
        Rating testRating4 = new Rating(5.0, movieId, user4Id);
        Rating testRating5 = new Rating(5.0, movieId, user5Id);
        Rating testRating6 = new Rating(5.0, movieId, user6Id);

        mRatingDAO.saveRating(testRating1);
        mRatingDAO.saveRating(testRating2);
        mRatingDAO.saveRating(testRating3);
        mRatingDAO.saveRating(testRating4);
        mRatingDAO.saveRating(testRating5);
        mRatingDAO.saveRating(testRating6);

        // Test friends

        long currentUserId = App.getCurrentUser().getId();
        mFriendDAO.addFriend(new Friend(currentUserId, user1Id));
        mFriendDAO.addFriend(new Friend(currentUserId, user2Id));
        mFriendDAO.addFriend(new Friend(currentUserId, user3Id));
        mFriendDAO.addFriend(new Friend(currentUserId, user4Id));
        mFriendDAO.addFriend(new Friend(currentUserId, user5Id));
        mFriendDAO.addFriend(new Friend(currentUserId, user6Id));

        //add users to be able to search for them:
        mUserDAO.saveUser(new User("Karin", "admin"));
        mUserDAO.saveUser(new User("Linn", "admin"));
        mUserDAO.saveUser(new User("Sara", "admin"));
        mUserDAO.saveUser(new User("Erik", "admin"));
        mUserDAO.saveUser(new User("Kent", "admin"));
        mUserDAO.saveUser(new User("Sibelius", "admin"));
        mUserDAO.saveUser(new User("Albert", "admin"));
        mUserDAO.saveUser(new User("Hebert", "admin"));
        mUserDAO.saveUser(new User("Kalle", "admin"));
        mUserDAO.saveUser(new User("Lisa", "admin"));
        mUserDAO.saveUser(new User("Jocke1989", "admin"));
        mUserDAO.saveUser(new User("MovieFanBoy2016", "admin"));
        mUserDAO.saveUser(new User("whatever", "admin"));
        mUserDAO.saveUser(new User("Karim", "admin"));


        //RECOMMENDATONDATA

        User primaryUser = App.getCurrentUser();

        Movie savingPrivateRyan = new Movie("Saving Private Ryan", 1995);
        Movie fireWalkWithMe = new Movie("Fire Walk with Me", 1991);
        savingPrivateRyan.setCommunityRating(2);
        fireWalkWithMe.setCommunityRating(4);
        long savingPrivateRyanMovieId = mMovieDAO.saveMovie(savingPrivateRyan);
        long fireWalkWithMeMovieId = mMovieDAO.saveMovie(fireWalkWithMe);

        //Add two friends that rate the same movies:
        User firstFriend = new User("Ada", "admin");
        User secondFriend = new User("Eva", "password");
        long firstFriendId = mUserDAO.saveUser(firstFriend);
        long secondFriendId = mUserDAO.saveUser(secondFriend);

        //let them rate the two movies:
        long firstFriendRatingId = mRatingDAO.saveRating(new Rating(5, savingPrivateRyanMovieId, firstFriendId));
        long secondFriendRatingId = mRatingDAO.saveRating(new Rating(1, fireWalkWithMeMovieId, secondFriendId));

        //finally... ad the as friends to primaryUser
        mFriendDAO.addFriend(new Friend(primaryUser.getId(), firstFriendId));
        mFriendDAO.addFriend(new Friend(primaryUser.getId(), secondFriendId));
    }

    public void clearDatabase() {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
    }

}
