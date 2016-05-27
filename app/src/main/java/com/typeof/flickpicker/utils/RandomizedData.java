package com.typeof.flickpicker.utils;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.helpers.RatingHelper;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;
import java.util.Random;

class RandomizedData {

    private static MovieDAO mMovieDAO = App.getMovieDAO();
    private static final FriendDAO mFriendDAO = App.getFriendDAO();
    private static UserDAO mUserDAO = App.getUserDAO();
    private static RatingDAO mRatingDAO = App.getRatingDAO();
    private static PlaylistDAO mPlaylistDAO = App.getPlaylistDAO();
    private static final long currentUserId = App.getCurrentUser().getId();

    public static void randomizeData(int amount) {

        Random r = new Random();

        for (int i = 0; i < amount; i++) {
            int userR = 1 + r.nextInt(11);
            int movieR = 1 + r.nextInt(248);
            int ratingR = 1 + r.nextInt(5);
            RatingHelper.createNewRating(ratingR, movieR, userR);
            mFriendDAO.addFriend(new Friend(currentUserId, userR));
            if (ratingR < 4) {
                RatingHelper.createNewRating(1 + r.nextInt(5), movieR, currentUserId);
            }
        }
    }

}
