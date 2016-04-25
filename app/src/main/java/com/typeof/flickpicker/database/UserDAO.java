package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.User;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface UserDAO {

    public long saveUser(User user);
    public User getUserById(long userId);
    public int deleteUser(User user);

}
