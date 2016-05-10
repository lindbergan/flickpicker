package com.typeof.flickpicker.activities;

import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.database.sql.SQLMovieDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-03.
 */
public class SeedData {
    static MovieDAO mMovieDAO = App.getMovieDAO();
    static RatingDAO mRatingDAO = App.getRatingDAO();
    static UserDAO mUserDAO = App.getUserDAO();
    static PlaylistDAO mPlaylistDAO = App.getPlaylistDAO();
    private static long userId;

    public static void seedCommunityData(){

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

    }

    public static void seedMyCollectionData(){

        Movie Oblivion = new Movie("Oblivion", 2016);
        Oblivion.setCommunityRating(3.3);
        long movieIdOblivion = mMovieDAO.saveMovie(Oblivion);

        Movie BraveHeart = new Movie("BraveHeart", 2014);
        BraveHeart.setCommunityRating(4.1);
        long movieIdBraveHeart = mMovieDAO.saveMovie(BraveHeart);

        Movie KarateKid = new Movie("KarateKid", 2016);
        KarateKid.setCommunityRating(3.6);
        long movieIdKarateKid = mMovieDAO.saveMovie(KarateKid);

        Movie guardianOfTheGalaxy = new Movie("Guardian of the Galaxy", 2014);
        guardianOfTheGalaxy.setCommunityRating(4.1);
        long movieIdGuardianOfTheGalaxy = mMovieDAO.saveMovie(guardianOfTheGalaxy);

        Movie birdman = new Movie("Birdman", 2014);
        birdman.setCommunityRating(3.3);
        long movieIdBirdman = mMovieDAO.saveMovie(birdman);

        Movie interstellar = new Movie("Interstellar", 2014);
        interstellar.setCommunityRating(4.6);
        long movieIdInterstellar = mMovieDAO.saveMovie(interstellar);

        userId = mUserDAO.saveUser(new User("Olle","password"));

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

    }

    public static void seedSearchData(){

        //get all the movies from SeedCommunityData();
        seedCommunityData();

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

    }

    public static long getUserId(){
        return userId;
    }

}
