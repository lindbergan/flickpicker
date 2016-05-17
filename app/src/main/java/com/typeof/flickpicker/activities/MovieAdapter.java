package com.typeof.flickpicker.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;

public class MovieAdapter extends CustomAdapter {

    private Context mContext;

    public MovieAdapter(Context context, Object[] obj) {
        super(context, obj);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Movie mov = (Movie) getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_community, parent, false);
            viewHolder.movieName = (TextView) convertView.findViewById(R.id.moviename_textview_movieCell);
            viewHolder.movieYear = (TextView) convertView.findViewById(R.id.movie_year_textview_movieCell);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar_movieCell);
            viewHolder.moviePoster = (ImageView) convertView.findViewById(R.id.imageView_movieCell);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                Bundle args = new Bundle();
                args.putLong("movieId", mov.getId());
                movieDetailFragment.setArguments(args);
                MainActivity mainActivity = (MainActivity) getContext();

                SingleFragmentHelper.setFragment(mainActivity, movieDetailFragment);
            }
        });


        viewHolder.movieName.setText(mov.getTitle());
        viewHolder.movieYear.setText(String.valueOf(mov.getYear()));
        viewHolder.ratingBar.setRating(Float.parseFloat(Double.toString(mov.getCommunityRating())));
        Picasso.with(getContext()).load(mov.getPoster()).into(viewHolder.moviePoster);
        return convertView;
    }

    private static class ViewHolder {

        TextView movieName;
        TextView movieYear;
        RatingBar ratingBar;
        ImageView moviePoster;
    }
}
