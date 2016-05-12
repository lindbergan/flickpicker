package com.typeof.flickpicker.activities;

import android.content.Context;
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
        TextView moviename;
        TextView movieyear;
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
            viewHolder.moviename = (TextView) convertView.findViewById(R.id.moviename_textview);
            viewHolder.movieyear = (TextView) convertView.findViewById(R.id.movie_year_textview);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(App.getUserDAO().getUserById(r.getUserId()).getUsername());
        viewHolder.moviename.setText(App.getMovieDAO().findMovie(r.getMovieId()).getTitle());
        viewHolder.movieyear.setText("(" + String.valueOf(App.getMovieDAO().findMovie(r.getMovieId()).getYear()) + ")");
        viewHolder.ratingBar.setRating(Float.parseFloat(Double.toString(r.getRating())));
        return convertView;
    }
}