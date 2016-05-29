package com.typeof.flickpicker.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import java.util.List;
import java.util.Random;

public class RandomizedData extends AsyncTask<Void, Void, Void> {

    private final ProgressDialog mProgressDialog;

    public RandomizedData(Context context) {
        mProgressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Creating dummy data... hold on!");
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void createRandomizedData() {
        Random rand = new Random();

        MovieDAO movieDAO = App.getMovieDAO();
        UserDAO userDAO = App.getUserDAO();
        RatingDAO ratingDAO = App.getRatingDAO();

        List<Movie> movieList = movieDAO.getCommunityTopPicks(500);
        movieList.size();
        List<User> userList = userDAO.getAllUsers();
        
        // create 100 ratings
        for(int i = 0; i < 500; i++) {
            int randomMovieIndex = rand.nextInt(movieList.size());
            int randomUserIndex = rand.nextInt(userList.size());
            double randomRating = 1 + rand.nextInt(5); //max rating + 1 because bound is exclusive

            long randomMovieId = movieList.get(randomMovieIndex).getId();
            long randomUserId = userList.get(randomUserIndex).getId();

            Rating rating = new Rating(randomRating, randomMovieId, randomUserId);
            ratingDAO.saveRating(rating);
        }
    }



    @Override
    protected Void doInBackground(Void... params) {
        createRandomizedData();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressDialog.hide();
        FriendDAO friendDAO = App.getFriendDAO();
        friendDAO.updateFriendMatches(App.getCurrentUser().getId());

        App.getEventBus().triggerEvent("randomize_data");
    }

}
