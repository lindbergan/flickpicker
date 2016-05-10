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
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.MovieDAO;


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
    private TextView movieDescription;

    MovieDAO mMovieDAO;
    private long movieId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO: write setArguments in CommunityFragment
        mMovieDAO = App.getMovieDAO();
        Bundle bundle = getArguments();
        long id = bundle.getLong("movieId");
        movieId = id;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View movieDetailView = inflater.inflate(R.layout.activity_movie_detail, container, false);

        //TODO: add icons to Movies
        movieImage = (ImageView) getView().findViewById(R.id.movieDetailImageView);
        movieImage.setImageDrawable(null); //setImageIcon does not work due to API mismatch?

        //setting up text views
        movieTitle = (TextView) getView().findViewById(R.id.movieDetailTitleTextField);
        movieDescription = (TextView) getView().findViewById(R.id.descriptionTextField);
        ratingBar = (RatingBar) getView().findViewById(R.id.movieDetailRatingBar);

        //setting up button
        rateButton = (Button) getView().findViewById(R.id.movieDetailRateButton);

        //creates and saves new rating when rate button is clicked
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: finnish up method..
                //TODO: handle case where user presses button before choosing a rating
                long userId = App.getCurrentUser().getId();
                long movieId = 0;

                double numStars = (double) ratingBar.getNumStars();
                Rating newRating = new Rating(numStars, movieId, userId);

            }
        });


        //TEST
        setMovieTextFields();

        return movieDetailView;
    }

    public void setMovieTextFields(){

        //TODO how to get information (the movie object) from pressed movie cell (set/getArgument?)
        Movie movie = mMovieDAO.findMovie(movieId);
        movieTitle.setText(movie.getTitle());

    }


}
