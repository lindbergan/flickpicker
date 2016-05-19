package com.typeof.flickpicker.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
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

import org.w3c.dom.Text;

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
            viewHolder.movieGenre = (TextView) convertView.findViewById(R.id.rowCommunityGenre);
            viewHolder.movieYear = (TextView) convertView.findViewById(R.id.rowCommunityYear);
            viewHolder.friendsIcon = (TextView) convertView.findViewById(R.id.rowCommunityFriendsIcon);
            viewHolder.friendsText = (TextView) convertView.findViewById(R.id.rowCommunityFriendsText);
            viewHolder.communityIcon = (TextView) convertView.findViewById(R.id.rowCommunityCommunityIcon);
            viewHolder.communityText = (TextView) convertView.findViewById(R.id.rowCommunityCommunityText);
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

                KeyboardHelper.hideSoftKeyboard(mainActivity);

                SingleFragmentHelper.setFragment(mainActivity, movieDetailFragment);
            }
        });

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/fontawesome-webfont.ttf");

        viewHolder.movieName.setText(mov.getTitle());

        viewHolder.movieGenre.setText(mov.getGenre());
        viewHolder.movieYear.setText(String.valueOf(mov.getYear()));

        viewHolder.friendsIcon.setTypeface(font);
        int numOfFriendsSeen = App.getMovieDAO().getFriendsSeenMovie(mov.getId(), App.getCurrentUser().getId()).size();
        viewHolder.friendsText.setText(String.valueOf(numOfFriendsSeen) + " friends have seen this");

        viewHolder.communityIcon.setTypeface(font);
        double communityRating = round(mov.getCommunityRating(), 1);
        viewHolder.communityText.setText("rated " + String.valueOf(communityRating) + " by the users");

        Picasso.with(getContext()).load(mov.getPoster()).into(viewHolder.moviePoster);
        return convertView;
    }


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
