package com.typeof.flickpicker;
import android.test.ApplicationTestCase;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-21.
 */
public class BaseTest extends ApplicationTestCase<App> {

    private boolean appCreated = false;

    public BaseTest() {
        super(App.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if(!appCreated) {
            createApplication();
            App.createDatabase();
            appCreated = true;
        }
    }
}
