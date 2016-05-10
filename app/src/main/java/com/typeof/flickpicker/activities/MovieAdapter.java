package com.typeof.flickpicker.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;

public class MovieAdapter extends CustomAdapter {

    public MovieAdapter(Context context, Object[] obj) {
        super(context, obj);
    }

    private static class ViewHolder {
        TextView movieName;
        TextView movieYear;
        RatingBar ratingBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie mov = (Movie) getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.custom_row_community, parent, false);
            viewHolder.movieName = (TextView) convertView.findViewById(R.id.moviename_textview_movieCell);
            viewHolder.movieYear = (TextView) convertView.findViewById(R.id.movie_year_textview_movieCell);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar_movieCell);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.movieName.setText(mov.getTitle());
        viewHolder.movieYear.setText(String.valueOf(mov.getYear()));
        viewHolder.ratingBar.setRating(Float.parseFloat(Double.toString(mov.getCommunityRating())));
        return convertView;
    }
}
