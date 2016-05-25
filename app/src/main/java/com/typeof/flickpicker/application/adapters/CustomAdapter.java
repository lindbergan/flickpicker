package com.typeof.flickpicker.application.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * Abstract class CustomAdapter extends ArrayAdapter
 * Is inherited to ensure that other adapters uses getView method.
 */

public abstract class CustomAdapter extends ArrayAdapter {

    public CustomAdapter(Context context, Object[] objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    abstract public View getView(int position, View convertView, ViewGroup parent);
}
