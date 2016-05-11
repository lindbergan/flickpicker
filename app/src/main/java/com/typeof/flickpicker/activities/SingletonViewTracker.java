package com.typeof.flickpicker.activities;

public class SingletonViewTracker {

    private static SingletonViewTracker instance = new SingletonViewTracker();

    private SingletonViewTracker(){}
    private String currentCommunityTab = "topMovies";

    public static SingletonViewTracker getInstance(){
        return instance;
    }

    public void setCurrentCommunityTab(String currentTab){
        currentCommunityTab = currentTab;
    }
    public String getCurrentCommunityTab(){
        return currentCommunityTab;
    }
}
