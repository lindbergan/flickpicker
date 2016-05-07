package com.typeof.flickpicker.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-07.
 */
public class MetaData {
    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        } catch (PackageManager.NameNotFoundException e ) {
            Log.e("Meta Data Error", "Unable to load meta-data: " + e .getMessage());
        }
        return null;
    }
}
