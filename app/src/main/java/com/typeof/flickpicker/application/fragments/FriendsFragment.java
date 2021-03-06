package com.typeof.flickpicker.application.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.adapters.FriendsActivityAdapter;
import com.typeof.flickpicker.application.helpers.KeyboardHelper;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.FriendDAO;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * FriendsFragment extends Fragment
 * Used for showing friends recent activity
 */

public class FriendsFragment extends Fragment implements PropertyChangeListener {

    // Fields

    private FriendDAO mFriendDAO;
    private List<Rating> mFriendsRecentActivity;
    private ListAdapter ratingListAdapter;

    // Views

    private ListView mListViewFeed;
    private SearchView mNameTextField;
    private TextView hiddenText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends, container, false);

        mFriendDAO = App.getFriendDAO();

        mListViewFeed = (ListView) view.findViewById(R.id.mFeed);
        mNameTextField = (SearchView) view.findViewById(R.id.search_for_a_name);
        hiddenText = (TextView) view.findViewById(R.id.hiddenNoFriendsText);

        KeyboardHelper keyboardHelper = new KeyboardHelper(getActivity(), getContext());
        keyboardHelper.setupUI(view.findViewById(R.id.parent));

        getFriendsRecentActivities();
        initAdapters();
        updateRecentActivities();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return view;
    }

    private void getFriendsRecentActivities() {
        mFriendsRecentActivity = mFriendDAO.getFriendsLatestActivities(App.getCurrentUser().getId());
    }

    private void initAdapters() {
        ListAdapter ratingListAdapter = new FriendsActivityAdapter(getActivity(), mFriendsRecentActivity.toArray());
        mListViewFeed.setAdapter(ratingListAdapter);
    }

    /**
     * On every char change in the search query the onQueryTextChange method is called
     * Loops through all the recent ratings and filters out those names who matches the search query
     * If no ratings is found to match the search query the 'hiddenText' is presented
     * Uses the FriendsActivityAdapter class
     */

    private void updateRecentActivities() {
        mNameTextField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                // Sets the hiddenText invisible before the check if friends are found
                if (hiddenText.getVisibility() == View.VISIBLE) hiddenText.setVisibility(View.INVISIBLE);

                if (newText != null) {
                    List<Rating> result = new ArrayList<>();
                    for (Rating r : mFriendsRecentActivity) {
                        String username = App.getUserDAO().getUserById(r.getUserId()).getUsername();
                        username = username.toLowerCase();
                        newText = newText.toLowerCase();
                        if (username.contains(newText)) {
                            result.add(r);
                        }
                    }
                    if (result.size() == 0) {
                        hiddenText.setVisibility(View.VISIBLE);
                    }
                    ratingListAdapter = new FriendsActivityAdapter(getActivity(), result.toArray());
                    mListViewFeed.setAdapter(ratingListAdapter);
                }
                else {
                    getFriendsRecentActivities();
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("randomize_data")) {
            getFriendsRecentActivities();
            initAdapters();
            updateRecentActivities();
        }
    }
}
