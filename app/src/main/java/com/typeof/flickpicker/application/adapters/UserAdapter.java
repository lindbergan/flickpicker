package com.typeof.flickpicker.application.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.User;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-10.
 */

/**
 * UserAdapter extends CustomAdapter
 * Used for displaying users
 */

public class UserAdapter extends CustomAdapter {

    private final Context c;

    public UserAdapter(Context context, Object[] obj) {
        super(context, obj);
        c = context;
    }

    private static class ViewHolder {
        TextView profileIcon;
        TextView userName;
        TextView nrOfRatings;
        TextView nrOfPoints;
        TextView addFriendButton;
        TextView removeFriendButton;
        TextView friends;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final User user = (User) getItem(position);
        boolean isFriend = App.getFriendDAO().isFriend(user.getId());
        final ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_row_search_users, parent, false);

            viewHolder.profileIcon = (TextView) view.findViewById(R.id.image_profilepicture);
            viewHolder.userName = (TextView) view.findViewById(R.id.username_textview);
            viewHolder.nrOfRatings = (TextView) view.findViewById(R.id.nr_of_ratings);
            viewHolder.nrOfPoints = (TextView) view.findViewById(R.id.nr_of_points);
            viewHolder.addFriendButton = (TextView) view.findViewById(R.id.button_add_friend);
            viewHolder.removeFriendButton = (TextView) view.findViewById(R.id.button_remove_friend);
            viewHolder.friends = (TextView) view.findViewById(R.id.amount_of_friends);

            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        // Typeface is used to set the image icons

        int nrOfRatings = App.getRatingDAO().getAllRatingsFromUser(user.getId()).size();

        Typeface tf = Typeface.createFromAsset(c.getAssets(), "fonts/fontawesome-webfont.ttf");
        viewHolder.profileIcon.setTypeface(tf);
        viewHolder.addFriendButton.setTypeface(tf);
        viewHolder.removeFriendButton.setTypeface(tf);
        viewHolder.userName.setText(user.getUsername());
        viewHolder.nrOfRatings.setText(String.valueOf(nrOfRatings));
        viewHolder.nrOfPoints.setText(String.valueOf(user.getScore()));
        viewHolder.friends.setText(String.valueOf(App.getFriendDAO().getFriendsFromUserId(user.getId()).size()));
        TextView ratings = (TextView) view.findViewById(R.id.ratings);

        if (nrOfRatings == 1) {
            ratings.setText("rating");
        }
        else {
            ratings.setText("ratings");
        }

        if (isFriend) {
            showRemoveButton(viewHolder);
        }
        else {
            showAddButton(viewHolder);
        }

        // Displays a short message using Toasts to indicate that a user has been added / removed

        viewHolder.addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getFriendDAO().addFriend(new Friend(App.getCurrentUser().getId(), user.getId()));
                showRemoveButton(viewHolder);
                Toast.makeText(UserAdapter.this.getContext(), "Friend Added", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.removeFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getFriendDAO().removeFriend(App.getCurrentUser().getId(), user.getId());
                showAddButton(viewHolder);
                Toast.makeText(UserAdapter.this.getContext(), "Friend Removed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showAddButton(ViewHolder viewHolder) {
        viewHolder.addFriendButton.setVisibility(View.VISIBLE);
        viewHolder.removeFriendButton.setVisibility(View.INVISIBLE);
        viewHolder.addFriendButton.setClickable(true);
        viewHolder.removeFriendButton.setClickable(false);
    }

    private void showRemoveButton(ViewHolder viewHolder) {
        viewHolder.addFriendButton.setVisibility(View.INVISIBLE);
        viewHolder.removeFriendButton.setVisibility(View.VISIBLE);
        viewHolder.addFriendButton.setClickable(false);
        viewHolder.removeFriendButton.setClickable(true);
    }
}