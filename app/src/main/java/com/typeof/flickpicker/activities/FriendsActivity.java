package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.FriendDAO;
import java.util.ArrayList;
import java.util.List;


public class FriendsActivity extends AppCompatActivity {

    private FriendDAO mFriendDAO;
    private List<Rating> mFriendsRecentActivity;
    private ListView mFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database db = App.getDatabaseSeed();
        db.dropTables();
        db.setUpTables();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // mFriendDAO = App.getFriendDAO();
        mFeed = (ListView) findViewById(R.id.mFeed);

        updateFriendsRecentActivity();
        placeDummyData();
        initMFeed();

    }

    // Insert real userId

    public void initMFeed() {
        ListAdapter ratingListAdapter = new RatingAdapter(this, mFriendsRecentActivity.toArray());
        mFeed.setAdapter(ratingListAdapter);

    }

    public void updateFriendsRecentActivity() {

        mFriendsRecentActivity = new ArrayList<>();

        // mFriendsRecentActivity = mFriendDAO.getFriendsLatestActivities();
    }

    public void placeDummyData() {

        User u1 = new User("niklas", "password");
        User u2 = new User("johan", "password");
        u1.setId(1);
        u2.setId(2);
        u1.setScore(0);
        u2.setScore(0);
        App.getUserDAO().saveUser(u1);
        App.getUserDAO().saveUser(u2);

        Movie m = new Movie("PunkBrothers", 1994);
        m.setId(1);
        App.getMovieDAO().saveMovie(m);

        Rating r = new Rating(5.0, m.getId(), u1.getId());
        Rating r2 = new Rating(4.0, m.getId(), u2.getId());
        App.getRatingDAO().saveRating(r);
        App.getRatingDAO().saveRating(r2);

        App.getFriendDAO().addFriend(new Friend(App.getCurrentUser().getId(), u2.getId()));
        App.getFriendDAO().addFriend(new Friend(App.getCurrentUser().getId(), u2.getId()));

        mFriendsRecentActivity.add(r);
        mFriendsRecentActivity.add(r2);
    }

}
