package com.typeof.flickpicker.utils;

import android.util.Log;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-10.
 */
public class ExecutionTimeLogger {
    private long startNow;
    private long endNow;

    public void startTimer() {
        startNow = android.os.SystemClock.uptimeMillis();
    }

    public void stopTimer() {
        endNow = android.os.SystemClock.uptimeMillis();
    }

    public void stopTimerAndLogResults() {
        stopTimer();
        Log.d("EXECUTION TIME", "Execution time: " + getExecutionTime());
    }

    public long getExecutionTime() {
        return endNow - startNow;
    }
}
