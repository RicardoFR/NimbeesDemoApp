package com.nimbees.newdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * NimbeesClient need to be initialized before any other class, thats why we extend Application here.
 * <p/>
 * Let the bees fly!
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        try {
            NimbeesClient.init(this);
            Log.i("NimbeesDemo", "nimBees Client successfully initialized.");
        } catch (NimbeesException e) {
            Log.e("NimbeesDemo", "Error initializing nimBees Platform: " + e.getMessage());
        }

        /** Realm config */
        Realm
                .setDefaultConfiguration(new RealmConfiguration.Builder(this)
                        .name("nimbees-android-demo.realm")
                        .setModules(Realm.getDefaultModule(), new NimbeesClient.NimbeesLibraryModule())
                        .deleteRealmIfMigrationNeeded()
                        .build());

        /** The Calligraphy library initialization */
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Ubuntu-Light.ttf")
                // Aditional fonts
                .setFontAttrId(R.attr.fontPath)
                .build());

        mContext = getApplicationContext();

        super.onCreate();
    }

    public static Context getContext() {
        return mContext;
    }

}
