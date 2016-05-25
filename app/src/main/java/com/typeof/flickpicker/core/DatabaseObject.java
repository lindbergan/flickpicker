package com.typeof.flickpicker.core;

/**
 * DatabaseObject
 *
 * An interface that specifies what a database object/core entity needs to implement in order
 * to be such an object.
 */

public interface DatabaseObject {
    long getId();
    void setId(long id);
}
