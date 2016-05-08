package com.typeof.flickpicker.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Rating;

public class RatingAdapter extends CustomAdapter {

    Object[] obj;

    public RatingAdapter(Context context, Object[] obj) {
        super(context, obj);
        this.obj = obj;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        TextView username = (TextView) customView.findViewById(R.id.username_textview);
        TextView moviename = (TextView) customView.findViewById(R.id.moviename_textview);
        TextView movieyear = (TextView) customView.findViewById(R.id.movie_year_textview);
        RatingBar ratingBar = (RatingBar) customView.findViewById(R.id.ratingBar);

        Rating r = (Rating) getItem(position);
        username.setText(App.getUserDAO().getUserById(r.getUserId()).getUsername());
        moviename.setText(App.getMovieDAO().findMovie(r.getMovieId()).getTitle());
        movieyear.setText(App.getMovieDAO().findMovie(r.getMovieId()).getYear()+"");
        ratingBar.setRating(Float.parseFloat(Double.toString(r.getRating())));
        return customView;
    }
}
