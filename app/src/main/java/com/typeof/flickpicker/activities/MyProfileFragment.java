package com.typeof.flickpicker.activities;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.RatingDAO;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16/05/16.
 */
public class MyProfileFragment extends Fragment {

    private TextView mUserProfileImageView;
    private TextView mUsernameTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_profile, container, false);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");

        mUserProfileImageView = (TextView) view.findViewById(R.id.no_profile_picture);
        mUsernameTextView = (TextView) view.findViewById(R.id.myProfileUsernameTextView);
        TextView userScoreTextView = (TextView) view.findViewById(R.id.myProfileScoreTextView);
        TextView nrOfRatings = (TextView) view.findViewById(R.id.myProfileNumMoviesSeenTextView);
        TextView meanRating = (TextView) view.findViewById(R.id.myProfileMeadRatingTextView);

        User user = App.getCurrentUser();

        RatingDAO ratingDAO = App.getRatingDAO();

        List<Rating> ratingList = ratingDAO.getAllRatingsFromUser(user.getId());
        double result = 0;

        for (Rating r : ratingList) {
            result = result + r.getRating();
        }

        mUsernameTextView.setText(user.getUsername());
        userScoreTextView.setText(String.valueOf(user.getScore()));
        mUserProfileImageView.setTypeface(tf);
        nrOfRatings.setText(String.valueOf(ratingDAO.getAllRatingsFromUser(user.getId()).size()));
        meanRating.setText(String.valueOf(result/ratingList.size()));

        return view;


    }
}
