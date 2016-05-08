package com.typeof.flickpicker.activities;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.sql.SQLMovieDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-03.
 */
public class SeedData {
    MovieDAO mMovieDAO = App.getMovieDAO();

    public void seed(){

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

    }


}
