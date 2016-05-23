package com.typeof.flickpicker.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;


/**
 * MovieAdapter extends CustomAdapter
 * Used to define list items in lists that show movies objects
 */

public class MovieAdapter extends CustomAdapter {

    Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");

    public MovieAdapter(Context context, Object[] obj) {
        super(context, obj);
    }

    /**
     * Private static class ViewHolder
     * Used for caching values
     */
    private static class ViewHolder {

        TextView movieName;
        TextView movieGenre;
        TextView movieYear;
        TextView friendsIcon;
        TextView friendsText;
        TextView communityIcon;
        TextView communityText;
        ImageView moviePoster;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        final Movie movie = (Movie) getItem(position);

        if (view == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_row_community, parent, false);

            viewHolder.movieName = (TextView) view.findViewById(R.id.rowCommunityMovieTitle);
            viewHolder.movieGenre = (TextView) view.findViewById(R.id.rowCommunityGenre);
            viewHolder.movieYear = (TextView) view.findViewById(R.id.rowCommunityYear);
            viewHolder.friendsIcon = (TextView) view.findViewById(R.id.rowCommunityFriendsIcon);
            viewHolder.friendsText = (TextView) view.findViewById(R.id.rowCommunityFriendsText);
            viewHolder.communityIcon = (TextView) view.findViewById(R.id.rowCommunityCommunityIcon);
            viewHolder.communityText = (TextView) view.findViewById(R.id.rowCommunityCommunityText);
            viewHolder.moviePoster = (ImageView) view.findViewById(R.id.rowCommunityImageView);
            view.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) view.getTag();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                Bundle args = new Bundle();
                args.putLong("movieId", movie.getId());
                movieDetailFragment.setArguments(args);
                MainActivity mainActivity = (MainActivity) getContext();

                KeyboardHelper.hideSoftKeyboard(mainActivity);

                SingleFragmentHelper.setFragment(mainActivity, movieDetailFragment);
            }
        });

        viewHolder.movieName.setText(movie.getTitle());
        viewHolder.movieGenre.setText(movie.getGenre());
        viewHolder.movieYear.setText(String.valueOf(movie.getYear()));

        viewHolder.friendsIcon.setTypeface(font);
        int numOfFriendsSeen = App.getMovieDAO().getFriendsSeenMovie(movie.getId(), App.getCurrentUser().getId()).size();
        viewHolder.friendsText.setText(String.valueOf(numOfFriendsSeen));

        viewHolder.communityIcon.setTypeface(font);
        double communityRating = round(movie.getCommunityRating(), 1);
        viewHolder.communityText.setText(String.valueOf(communityRating));

        Picasso.with(getContext()).load(movie.getPoster()).into(viewHolder.moviePoster);
        return view;
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
