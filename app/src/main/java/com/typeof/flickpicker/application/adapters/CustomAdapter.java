package com.typeof.flickpicker.application.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * Abstract class CustomAdapter extends ArrayAdapter
 * Is inherited to ensure that other adapters uses getView method.
 */

abstract class CustomAdapter extends ArrayAdapter {

    CustomAdapter(Context context, Object[] objects) {
        //noinspection unchecked
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    abstract public View getView(int position, View convertView, ViewGroup parent);
}
