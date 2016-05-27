package com.typeof.flickpicker.application.helpers;

import java.beans.PropertyChangeListener;

/**
 * Interface describing an observable class
 */
public interface DataObservable {
    void addObserver(PropertyChangeListener observer);
}
