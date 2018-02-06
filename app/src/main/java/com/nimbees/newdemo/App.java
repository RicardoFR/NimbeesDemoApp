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

    /** The Realm Configuration */
    private RealmConfiguration mRealmConfiguration;

    /** Realm database name */
    public static final String DATABASE_NAME = "nimBees-demo.realm";

    @Override
    public void onCreate() {

        Realm.init(this);

        try {
            NimbeesClient.init(this);
            NimbeesClient.setDebugMode(true);
            Log.i("NimbeesDemo", "nimBees Client successfully initialized.");
        } catch (NimbeesException e) {
            Log.e("NimbeesDemo", "Error initializing nimBees Platform: " + e.getMessage());
        }

        // Realm default configuration
        mRealmConfiguration = new RealmConfiguration.Builder()
                .name(DATABASE_NAME)
                .schemaVersion(1)
                .modules(
                        Realm.getDefaultModule(),
                        new NimbeesClient.NimbeesLibraryModule()
                )
                .deleteRealmIfMigrationNeeded()
                .build();

        // Realm config */
        Realm.setDefaultConfiguration(mRealmConfiguration);

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
