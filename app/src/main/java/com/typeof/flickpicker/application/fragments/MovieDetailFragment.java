package com.typeof.flickpicker.application.fragments;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.helpers.DataObservable;
import com.typeof.flickpicker.application.helpers.RatingHelper;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * MovieDetailFragment extends Fragment
 * Used for showing a detailed view of a mMovie
 */

public class MovieDetailFragment extends Fragment {

    private ImageView mMovieImage;
    private TextView mMovieTitle;
    private TextView mMovieGenre;
    private TextView mFriendsIcon;
    private TextView mNumOfFriendsSeen;
    private TextView mCommunityIcon;
    private TextView mCommunityRating;
    private TextView mMovieDescription;
    private TextView mMovieDetailAddToPlaylistLabel;
    private ToggleButton mAddToWatchListButton;
    private RatingBar mRatingBar;
    private Button mRateButton;
    private User mUser;
    private PlaylistDAO mPlaylistDAO;
    final private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final MovieDAO mMovieDAO = App.getMovieDAO();
    private long movieId;
    private Movie mMovie;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        movieId = bundle.getLong("movieId");
        mMovie = App.getMovieDAO().findMovie(movieId);
        mPlaylistDAO = App.getPlaylistDAO();
        mUser = App.getCurrentUser();
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
     * method to assign variables to their different components in the layout file.
     */
    private void hookUpViews(View view){

        mMovieImage = (ImageView) view.findViewById(R.id.movieDetailImageView);

        mMovieTitle = (TextView) view.findViewById(R.id.movieDetailTitleTextField);
        mMovieGenre = (TextView) view.findViewById(R.id.movieDetailGenreTextField);
        mFriendsIcon = (TextView) view.findViewById(R.id.movieDetailFriendsIcon);
        mNumOfFriendsSeen = (TextView) view.findViewById(R.id.movieDetailNumOfFriendsSeen);
        mCommunityIcon = (TextView) view.findViewById(R.id.movieDetailCommunityIcon);
        mCommunityRating = (TextView) view.findViewById(R.id.movieDetailCommunityRating);
        mMovieDescription = (TextView) view.findViewById(R.id.descriptionTextField);

        mMovieDetailAddToPlaylistLabel = (TextView) view.findViewById(R.id.movieDetailAddToPlaylistLabel);
        mAddToWatchListButton = (ToggleButton) view.findViewById(R.id.movieDetailAddToPlaylistButton);
        mRatingBar = (RatingBar) view.findViewById(R.id.movieDetailRatingBar);
        mRateButton = (Button) view.findViewById(R.id.movieDetailRateButton);

    }



    /**
     * method for adding the information about the selected mMovie to the correct text views
     */
    private void populateMovieFields(){

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");

        Picasso.with(getContext()).load(mMovie.getPoster()).into(mMovieImage);
        mMovieTitle.setText(mMovie.getTitle());
        mMovieGenre.setText(mMovie.getGenre());

        mFriendsIcon.setTypeface(font);
        int numSeen = mMovieDAO.numOfFriendsHasSeenMovie(movieId, App.getCurrentUser().getId());
        mNumOfFriendsSeen.setText(String.format("%s friends have seen this", String.valueOf(numSeen)));

        mCommunityIcon.setTypeface(font);

        double rating = mMovie.getCommunityRating();
        mCommunityRating.setText(String.format("rated %s by the community", String.valueOf(round(rating, 1))));
        mMovieDescription.setText(mMovie.getDescription());



    }



    /**
     * method that sets the status of 'add to playlist' widgets to their correct state
     * depending on if the mMovie is on the user's playlist or not
     */
    private void setAddToPlaylistWidgets() {

        mAddToWatchListButton.setTextOff("+");
        mAddToWatchListButton.setTextOn("-");

        if (mPlaylistDAO.isMovieOnPlaylist(mUser, mMovie)) {
            mAddToWatchListButton.setChecked(true);
            mMovieDetailAddToPlaylistLabel.setText(R.string.remove_movie_from_playlist);
        }else{
            mAddToWatchListButton.setChecked(false);
            mMovieDetailAddToPlaylistLabel.setText(R.string.add_movie_to_playlist);
        }
    }



    /**
     * method used in onCreateView to set rating bar and button to correct states
     * depending on if the user have already rated the mMovie or not
     */
    private void setRateWidgets(){

        if(hasUserSeenMovie()){
            setRateButtonInactive();
            mRatingBar.setRating((float) App.getRatingDAO().getRatingFromUser(App.getCurrentUser().getId(), movieId));
        }
        else{
            setRateButtonActive();
        }
    }



    /**
     * method for initiating the Fragment's various listeners
     */
    private void initListeners() {



        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRateButtonActive();
            }
        });

        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if mMovie is on playlist it is removed
                if (mPlaylistDAO.isMovieOnPlaylist(mUser, mMovie)) {
                    App.getPlaylistDAO().removeMovieFromPlaylist(App.getCurrentUser(), mMovie);
                    mAddToWatchListButton.setChecked(false);
                }

                RatingHelper.createNewRating(mRatingBar.getRating(), movieId, App.getCurrentUser().getId());

                App.getEventBus().triggerEvent("ratings_changed");
                setRateButtonInactive();
            }
        });

        mAddToWatchListButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                PlaylistDAO playlistDAO = App.getPlaylistDAO();

                if (isChecked && !mPlaylistDAO.isMovieOnPlaylist(mUser,mMovie)) {
                    playlistDAO.addMovieToPlaylist((App.getCurrentUser()), mMovie);
                    setAddToWatchListLabel();
                } else {
                    playlistDAO.removeMovieFromPlaylist(App.getCurrentUser(), mMovie);
                    setAddToWatchListLabel();
                }

                App.getEventBus().triggerEvent("playlist_changed");

            }
        });

    }


    /**
     * method that checks if the user have seen the mMovie or not
     * @return hasSeen true if user have seen mMovie, false if not
     */
    private boolean hasUserSeenMovie(){
        List<Movie> userCollection = mMovieDAO.getMovieCollectionFromUserId(10000, App.getCurrentUser().getId());

        for (Movie movie : userCollection) {
            if (movie.getId() == movieId) {
                return true;
            }
        }
        return false;
    }

    /**
     * method that sets 'add to watchlist' label to specific string depending on the
     * toggle button's state
     */
    private void setAddToWatchListLabel() {
        if (mAddToWatchListButton.isChecked()) {
            mMovieDetailAddToPlaylistLabel.setText(R.string.remove_movie_from_playlist);
        }else{
            mMovieDetailAddToPlaylistLabel.setText(R.string.add_movie_to_playlist);
        }
    }


    /**
     * method to activate rate button and change its appearance
     */
    private void setRateButtonActive(){
        mRateButton.setBackgroundResource(R.drawable.test_button_round_corners);
        mRateButton.setClickable(true);
    }



    /**
     * method to inactivate rate button and change its appearance
     */
    private void setRateButtonInactive(){
        mRateButton.setBackgroundResource(R.drawable.button_inactive);
        mRateButton.setClickable(false);
    }

    /**
     * help method for rounding of double values
     *
     * @param value the double value to be rounded
     * @param precision number of desired decimals
     * @return value rounded to chosen amount of decimals
     */
    private double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}



