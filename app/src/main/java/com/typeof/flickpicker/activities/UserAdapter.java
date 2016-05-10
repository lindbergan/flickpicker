package com.typeof.flickpicker.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.User;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-10.
 */
public class UserAdapter extends CustomAdapter {

    Object[] obj;

    public UserAdapter(Context context, Object[] obj) {
        super(context, obj);
        this.obj = obj;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row_search_users, parent, false);

        TextView userName = (TextView) customView.findViewById(R.id.username_textview);
        TextView userScore = (TextView) customView.findViewById(R.id.userScore_textview);

        User user = (User) getItem(position);
        userName.setText(user.getUsername());
        userScore.setText(String.valueOf(user.getScore()) + "(icon/idiom)");
        return customView;
    }
}
