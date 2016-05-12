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
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.sql.SQLPlaylistDAO;

import java.util.Iterator;
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
    private RatingBar ratingBar;
    private Button rateButton;



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
        setAddToPlaylistWidgets();
        setRateWidgets();

        movieImage.setImageDrawable(null); //setImageIcon does not work due to API mismatch?



        //TODO: clickListener or ratingBarChangedListener???
        //ensures that rate button is clickable if user wants to rate movie
        //or if user wants to update an rating
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRateButtonActive();
            }
        });




        //creates and saves new rating when rate button is clicked
        //if user have rated movie it does nothing
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hasUserSeenMovie()){
                    return;
                }else {

                    RatingDAO ratingDAO = App.getRatingDAO();
                    long userId = App.getCurrentUser().getId();

                    double numStars = (double) ratingBar.getNumStars();
                    Rating newRating = new Rating(numStars, movieId, userId);
                    ratingDAO.saveRating(newRating);
                }
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

        //setting up rate bar and button and add-to-playlist button
        addToWatchListButton = (Button) view.findViewById(R.id.movieDetailAddToPlaylistButton);
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


    //TODO: best way to decide if user have seen movie?


    /**
     * method that checks if the user have seen the movie or not
     * @return hasSeen true if user have seen movie, false if not
     */
    public boolean hasUserSeenMovie(){

        boolean hasSeen = false;
        List<Movie> userCollection = mMovieDAO.getUsersMovieCollection(10000, App.getCurrentUser().getId());
        Iterator<Movie> iterator = userCollection.iterator();

        while(iterator.hasNext()){
            Movie movie = iterator.next();
            if(movie.getId() == movieId){
                hasSeen = true;
                break;
            }
        }
        return hasSeen;
    }


    /**
     * method to get user's rating for specific movie
     * @return rating
     */
    public double getUserRating(){

        //save user id to compare against the movie's ratings
        long userId = App.getCurrentUser().getId();

        //creates list with movie's ratings and iterator
        double rating = -1;
        RatingDAO ratingDAO = App.getRatingDAO();
        List<Rating> ratings = ratingDAO.getMovieRatings(movieId);
        Iterator<Rating> iterator = ratings.iterator();

        //iterates list to see if any of the ratings is made by the active user
        //if so the rating is saved and the loop breaks
        while(iterator.hasNext()){
            Rating r = iterator.next();
            if(r.getUserId() == userId){
                rating = r.getRating();
                break;
            }
        }
        return rating;
    }

    //TODO: change Playlist so it just handles the one playlist
    public void setAddToPlaylistWidgets(){
        PlaylistDAO playlistDAO = App.getPlaylistDAO();

        //List<Movie> movies = playlistDAO.getUserPlaylist(App.getCurrentUser().getId());
        //Iterator<Movie> iterator = movies.iterator();
        //while(iterator.hasNext()){
        //....


    }

    /**
     * method used in onCreateView to set rating bar and button to correct states
     * depending on if the user have already rated the movie or not
     */
    public void setRateWidgets(){

        boolean seenMovie = hasUserSeenMovie();

        if(seenMovie){
            int rating = (int)getUserRating();
            ratingBar.setRating(rating);
            setRateButtonInactive();
        }else{
            ratingBar.setRating(0);
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
