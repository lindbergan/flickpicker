package com.typeof.flickpicker.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;

public class MovieAdapter extends CustomAdapter {

    Object[] obj;
    LayoutInflater inflater;

    public MovieAdapter(Context context, Object[] obj) {
        super(context, obj);
        inflater = LayoutInflater.from(getContext());
        this.obj = obj;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View customView = inflater.inflate(R.layout.custom_row_community, parent, false);

        TextView movieName = (TextView) customView.findViewById(R.id.moviename_textview_movieCell);
        TextView movieYear = (TextView) customView.findViewById(R.id.movie_year_textview_movieCell);
        RatingBar ratingBar = (RatingBar) customView.findViewById(R.id.ratingBar_movieCell);

        Movie mov = (Movie) getItem(position);
        movieName.setText(mov.getTitle());
        movieYear.setText(String.valueOf(mov.getYear()));
        ratingBar.setRating(Float.parseFloat(Double.toString(mov.getCommunityRating())));
        return customView;
    }
}
