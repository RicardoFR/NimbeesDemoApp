package com.nimbees.newdemo.activity;

import android.app.Application;
import android.util.Log;

import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;

/**
 * NimbeesClient need to be initialized before any other class, thats why we extend Application here.
 *
 * Lets fly to the bees!
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        try {
            NimbeesClient.init(this);
            Log.e("CustomApplication", "Initializing nimBees Platform");

        } catch (NimbeesException e) {
            Log.e("CustomApplication", "Error initializing nimBees Platform");
        }
        super.onCreate();
    }
}
