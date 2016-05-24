package com.typeof.flickpicker.core;

import com.typeof.flickpicker.application.App;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * MovieAlgorithm
 *
 * A helper class that calculates and returns a list of recommended movies for the user,
 * based on movies the users friends have seen, the ratings they have given the movies as well as
 * how well they match the users preferences.
 */

public class MovieAlgorithm {

    /**
     * A method for getting suitable recommendations for the user based on an algorithm. Each movie
     * gets a score associated to it, which is based on the match value of the user and its friends.
     * The list contains movies the users friends have seen but not the user and sets the score of
     * the movie as (rating value) x (match value).
     *
     * @return returns a list of recommended movies for the user, sorted by score.
     */

    public static List<Movie> getRecommendations(User currentUser) {

        List<Movie> results = new ArrayList<>();
        int desiredSizeOfList = 100;

        //The amount of movies the user and friend both need to have seen in order to be considered
        //as having "similar taste"
        int requirements = 5;

        //Get all user's friends and movies
        List<User> allFriends = App.getFriendDAO().getFriendsFromUserId(currentUser.getId());
        List<Movie> usersMovieCollection = App.getMovieDAO().getMovieCollectionFromUserId(desiredSizeOfList, currentUser.getId());

        //Filter out the friends that doesn't live up to the requirements of "similar taste"
        List<Friend> friendsWithSimilarTaste = getFriendsWithSimilarTaste(allFriends, currentUser, requirements);

        //Loop trough all friendsWithSimilarTaste's movie collections and save them in a map
        Map<Movie,Double> friendsMoviesAndScore = getFriendsMovies(desiredSizeOfList, friendsWithSimilarTaste);

        //Remove all movies users have seen from the map (we don't want those as recommendations)
        removeMoviesUserHasSeen(friendsMoviesAndScore, usersMovieCollection);

        //Sort the remaining elements in the map based on score
        Map<Movie,Double> sortedMoviesByScores = sortMapByScore(friendsMoviesAndScore);

        //Extract all movies from the sorted map
        results = extractMoviesFromMap(sortedMoviesByScores);

       return results;
    }

    /**
     * A method for filtering which of the friends meet the requirements.
     * @param users the users that are friends to the currentUser
     * @param currentUser the current user
     * @param requirements the requirements for the filtering
     * @return returns a list of friends that meet the requirements
     */
    public static List<Friend> getFriendsWithSimilarTaste(List<User> users, User currentUser, int requirements ){

        //Save the friends that meet the requirements in a list
        List<Friend> friendsWithSimilarTaste = new ArrayList<Friend>();

        for (int i = 0; i < users.size(); i++){

            Friend currentFriend = App.getFriendDAO().getFriendRelation(currentUser.getId(),users.get(i).getId());

            if (currentFriend.getNmbrOfMoviesBothSeen() >= requirements){
                friendsWithSimilarTaste.add(currentFriend);
            }
        }
        return friendsWithSimilarTaste;
    }

    /**
     * A method that extract all movies rated by friends that have similar taste and places them in a map.
     * The method also associate a score value to each movie, based on the friends rating and match value.
     * @param desiredSizeOfList the specified size of the list
     * @param friendsWithSimilarTaste a list of friends that meet the requirements
     * @return returns a map containing all movies friends with similar taste have seen
     */

    public static Map<Movie,Double> getFriendsMovies(int desiredSizeOfList, List<Friend> friendsWithSimilarTaste){

        //Create the map that will hold the movies and scores
        HashMap<Movie,Double> friendsMoviesAndScore = new HashMap<Movie,Double>();
        List<Double> disMatchValues = getAllDisMatchValues(friendsWithSimilarTaste);

        //Convert friends to users to be able to get their movie collections
        List<User> userList = convertFriendsToUsers(friendsWithSimilarTaste);

        //loop through each user
        for (int i = 0; i < userList.size(); i++){

            long currentUserId = userList.get(i).getId();
            List<Movie> currentUsersMovieCollection = App.getMovieDAO().getMovieCollectionFromUserId(desiredSizeOfList, currentUserId);

            //loop trough each users collection
            for (int j = 0; j<currentUsersMovieCollection.size(); j++){

                Movie currentMovie = currentUsersMovieCollection.get(j);
                double currentRating = App.getRatingDAO().getRatingFromUser(currentUserId,currentMovie.getId());
                double currentDisMatchValue = disMatchValues.get(i);

                //check if another friend has rated the same movie
                checkIfRatedByOtherUser(friendsMoviesAndScore,currentDisMatchValue,currentMovie,currentRating);
            }
        }
        return friendsMoviesAndScore;
    }

    private static void checkIfRatedByOtherUser(Map<Movie,Double> friendsMoviesAndScore,double currentDisMatchValue,Movie currentMovie,double currentRating){

        //Check if currentMovie has been rated by another friend. If so - determine which recommendation is the best one.
        //If not - set movie's score to (ratingValue x 1/dismatchValue) == (ratingValue x matchValue)
        if (friendsMoviesAndScore.containsKey(currentMovie)){
            determineBestMatch(friendsMoviesAndScore,currentDisMatchValue,currentMovie,currentRating);
        }
        else{
            double score = currentRating * 1/currentDisMatchValue;
            friendsMoviesAndScore.put(currentMovie,score);
        }
    }

    private static void determineBestMatch(Map<Movie,Double> friendsMoviesAndScore, double currentDisMatchValue,Movie currentMovie, double currentRating){

        //Determine the best match and save that one in the map
        double previousScore = friendsMoviesAndScore.get(currentMovie);
        double currentScore = currentRating * 1/currentDisMatchValue;

        if(currentScore > previousScore) {
            //remove old element && replace with the better match
            friendsMoviesAndScore.remove(currentMovie);
            friendsMoviesAndScore.put(currentMovie, currentScore);
        }
    }

    /**
     * A method for extracting all dismatch values from friends with similar taste.
     * @param friendsWithSimilarTaste the list of friends with similar taste
     * @return returns a list of dismatch values
     */

    public static List<Double> getAllDisMatchValues(List<Friend> friendsWithSimilarTaste){

        List<Double> disMatchValues = new ArrayList<Double>();

        for(int i = 0; i < friendsWithSimilarTaste.size(); i++){

            double currentDisMatchValue = friendsWithSimilarTaste.get(i).getDisMatch();
            disMatchValues.add(currentDisMatchValue);
        }
        return disMatchValues;
    }

    private static List<User> convertFriendsToUsers(List<Friend> friendsWithSimilarTaste){

        List<User> userList = new ArrayList<User>();

        for (int i = 0; i < friendsWithSimilarTaste.size(); i++){

            long currentFriendsUserId = friendsWithSimilarTaste.get(i).getGetUserIdTwo();
            User currentFriend = App.getUserDAO().getUserById(currentFriendsUserId);
            userList.add(currentFriend);
        }
        return  userList;
    }

    /**
     * A method for removing movies the user has already seen from the map.
     * @param friendsMoviesAndScore the map of movies and scores that the users friends have rated
     * @param usersMovieCollection the users movie collection
     */

    public static void removeMoviesUserHasSeen(Map<Movie,Double> friendsMoviesAndScore, List<Movie> usersMovieCollection){

        for (int i= 0; i<usersMovieCollection.size();i++){

            Movie movieUserHasSeen = usersMovieCollection.get(i);

            if (friendsMoviesAndScore.containsKey(App.getMovieDAO().findMovie(movieUserHasSeen.getId()))){
                friendsMoviesAndScore.remove(App.getMovieDAO().findMovie(movieUserHasSeen.getId()));
            }
        }
    }

    /**
     * A method for sorting the entries in the map based on score.
     * @param map the map that will be sorted
     * @param <Movie> the movie entry
     * @param <Double> the score entry
     * @return returns a sorted map based on score in descending order
     */

    public static <Movie, Double extends Comparable<Double>> Map<Movie, Double> sortMapByScore(final Map<Movie, Double> map) {
        Comparator<Movie> valueComparator =  new Comparator<Movie>() {
            public int compare(Movie firstMovie, Movie secondMovie) {
                int compare = map.get(secondMovie).compareTo(map.get(firstMovie));
                if (compare == 0) return 1;
                else return compare;
            }
        };
        Map<Movie, Double> sortedMapByScores = new TreeMap<Movie, Double>(valueComparator);
        sortedMapByScores.putAll(map);
        return new LinkedHashMap<Movie,Double>(sortedMapByScores);
    }

    /**
     * A method for extracting all the movies in the map.
     * @param map the map containing the movies to be extracted
     * @return returns a list of movies
     */
    public static List<Movie> extractMoviesFromMap(Map<Movie,Double> map){

        List<Movie> results = new ArrayList<Movie>();

        //loop through the set of keys (not interested in values anymore - since keys already sorted based on values)
        for(Map.Entry<Movie,Double> entryInSet : map.entrySet()){

            Movie highestScoredMovie = entryInSet.getKey();
            results.add(highestScoredMovie);
        }
        return results;
    }
}
