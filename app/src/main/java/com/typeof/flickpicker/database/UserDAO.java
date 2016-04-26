package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface UserDAO {

    public User getUserById(long userId);
    public long saveUser(User user);
    public List<User> searchUser(String column, String searchString);
    public int deleteUser(User user);

}
