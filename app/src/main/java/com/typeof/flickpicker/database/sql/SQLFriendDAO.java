package com.typeof.flickpicker.database.sql;
import android.content.Context;

import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.PlaylistDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public class SQLFriendDAO extends SQLDAO implements FriendDAO {
    public SQLFriendDAO(Context ctx) {
        super(ctx);
    }
}
