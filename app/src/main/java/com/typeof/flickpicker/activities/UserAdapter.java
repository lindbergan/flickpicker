package com.typeof.flickpicker.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.User;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-10.
 */

public class UserAdapter extends CustomAdapter {

    public UserAdapter(Context context, Object[] obj) {
        super(context, obj);
    }

    private static class ViewHolder {
        TextView userName;
        TextView userScore;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        User user = (User) getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_search_users, parent, false);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.username_textview);
            viewHolder.userScore = (TextView) convertView.findViewById(R.id.userScore_textview);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userName.setText(user.getUsername());
        viewHolder.userScore.setText(String.valueOf(user.getScore()) + "(icon/idiom)");
        return convertView;
    }
}