package com.typeof.flickpicker.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;

public class MovieAdapter extends CustomAdapter {

    Object[] obj;

    public MovieAdapter(Context context, Object[] obj) {
        super(context, obj);
        this.obj = obj;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row_community, parent, false);

        TextView movieName = (TextView) customView.findViewById(R.id.moviename_textview_movieCell);
        TextView movieYear = (TextView) customView.findViewById(R.id.movie_year_textview_movieCell);
        RatingBar ratingBar = (RatingBar) customView.findViewById(R.id.ratingBar_movieCell);

        Movie mov = (Movie) getItem(position);
        movieName.setText(mov.getTitle());
        movieYear.setText(String.valueOf(mov.getYear()));
        ratingBar.setRating(Float.parseFloat(Double.toString(mov.getCommunityRating())));
        return customView;
    }

    /*
    private class CreateListCellTask extends AsyncTask<Integer, Void, View> {
        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        protected View doInBackground(Integer... ints) {
            // Some long-running task like downloading an image.
            Bitmap = downloadImageFromUrl(ints[0]);
            return someBitmap;
        }

        protected void onProgressUpdate(Progress... values) {
            // Executes whenever publishProgress is called from doInBackground
            // Used to update the progress indicator
            progressBar.setProgress(values[0]);
        }

        protected void onPostExecute(Bitmap result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
            imageView.setImageBitmap(result);
            // Hide the progress bar
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
    */
}
