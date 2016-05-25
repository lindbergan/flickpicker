package com.typeof.flickpicker.application.fragments;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.helpers.RatingHelper;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.MovieDAO;

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
    private TextView friendsIcon;
    private TextView numOfFriendsSeen;
    private TextView communityIcon;
    private TextView communityRating;
    private TextView movieDescription;
    private Button addToWatchListButton;
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
        populateMovieFields();
        setAddToPlaylistWidgets();
        setRateWidgets();
        initListeners();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

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
        friendsIcon = (TextView) view.findViewById(R.id.movieDetailFriendsIcon);
        numOfFriendsSeen = (TextView) view.findViewById(R.id.movieDetailNumOfFriendsSeen);
        communityIcon = (TextView) view.findViewById(R.id.movieDetailCommunityIcon);
        communityRating = (TextView) view.findViewById(R.id.movieDetailCommunityRating);
        movieDescription = (TextView) view.findViewById(R.id.descriptionTextField);


        //setting up rate bar and button and add-to-playlist button
        addToWatchListButton = (Button) view.findViewById(R.id.movieDetailAddToPlaylistButton);
        ratingBar = (RatingBar) view.findViewById(R.id.movieDetailRatingBar);

        //setting up button
        rateButton = (Button) view.findViewById(R.id.movieDetailRateButton);

    }

    public void initListeners() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRateButtonActive();
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RatingHelper.createNewRating(ratingBar.getRating(), movieId, App.getCurrentUser().getId());
                //App.getRatingDAO().saveRating(new Rating(ratingBar.getRating(), movieId, App.getCurrentUser().getId()));
                setRateButtonInactive();
            }
        });
    }

    /**
     * method for adding the information about the selected movie to the correct text views
     */
    public void populateMovieFields(){

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");

        Movie movie = mMovieDAO.findMovie(movieId);
        movieTitle.setText(movie.getTitle());
        movieGenre.setText(movie.getGenre());

        friendsIcon.setTypeface(font);
        int numSeen = mMovieDAO.numOfFriendsHasSeenMovie(movieId, App.getCurrentUser().getId());
        numOfFriendsSeen.setText(String.format("%s friends have seen this", String.valueOf(numSeen)));

        communityIcon.setTypeface(font);
        double rating = movie.getCommunityRating();
        communityRating.setText(String.format("rated %s by the community", String.valueOf(rating)));
        movieDescription.setText(movie.getDescription());
        Picasso.with(getContext()).load(movie.getPoster()).into(movieImage);
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
