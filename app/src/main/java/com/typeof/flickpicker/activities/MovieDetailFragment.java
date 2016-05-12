package com.typeof.flickpicker.activities;

import android.annotation.SuppressLint;
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
import com.typeof.flickpicker.database.RatingDAO;


/**
 * FlickPicker
 * Group 22
 * Created on 09/05/16.
 */
public class MovieDetailFragment extends Fragment {

    private ImageView movieImage;
    private TextView movieTitle;
    private TextView movieGenre;
    private TextView numOfFriendsSeen;
    private TextView communityRating;
    private RatingBar ratingBar;
    private Button rateButton;
    private TextView movieDescription;

    MovieDAO mMovieDAO;
    long movieId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //should this code be in onCreateView?
        //TODO: write setArguments in CommunityFragment
        mMovieDAO = App.getMovieDAO();
        Bundle bundle = getArguments();
        movieId = bundle.getLong("movieId");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View movieDetailView = inflater.inflate(R.layout.activity_movie_detail, container, false);

        hookUpViews(movieDetailView);
        setMovieTextFields();
        movieImage.setImageDrawable(null); //setImageIcon does not work due to API mismatch?

        boolean seenMovie = hasUserRatedMovie();

        if(seenMovie){
            int rating;
        }

        //creates and saves new rating when rate button is clicked
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RatingDAO ratingDAO = App.getRatingDAO();
                //TODO: finnish up method..
                //TODO: handle case where user presses button before choosing a rating
                long userId = App.getCurrentUser().getId();

                double numStars = (double) ratingBar.getNumStars();
                Rating newRating = new Rating(numStars, movieId, userId);
                ratingDAO.saveRating(newRating);

            }
        });

        return movieDetailView;
    }

    /**
     * method to assign variables to their different combonents in the layout.
     */
    public void hookUpViews(View view){

        //TODO: add icons to Movies
        //setting up image view
        movieImage = (ImageView) view.findViewById(R.id.movieDetailImageView);

        //setting up text views
        movieTitle = (TextView) view.findViewById(R.id.movieDetailTitleTextField);
        movieGenre = (TextView) view.findViewById(R.id.movieDetailGenreTextField);
        numOfFriendsSeen = (TextView) view.findViewById(R.id.movieDetailNumOfFriendsSeen);
        communityRating = (TextView) view.findViewById(R.id.movieDetailCommunityRating);
        movieDescription = (TextView) view.findViewById(R.id.descriptionTextField);
        ratingBar = (RatingBar) view.findViewById(R.id.movieDetailRatingBar);

        //setting up button
        rateButton = (Button) view.findViewById(R.id.movieDetailRateButton);

    }

    /**
     * method for adding the information about the selected movie to the correct text views
     */
    @SuppressLint("SetTextI18n")
    public void setMovieTextFields(){

        Movie movie = mMovieDAO.findMovie(movieId);
        movieTitle.setText(movie.getTitle());
        movieGenre.setText("Genre:    " + movie.getGenre());

        //TODO: improve exception handling?
        try {
            numOfFriendsSeen.setText("" + mMovieDAO.numOfFriendsHasSeenMovie(movieId, App.getCurrentUser().getId()));
        }catch(NullPointerException e){
            numOfFriendsSeen.setText("n/a");
        }

        communityRating.setText("" + movie.getCommunityRating());

        //TODO: handle descriptions with amount of text that does not fit text view?
        movieDescription.setText(movie.getDescription());

    }

    public boolean hasUserRatedMovie(){

        //TODO: best way to decide if user have seen movie?


        return false;
    }


}
