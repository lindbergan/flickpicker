package com.typeof.flickpicker.utils;

import android.util.Log;

/**
 * ExecutionTimeLogger
 * Helper class for measuring execution times
 */
class ExecutionTimeLogger {
    private long startNow;
    private long endNow;

    public void startTimer() {
        startNow = android.os.SystemClock.uptimeMillis();
    }

    private void stopTimer() {
        endNow = android.os.SystemClock.uptimeMillis();
    }

    public void stopTimerAndLogResults() {
        stopTimer();
        Log.d("EXECUTION TIME", "Execution time: " + getExecutionTime());
    }

    private long getExecutionTime() {
        return endNow - startNow;
    }
}
