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
    private ListView mFeed;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getDatabase().seedDatabase();

        mFriendDAO = App.getFriendDAO();
        mFeed = (ListView) mView.findViewById(R.id.mFeed);

        updateFriendsRecentActivity();
        initMFeed();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View communityView = inflater.inflate(R.layout.activity_community, container, false);
        mView = communityView;
        return communityView;
    }

    public void initMFeed() {
        ListAdapter ratingListAdapter = new RatingAdapter(getActivity(), mFriendsRecentActivity.toArray());
        mFeed.setAdapter(ratingListAdapter);

    }

    public void updateFriendsRecentActivity() {
        mFriendsRecentActivity = mFriendDAO.getFriendsLatestActivities(App.getCurrentUser().getId());
    }

}
