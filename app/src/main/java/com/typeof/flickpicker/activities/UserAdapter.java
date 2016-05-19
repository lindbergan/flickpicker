package com.typeof.flickpicker.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.User;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-10.
 */

public class UserAdapter extends CustomAdapter {

    private Context c;
    private boolean isFriend;

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final User user = (User) getItem(position);
        isFriend = App.getFriendDAO().isFriend(user.getId());
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_search_users, parent, false);
            viewHolder.profileIcon = (TextView) convertView.findViewById(R.id.image_profilepicture);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.username_textview);
            viewHolder.nrOfRatings = (TextView) convertView.findViewById(R.id.nr_of_ratings);
            viewHolder.nrOfPoints = (TextView) convertView.findViewById(R.id.nr_of_points);
            viewHolder.addFriendButton = (TextView) convertView.findViewById(R.id.button_add_friend);
            viewHolder.removeFriendButton = (TextView) convertView.findViewById(R.id.button_remove_friend);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Typeface tf = Typeface.createFromAsset(c.getAssets(), "fonts/fontawesome-webfont.ttf");
        viewHolder.profileIcon.setTypeface(tf);
        viewHolder.addFriendButton.setTypeface(tf);
        viewHolder.removeFriendButton.setTypeface(tf);
        viewHolder.userName.setText(user.getUsername());
        viewHolder.nrOfRatings.setText(String.valueOf(App.getMovieDAO().getUserRatings(1000, user.getId()).size()));
        viewHolder.nrOfPoints.setText(String.valueOf(user.getScore()));

        if (isFriend) {
            showRemoveButton(viewHolder);
        }
        else {
            showAddButton(viewHolder);
        }

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

        return convertView;
    }

    public void showAddButton(ViewHolder viewHolder) {
        viewHolder.addFriendButton.setVisibility(View.VISIBLE);
        viewHolder.removeFriendButton.setVisibility(View.INVISIBLE);
        viewHolder.addFriendButton.setClickable(true);
        viewHolder.removeFriendButton.setClickable(false);
    }

    public void showRemoveButton(ViewHolder viewHolder) {
        viewHolder.addFriendButton.setVisibility(View.INVISIBLE);
        viewHolder.removeFriendButton.setVisibility(View.VISIBLE);
        viewHolder.addFriendButton.setClickable(false);
        viewHolder.removeFriendButton.setClickable(true);
    }
}