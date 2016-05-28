package com.typeof.flickpicker.application.helpers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-28.
 */
public class EventBus implements DataObservable {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public EventBus() {

    }

    @Override
    public void addObserver(PropertyChangeListener observer) {
        pcs.addPropertyChangeListener(observer);
    }

    public void triggerEvent(String propertyName) {
        pcs.firePropertyChange(propertyName, true, false);
    }
}
