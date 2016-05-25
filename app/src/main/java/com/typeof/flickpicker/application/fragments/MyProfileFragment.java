package com.typeof.flickpicker.application.fragments;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.RatingDAO;

import java.util.List;


/**
 * MyProfileFragment extends Fragment
 * Used for showing profile view of the current user
 */

public class MyProfileFragment extends Fragment {

    private TextView mUserProfileImageView;
    private TextView mUsernameTextView;
    private TextView mUserScoreTextView;
    private TextView mNrOfRatings;
    private TextView mMeanRating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_my_profile, container, false);
        hookUpViews(view);
        populateProfileFields();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        
        return view;

    }


    /**
     * method to assign variables to their different components in the layout file.
     */
    public void hookUpViews(View view){

        mUserProfileImageView = (TextView) view.findViewById(R.id.no_profile_picture);
        mUsernameTextView = (TextView) view.findViewById(R.id.myProfileUsernameTextView);
        mUserScoreTextView = (TextView) view.findViewById(R.id.myProfileScoreTextView);
        mNrOfRatings = (TextView) view.findViewById(R.id.myProfileNumMoviesSeenTextView);
        mMeanRating = (TextView) view.findViewById(R.id.myProfileMeadRatingTextView);

    }


    /**
     * method for adding the information about the user to the correct fields
     */
    public void populateProfileFields(){

        User user = App.getCurrentUser();
        RatingDAO ratingDAO = App.getRatingDAO();
        List<Rating> ratingList = ratingDAO.getAllRatingsFromUser(user.getId());

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
        mUsernameTextView.setText(user.getUsername());
        mUserScoreTextView.setText(String.valueOf(user.getScore()));
        mUserProfileImageView.setTypeface(font);
        mNrOfRatings.setText(String.valueOf(ratingDAO.getAllRatingsFromUser(user.getId()).size()));
        mMeanRating.setText(String.valueOf(meanRating(ratingList)));
    }

    /**
     * help method for calculating the user's mean rating value
     * @param ratingList a list of all the user's ratings
     * @return the mean of all the user's ratings
     */
    public double meanRating(List<Rating> ratingList) {

        double sum = 0;
        for (Rating r : ratingList) {
            sum = sum + r.getRating();
        }
        return round(sum / ratingList.size(), 1);
    }


    /**
     * help method for rounding of double values
     *
     * @param value the double value to be rounded
     * @param precision number of desired decimals
     * @return value rounded to chosen amount of decimals
     */
    public double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}

