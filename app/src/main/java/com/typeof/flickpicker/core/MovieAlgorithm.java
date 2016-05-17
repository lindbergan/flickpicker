package com.typeof.flickpicker.core;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * FlickPicker
 * Group 22
 * Created on 03/05/16.
 */

public class MovieAlgorithm {

    /**
     * Method for getting movies recommended for the user based on algorithm.
     * @return List of movies recommended for the user.
     */
    public static List<Movie> getRecommendations(User user) {

        List<Movie> results = new ArrayList<>();
        User currentUser = user;
        int desiredSizeOfList = 100;
        int requirements = 5;

        List<User> allFriends = App.getFriendDAO().getFriendsFromUserId(user.getId());
        List<Friend> friendsWithSimilarTaste = getFriendsWithSimilarTaste(allFriends, currentUser, requirements);
        List<Movie> usersMovieCollection = App.getMovieDAO().getUsersMovieCollection(desiredSizeOfList, currentUser.getId());

        Map<Movie,Double> friendsMoviesAndScore = getFriendsMovies(desiredSizeOfList, friendsWithSimilarTaste); //return a map, key-value pair (holding movie && score)
        removeMoviesUserHasSeen(friendsMoviesAndScore, usersMovieCollection); //compare the key-value and remove it from the map if user has seen it before
        Map<Movie,Double> sortedMoviesAndScores = sortMapByValues(friendsMoviesAndScore); //sort the map based on values (SCORE)

        results = extractMoviesFromMap(sortedMoviesAndScores); //extract the movies from the sorted map

       return results;
    }

    public static List<Movie> extractMoviesFromMap(Map<Movie,Double> map){

        List<Movie> results = new ArrayList<Movie>();

        for(Map.Entry<Movie,Double> entryInSet : map.entrySet()){

            Movie highestScoredMovie = entryInSet.getKey();
            results.add(highestScoredMovie);
        }

        return results;
    }

    public static <Movie, Double extends Comparable<Double>> Map<Movie, Double> sortMapByValues(final Map<Movie, Double> map) {
        Comparator<Movie> valueComparator =  new Comparator<Movie>() {
            public int compare(Movie firstMovie, Movie secondMovie) {
                int compare = map.get(secondMovie).compareTo(map.get(firstMovie));
                if (compare == 0) return 1;
                else return compare;
            }
        };
        Map<Movie, Double> sortedMapByValues = new TreeMap<Movie, Double>(valueComparator);
        sortedMapByValues.putAll(map);
        return new LinkedHashMap<Movie,Double>(sortedMapByValues);
    }


    public static void removeMoviesUserHasSeen(Map<Movie,Double> friendsMoviesAndScore, List<Movie> usersMovieCollection){

        for (int i= 0; i<usersMovieCollection.size();i++){
            //if current movie exists in hashTable --> remove it
            Movie currentMovie = usersMovieCollection.get(i);

            if (friendsMoviesAndScore.containsKey(currentMovie)){
                friendsMoviesAndScore.remove(currentMovie);
            }
        }
    }

    public static List<Friend> getFriendsWithSimilarTaste(List<User> users, User currentUser, int requirements ){

        //convert the users to get the List of friends if the requirements are met
        List<Friend> friendsWithSimilarTaste = new ArrayList<Friend>();

        for (int i = 0; i < users.size(); i++){

            Friend currentFriend = App.getFriendDAO().getFriendRelation(currentUser.getId(),users.get(i).getId());

            if (currentFriend.getNmbrOfMoviesBothSeen() >= requirements){
                friendsWithSimilarTaste.add(currentFriend);
            }
        }

        return friendsWithSimilarTaste;
    }

    public static HashMap<Movie,Double> getFriendsMovies(int desiredSizeOfList, List<Friend> friendsWithSimilarTaste){

        HashMap<Movie,Double> friendsMoviesAndScore = new HashMap<Movie,Double>();
        List<Double> disMatchValues = getAllDisMatchValues(friendsWithSimilarTaste);

        List<User> userList = convertFriendsToUsers(friendsWithSimilarTaste);

        for (int i = 0; i < userList.size(); i++){

            long currentUserId = userList.get(i).getId();
            List<Movie> currentUsersMovieCollection = App.getMovieDAO().getUsersMovieCollection(desiredSizeOfList, currentUserId);

            for (int j = 0; j<currentUsersMovieCollection.size(); j++){

                // add key- value pair to hashmap: key: movieid from j, value score from i
                // check for duplicates among movie ratings (separate method)
                Movie currentMovie = currentUsersMovieCollection.get(j);
                double currentMatchValue = disMatchValues.get(i);
                checkIfRatedByOtherUser(friendsMoviesAndScore,currentMovie,currentMatchValue);
            }
        }

        return friendsMoviesAndScore;

    }

    public static void checkIfRatedByOtherUser(HashMap<Movie,Double> friendsMoviesAndScore,Movie currentMovie, double currentMatchValue){

        if (friendsMoviesAndScore.containsKey(currentMovie)){
            determineMatchValue(friendsMoviesAndScore,currentMovie,currentMatchValue);
        }
        else{
            friendsMoviesAndScore.put(currentMovie,currentMatchValue);
        }
    }

    public static void determineMatchValue(HashMap<Movie,Double> friendsMoviesAndScore, Movie currentMovie, double currentMatchValue){
        //put the best match in the table
        double previousMatchValue = friendsMoviesAndScore.get(currentMovie);
        if(currentMatchValue > previousMatchValue) {
            friendsMoviesAndScore.put(currentMovie, currentMatchValue);
        }
        else {
            friendsMoviesAndScore.put(currentMovie,previousMatchValue);
        }
    }

    public static List<Double> getAllDisMatchValues(List<Friend> friendsWithSimilarTaste){

        List<Double> disMatchValues = new ArrayList<Double>();

        for(int i = 0; i < friendsWithSimilarTaste.size(); i++){

            double currentDisMatchValue = friendsWithSimilarTaste.get(i).getDisMatch();
            disMatchValues.add(currentDisMatchValue);
        }

        return disMatchValues;
    }

    public static List<User> convertFriendsToUsers(List<Friend> friendsWithSimilarTaste){

        List<User> userList = new ArrayList<User>();

        for (int i = 0; i < friendsWithSimilarTaste.size(); i++){

            long currentFriendsUserId = friendsWithSimilarTaste.get(i).getGetUserIdTwo();
            User currentFriend = App.getUserDAO().getUserById(currentFriendsUserId);
            userList.add(currentFriend);
        }

        return  userList;
    }

}
