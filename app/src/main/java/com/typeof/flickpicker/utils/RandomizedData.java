package com.typeof.flickpicker.utils;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;
import java.util.Random;

public class RandomizedData {

    private static MovieDAO mMovieDAO = App.getMovieDAO();
    private static FriendDAO mFriendDAO = App.getFriendDAO();
    private static UserDAO mUserDAO = App.getUserDAO();
    private static RatingDAO mRatingDAO = App.getRatingDAO();
    private static PlaylistDAO mPlaylistDAO = App.getPlaylistDAO();
    private static long currentUserId = App.getCurrentUser().getId();

    public static void randomizeData(int amount) {

        Random r = new Random();

        for (int i = 0; i < amount; i++) {
            int userR = 1 + r.nextInt(9);
            int movieR = 14 + r.nextInt(185);
            int ratingR = 1 + r.nextInt(5);
            App.getRatingDAO().saveRating(new Rating(ratingR, movieR, userR));
        }
    }

}
