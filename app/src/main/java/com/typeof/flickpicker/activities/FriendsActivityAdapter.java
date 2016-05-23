package com.typeof.flickpicker.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.MovieDAO;

/**
 * FriendsActivityAdapter extends CustomAdapter
 * Used to set the values to each row of recent events
 */


public class FriendsActivityAdapter extends CustomAdapter {

    Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");

    public FriendsActivityAdapter(Context context, Object[] obj) {
        super(context, obj);
    }

    /**
     * Private static class ViewHolder
     * Used for caching values
     */

    private static class ViewHolder {
        TextView username;
        TextView movieName;
        TextView movieYear;
        RatingBar ratingBar;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Rating r = (Rating) getItem(position);

        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_row_friend_activity, parent, false);

            viewHolder.username = (TextView) view.findViewById(R.id.username_textview);
            viewHolder.movieName = (TextView) view.findViewById(R.id.moviename_textview);
            viewHolder.movieYear = (TextView) view.findViewById(R.id.movie_year_textview);
            viewHolder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        MovieDAO movieDAO = App.getMovieDAO();

        TextView textView = (TextView) view.findViewById(R.id.no_profile_picture);
        textView.setTypeface(font);

        viewHolder.username.setText(App.getUserDAO().getUserById(r.getUserId()).getUsername());
        viewHolder.movieName.setText(movieDAO.findMovie(r.getMovieId()).getTitle());
        viewHolder.movieYear.setText(String.format(" (%s)", String.valueOf(movieDAO.findMovie(r.getMovieId()).getYear())));
        viewHolder.ratingBar.setRating(Float.parseFloat(Double.toString(r.getRating())));

        return view;
    }
}