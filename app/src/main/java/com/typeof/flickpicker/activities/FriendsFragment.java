package com.typeof.flickpicker.activities;

import android.support.annotation.Nullable;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.FriendDAO;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FriendDAO mFriendDAO;
    private List<Rating> mFriendsRecentActivity;
    private ListView mListViewFeed;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendDAO = App.getFriendDAO();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View communityView = inflater.inflate(R.layout.activity_friends, container, false);

        mView = communityView;
        mListViewFeed = (ListView) mView.findViewById(R.id.mFeed);

        updateFriendsRecentActivity();
        initAdapters();

        return communityView;
    }

    public void initAdapters() {
        ListAdapter ratingListAdapter = new RatingAdapter(getActivity(), mFriendsRecentActivity.toArray());
        mListViewFeed.setAdapter(ratingListAdapter);

    }

    public void updateFriendsRecentActivity() {
        long asd = App.getCurrentUser().getId();
        mFriendsRecentActivity = mFriendDAO.getFriendsLatestActivities(App.getCurrentUser().getId());
    }

}
