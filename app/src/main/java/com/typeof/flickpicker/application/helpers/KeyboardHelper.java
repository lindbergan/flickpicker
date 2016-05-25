package com.typeof.flickpicker.application.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

/**
 * KeyboardHelper
 *
 * Helpers for closing the device keyboard when something other than the
 * input bar or the keyboard is pressed.
 */
public class KeyboardHelper {

    private Activity mActivity;
    private Context ctx;

    public KeyboardHelper(Activity activity, Context ctx) {
        this.ctx = ctx;
        this.mActivity = activity;
    }

    /**
     * Checks the view for View objects. If an object is not an EditText view, then
     * an touch listener is added. The method uses a recursive stratergy to work it's
     * way down inside the view hierarchy.
     *
     * @param view  View to check
     */
    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(mActivity, ctx);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public void hideKeyboard(Activity activity, Context ctx) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
