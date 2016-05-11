package com.typeof.flickpicker.activities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.typeof.flickpicker.R;

public abstract class CustomAdapter extends ArrayAdapter {

    public CustomAdapter(Context context, Object[] obj) {
        super(context, R.layout.custom_row_rating, obj);
    }

    abstract public View getView(int position, View convertView, ViewGroup parent);
}
