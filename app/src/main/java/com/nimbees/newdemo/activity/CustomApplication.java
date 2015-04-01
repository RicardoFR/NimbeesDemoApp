package com.nimbees.newdemo.activity;

import android.app.Application;
import android.util.Log;

import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;

/**
 * NimbeesClient need to be initialized before any other class, thats why we extend Application here.
 *
 * Let the bees fly!
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        try {
            NimbeesClient.init(this);
            Log.i("NimbeesDemo", "nimBees Client successfully initialized.");
        } catch (NimbeesException e) {
            Log.e("NimbeesDemo", "Error initializing nimBees Platform: " + e.getMessage());
        }
        super.onCreate();
    }
}
