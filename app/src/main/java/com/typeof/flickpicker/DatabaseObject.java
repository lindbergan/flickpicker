package com.typeof.flickpicker;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseObject<T> {
    public abstract int getId();
    public abstract String getTableName();

    public boolean save() {

        // database save
        return true;
    }

    public static <T> List<T> getAll() {
        // database get all records
        return new ArrayList<>();
    }

    public static <T> T find(int id) {

        // database find
        List<T> cool = new ArrayList<>();
        return cool.get(0);
    }

    public int delete() {
        //database delete

        return 0;
    }

}
