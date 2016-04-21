package com.typeof.flickpicker;

import com.typeof.flickpicker.core.Friend;

public abstract class DatabaseObject {

    public abstract String getTableName();
    public abstract int getId();

    public boolean save(){
        //TODO
        return true;
    }

    public DatabaseObject find(int ObjectId){
        //TODO
        return new Friend(1,2,3);

    }
    public int delete(){
        //TODO
        return 0;
    }
}
