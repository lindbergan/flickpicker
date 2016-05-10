package com.typeof.flickpicker.activities;

import android.app.Fragment;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;

import com.typeof.flickpicker.R;

public class SearchFragment extends Fragment {

    private TabHost mTabHostSearch;
    private ListView listViewSearchMovies;
    private ListView listViewSearchFriend;
    private SearchView mSearchViewMovie;
    private SearchView mSearchViewFriend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //reboot the database
        App.getDatabase().dropTables();
        App.getDatabase().setUpTables();

        //Feed the database with dummy Data
        SeedData.seedCommunityData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.activity_search, container, false);
        hookUpViews(searchView);
        configureTabs(searchView);
        setUpListeners();
        return searchView;
    }

    public void hookUpViews(View view){
        listViewSearchMovies = (ListView) view.findViewById(R.id.listViewSearchMovie);
        listViewSearchFriend = (ListView) view.findViewById(R.id.listViewSearchFriend);
        mSearchViewMovie = (SearchView) view.findViewById(R.id.searchViewMovie);
        mSearchViewFriend = (SearchView) view.findViewById(R.id.searchViewFriend);
        mSearchViewMovie.setSubmitButtonEnabled(false);

    }
    public void configureTabs(View view){

        mTabHostSearch = (TabHost) view.findViewById(R.id.tabHostSearch);
        mTabHostSearch.setup();

        final TabHost.TabSpec mTabSpecSearchMovie = mTabHostSearch.newTabSpec("searchMovie");
        mTabSpecSearchMovie.setContent(R.id.tabSearchMovie);
        mTabSpecSearchMovie.setIndicator("Search Movie");
        mTabHostSearch.addTab(mTabSpecSearchMovie);

        final TabHost.TabSpec mTabSpecSearchFriend = mTabHostSearch.newTabSpec("searchFriend");
        mTabSpecSearchFriend.setContent(R.id.tabSearchFriend);
        mTabSpecSearchFriend.setIndicator("Search Friend");
        mTabHostSearch.addTab(mTabSpecSearchFriend);

        mTabHostSearch.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if(tabId.equals("searchMovie")){
                    //do this
                }
                else{
                    //do that
                }
            }
        });
    }

    public void setUpListeners(){

        mSearchViewMovie.setIconifiedByDefault(false);
        mSearchViewFriend.setIconifiedByDefault(false);
       // mSearchViewMovie.setOnSearchClickListener(View.OnClickListener listener)

        //String str = String.valueOf(mSearchViewMovie.getQuery());
        //System.out.println(str);

        mSearchViewMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println(s);
                return true;
            }
        });



    }

}
