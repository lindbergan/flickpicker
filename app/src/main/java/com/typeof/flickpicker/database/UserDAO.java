package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface UserDAO {

    User getUserById(long userId);
    long saveUser(User user);
    List<User> searchUser(String column, String searchString);
    int deleteUser(User user);

}
