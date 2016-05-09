package com.typeof.flickpicker.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Rating;


/**
 * FlickPicker
 * Group 22
 * Created on 09/05/16.
 */
public class MovieDetailFragment extends Fragment {

    private ImageView movieImage;
    private TextView movieTitle;
    private RatingBar ratingBar;
    private Button rateButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View movieDetailView = inflater.inflate(R.layout.activity_movie_detail, container, false);

        movieImage = (ImageView) getView().findViewById(R.id.movieDetailImageView);

        //TODO: add icons to Movies
        movieImage.setImageDrawable(null); //setImageIcon does not work due to API mismatch?



        //setting up button
        rateButton = (Button) getView().findViewById(R.id.movieDetailRateButton);

        //creates and saves new rating when rate button is clicked
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long userId = App.getCurrentUser().getId();
                long movieId = 0;

                double numStars = (double)ratingBar.getNumStars();
                Rating newRating = new Rating(numStars, movieId, userId);


            }
        });

        return movieDetailView;
    }

}
