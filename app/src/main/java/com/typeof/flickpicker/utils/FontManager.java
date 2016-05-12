package com.typeof.flickpicker.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * FlickPicker
 * Group 22
 * Created on 12/05/16.
 */
public class FontManager {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}