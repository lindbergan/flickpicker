package com.typeof.flickpicker.core;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 03/05/16.
 */

public class MovieAlgorithm {

    /**
     * Method for getting movies recommended for the user based on algorithm.
     * @return List of movies recommended for the user.
     */
    public static List<Movie> getRecommendations(User user) {

        FriendDAO mFriendDAO = App.getFriendDAO();
        UserDAO mUserDAO = App.getUserDAO();
        MovieDAO mMovieDAO = App.getMovieDAO();
        User currentUser = user;

        List<Movie> results = new ArrayList<>();
        int requirements = 5;

        List<User> users = mFriendDAO.getFriendsFromUserId(user.getId());
        List<Friend> friendsWithSimilarTaste = getFriendsWithSimilarTaste(users, user, requirements);
        List<Movie> usersMovieCollection = mMovieDAO.getUsersMovieCollection(100, currentUser.getId());

        getFriendsMovies();
        removeMoviesUserHasSeen();
        sortRemainingMovies();


       return results;
    }

    public static List<Friend> getFriendsWithSimilarTaste(List<User> users, User currentUser, int requirements ){

        //convert the users to get the List of friends if the requirements are met
        List<Friend> friendsWithSimilarTaste = new ArrayList<Friend>();

        for (int i = 0; i < users.size(); i++){

            Friend currentFriend = App.getFriendDAO().getFriendRelation(currentUser.getId(),users.get(i).getId());

            if (currentFriend.getNmbrOfMoviesBothSeen() >= requirements){
                friendsWithSimilarTaste.add(currentFriend);
            }
        }

        return friendsWithSimilarTaste;
    }
}
