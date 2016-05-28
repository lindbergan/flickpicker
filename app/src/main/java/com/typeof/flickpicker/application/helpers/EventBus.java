package com.typeof.flickpicker.application.helpers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Event Manager that adds observers and lets the rest of the app use it to signal
 * the observers that a state has changed.
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
