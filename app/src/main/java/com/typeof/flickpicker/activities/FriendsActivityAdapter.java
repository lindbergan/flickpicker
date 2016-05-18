package com.typeof.flickpicker.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Rating;

public class FriendsActivityAdapter extends CustomAdapter {

    public FriendsActivityAdapter(Context context, Object[] obj) {
        super(context, obj);
    }

    private static class ViewHolder {
        TextView username;
        TextView movieName;
        TextView movieYear;
        RatingBar ratingBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Rating r = (Rating) getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.custom_row_friend_activity, parent, false);
            viewHolder.username = (TextView) convertView.findViewById(R.id.username_textview);
            viewHolder.movieName = (TextView) convertView.findViewById(R.id.moviename_textview);
            viewHolder.movieYear = (TextView) convertView.findViewById(R.id.movie_year_textview);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(App.getUserDAO().getUserById(r.getUserId()).getUsername());
        viewHolder.movieName.setText(App.getMovieDAO().findMovie(r.getMovieId()).getTitle());
        viewHolder.movieYear.setText(" (" + String.valueOf(App.getMovieDAO().findMovie(r.getMovieId()).getYear()) + ")");
        viewHolder.ratingBar.setRating(Float.parseFloat(Double.toString(r.getRating())));
        Drawable d = viewHolder.ratingBar.getProgressDrawable();
        DrawableCompat.setTint(d, Color.rgb(238, 216, 23));
        return convertView;
    }
}