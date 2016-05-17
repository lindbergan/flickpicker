package com.typeof.flickpicker.activities;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
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
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.utils.RatingHelper;

import java.util.List;

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
    private TextView movieDescription;
    private Button addToWatchListButton;
    private Button backBtn;
    private RatingBar ratingBar;
    private Button rateButton;

    private MovieDAO mMovieDAO = App.getMovieDAO();
    private long movieId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        movieId = bundle.getLong("movieId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View movieDetailView = inflater.inflate(R.layout.activity_movie_detail, container, false);

        hookUpViews(movieDetailView);
        setMovieTextFields();
        setAddToPlaylistWidgets();
        setRateWidgets();
        initListeners();

        return movieDetailView;
    }

    /**
     * method to assign variables to their different components in the layout.
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

        //setting up rate bar and button and add-to-playlist button
        addToWatchListButton = (Button) view.findViewById(R.id.movieDetailAddToPlaylistButton);
        ratingBar = (RatingBar) view.findViewById(R.id.movieDetailRatingBar);

        //setting up button
        rateButton = (Button) view.findViewById(R.id.movieDetailRateButton);
        backBtn = (Button)view.findViewById(R.id.backBtn);

    }

    public void initListeners() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRateButtonActive();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.onBackPressed();
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              RatingDAO ratingDAO = App.getRatingDAO();
              ratingDAO.saveRating(new Rating(ratingBar.getRating(), movieId, App.getCurrentUser().getId()));
              setRateButtonInactive();
          }
      });
    }

    /**
     * method for adding the information about the selected movie to the correct text views
     */
    public void setMovieTextFields(){

        Movie movie = mMovieDAO.findMovie(movieId);
        movieTitle.setText(movie.getTitle());
        movieGenre.setText(movie.getGenre());
        numOfFriendsSeen.setText(String.valueOf(mMovieDAO.numOfFriendsHasSeenMovie(movieId, App.getCurrentUser().getId())));
        communityRating.setText(String.valueOf(movie.getCommunityRating()));
        movieDescription.setText(movie.getDescription());

    }

    /**
     * method that checks if the user have seen the movie or not
     * @return hasSeen true if user have seen movie, false if not
     */
    public boolean hasUserSeenMovie(){
        List<Movie> userCollection = mMovieDAO.getMovieCollectionFromUserId(10000, App.getCurrentUser().getId());

        for (Movie movie : userCollection) {
            if (movie.getId() == movieId) {
                return true;
            }
        }
        return false;
    }

    public void setAddToPlaylistWidgets(){}

    /**
     * method used in onCreateView to set rating bar and button to correct states
     * depending on if the user have already rated the movie or not
     */
    public void setRateWidgets(){

        if(hasUserSeenMovie()){
            setRateButtonInactive();
            ratingBar.setRating((float) App.getRatingDAO().getRatingFromUser(App.getCurrentUser().getId(), movieId));
        }
        else{
            setRateButtonActive();
        }
    }
    /**
     * method to activate rate button and change its appearance
     */
    public void setRateButtonActive(){
        rateButton.setBackgroundResource(R.drawable.test_button_round_corners);
        rateButton.setClickable(true);
    }

    /**
     * method to inactivate rate button and change its appearance
     */
    public void setRateButtonInactive(){
        rateButton.setBackgroundResource(R.drawable.button_inactive);
        rateButton.setClickable(false);
    }




}
