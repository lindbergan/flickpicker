package com.typeof.flickpicker.activities;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;

import org.w3c.dom.Text;

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
    private TextView movieDetailAddToPlaylistLabel;
    private ToggleButton addToWatchListButton;

    private RatingBar ratingBar;
    private Button rateButton;

    private MovieDAO mMovieDAO = App.getMovieDAO();
    private long movieId;
    private Movie movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        movieId = bundle.getLong("movieId");
        movie = App.getMovieDAO().findMovie(movieId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View movieDetailView = inflater.inflate(R.layout.activity_movie_detail, container, false);

        hookUpViews(movieDetailView);
        populateMovieFields();
        setAddToPlaylistWidgets();
        setRateWidgets();
        initListeners();

        return movieDetailView;
    }

    /**
     * method to assign variables to their different components in the layout.
     */
    public void hookUpViews(View view){

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
        movieDetailAddToPlaylistLabel = (TextView) view.findViewById(R.id.movieDetailAddToPlaylistLabel);
        addToWatchListButton = (ToggleButton) view.findViewById(R.id.movieDetailAddToPlaylistButton);
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
                RatingDAO ratingDAO = App.getRatingDAO();
                ratingDAO.saveRating(new Rating(ratingBar.getRating(), movieId, App.getCurrentUser().getId()));
                setRateButtonInactive();
                //TODO: remove from playlist
            }
        });


        addToWatchListButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                PlaylistDAO playlistDAO = App.getPlaylistDAO();
                if (isChecked) {
                    playlistDAO.addMovieToPlaylist((App.getCurrentUser()), movie);
                    toggleAddToPlaylistButton();
                } else {
                    playlistDAO.removeMovieFromPlaylist(App.getCurrentUser(), movie);
                    toggleAddToPlaylistButton();
                }
            }
        });


    }


    private void toggleAddToPlaylistButton() {
        if (addToWatchListButton.isChecked()) {
            movieDetailAddToPlaylistLabel.setText("Remove from playlist");
        }else{
            movieDetailAddToPlaylistLabel.setText("Add to playlist");
        }
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
        numOfFriendsSeen.setText(String.valueOf(numSeen) + " friends have seen this");

        communityIcon.setTypeface(font);
        double rating = movie.getCommunityRating();
        communityRating.setText("rated " + String.valueOf(rating) + " by the community");

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

    public boolean isMovieOnPlaylist() {

        PlaylistDAO playlistDAO = App.getPlaylistDAO();
        Playlist playlist = playlistDAO.getUserPlaylist(App.getCurrentUser().getId());
        List<Number> movies = playlist.getMovieIds();
        boolean isOnWatchList = false;
        for (Number movie : movies){
            long id = movie.longValue();
            if(id == movieId){
                isOnWatchList = true;
                break;
            }
        }
        return isOnWatchList;
    }

    public void setAddToPlaylistWidgets() {

        addToWatchListButton.setTextOff("+");
        addToWatchListButton.setTextOn("-");

        if (isMovieOnPlaylist()) {
            addToWatchListButton.setChecked(true);
            movieDetailAddToPlaylistLabel.setText("Remove from playlist");
        }else{
            addToWatchListButton.setChecked(false);
            movieDetailAddToPlaylistLabel.setText("Add to playlist");
        }
    }






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
