package com.typeof.flickpicker.activities;

import android.os.AsyncTask;
import android.view.View;

/**
 * Created by Jolo on 5/10/16.
 */
public class CreateListCellTask extends AsyncTask<Void, Void, View> {

    protected View doInBackground(View cutomView) {
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            showDialog("Downloaded " + result + " bytes");
        }
    }

}
