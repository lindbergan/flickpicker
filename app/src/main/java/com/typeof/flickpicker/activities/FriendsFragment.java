package com.typeof.flickpicker.activities;

import android.support.annotation.Nullable;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.FriendDAO;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FriendDAO mFriendDAO;
    private List<Rating> mFriendsRecentActivity;
    private ListView mListViewFeed;
    private EditText mNameTextField;
    private Button mClearButton;
    private ListAdapter ratingListAdapter;
    private TextView hiddenText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendDAO = App.getFriendDAO();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View communityView = inflater.inflate(R.layout.activity_friends, container, false);

        mListViewFeed = (ListView) communityView.findViewById(R.id.mFeed);
        mNameTextField = (EditText) communityView.findViewById(R.id.search_for_a_name_editText);
        mClearButton = (Button) communityView.findViewById(R.id.clearButton);
        hiddenText = (TextView) communityView.findViewById(R.id.hiddenNoFriendsText);

        initClearTextField();
        getFriendsRecentActivities();
        initAdapters();
        updateRecentActivities();

        return communityView;
    }

    public void initAdapters() {
        ListAdapter ratingListAdapter = new FriendsActivityAdapter(getActivity(), mFriendsRecentActivity.toArray());

        mListViewFeed.setAdapter(ratingListAdapter);
    }

    public void updateRecentActivities() {
        mNameTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                // Sets the hiddenText invisible before the check if friends are found
                if (hiddenText.getVisibility() == View.VISIBLE) hiddenText.setVisibility(View.INVISIBLE);

                if (s != null) {
                    List<Rating> result = new ArrayList<>();

                    for (Rating r : mFriendsRecentActivity) {
                        if (App.getUserDAO().getUserById(r.getUserId()).getUsername().contains(s.toString())) {
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
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public void initClearTextField() {
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameTextField.setText("");
            }
        });
    }

    public void getFriendsRecentActivities() {
        mFriendsRecentActivity = mFriendDAO.getFriendsLatestActivities(App.getCurrentUser().getId());
    }

}
