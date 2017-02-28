package com.nimbees.newdemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.manager.CustomNotificationManager;
import com.nimbees.newdemo.ui.fragments.BeaconFragment;
import com.nimbees.newdemo.ui.fragments.ConfigurationFragment;
import com.nimbees.newdemo.ui.fragments.EmptyFragment;
import com.nimbees.newdemo.ui.fragments.InformationFragment;
import com.nimbees.newdemo.ui.fragments.MapFragment;
import com.nimbees.newdemo.ui.fragments.NavigationDrawerFragment;
import com.nimbees.newdemo.ui.fragments.NotificationDialogFragment;
import com.nimbees.newdemo.ui.fragments.NotificationFragment;
import com.nimbees.newdemo.ui.fragments.RegisterFragment;
import com.nimbees.newdemo.ui.fragments.TagFragment;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.beacons.actions.NimbeaconActionCustom;
import com.nimbees.platform.beacons.actions.NimbeaconActionMessage;
import com.nimbees.platform.beacons.actions.NimbeaconActionUrl;
import com.nimbees.platform.beacons.exceptions.NimbeaconException;
import com.nimbees.platform.beacons.listener.NimbeaconActionListener;
import com.nimbees.platform.beacons.listener.NimbeaconEventListener;
import com.nimbees.platform.beacons.model.Nimbeacon;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.nimbees.newdemo.manager.CustomNotificationManager.MAP_SCREEN_NAME;
import static com.nimbees.newdemo.manager.CustomNotificationManager.NOTIFICATIONS_SCREEN_NAME;

/**
 * Main activity of the application.
 */
public class NavigationActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /** The Navigation fragment */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /** The intent to launch the activity */
    private Intent mIntent;
    /** The Toolbar element */
    private Toolbar mToolbar;
    /** The screen key */
    public static final String KEY_SCREEN_TO_SHOW = "SCREEN_KEY";
    /** The map fragment key */
    public static final int FRAGMENT_MAP = 1;

    /**
     * We only have one instance of this activity so we need to override the intent from the notification to handle it
     *
     * @param intent The intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            if (!getIntent().getExtras().getString("title", "").equals("")) {
                resolveIntentextras(getIntent().getExtras());
            } else {
                if (!getIntent().getExtras().getString(NavigationActivity.KEY_SCREEN_TO_SHOW, "").equals("")) {
                    handleScreenTransition(getIntent().getExtras().getString(NavigationActivity.KEY_SCREEN_TO_SHOW));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        NimbeesClient.checkPlayServicesWithDialog(this,true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for Marshmallow and newer versions
            if (!NimbeesClient.getPermissionManager().getLocationPermissionState(this))
                NimbeesClient.getPermissionManager().checkPermissions(this);
        }

        // If we have the location permission, can start to scan
        if (NimbeesClient.getPermissionManager().getLocationPermissionState(this)) {
            scanBeacons();
        }

        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(mToolbar);

        mIntent = getIntent();

        if (mNavigationDrawerFragment == null) {
            mNavigationDrawerFragment = new NavigationDrawerFragment();
        }

        if (getIntent().getExtras() != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_SCREEN_TO_SHOW, mIntent.getIntExtra(KEY_SCREEN_TO_SHOW, 0));

            if (!mNavigationDrawerFragment.isAdded()) {
                mNavigationDrawerFragment.setArguments(bundle);
            }
        }

        if (!mNavigationDrawerFragment.isAdded()) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.navigation_drawer, mNavigationDrawerFragment);
            ft.commit();
        }

        // Set up the drawer.
        mNavigationDrawerFragment.saveContainerIds(R.id.navigation_drawer, R.id.drawer_layout);
    }

    /**
     * This method start the beacon search listener
     */
    public void scanBeacons() {

        try {
            NimbeesClient.getBeaconManager().startSearching(new NimbeaconActionListener() {
                @Override
                public void onMessageAction(NimbeaconActionMessage nimbeaconActionMessage) {
                    //log("OnMessageAction : " + nimbeaconActionMessage.getContent());
                    CustomNotificationManager.showBeaconNotification(nimbeaconActionMessage.getTitle(), nimbeaconActionMessage.getContent());
                }

                @Override
                public void onUrlAction(NimbeaconActionUrl nimbeaconActionUrl) {
                    //log("onUrlAction : " + nimbeaconActionUrl.getUrl());
                    CustomNotificationManager.showBeaconNotification(nimbeaconActionUrl.getDescription(), nimbeaconActionUrl.getUrl());

                }

                @Override
                public void onCustomAction(NimbeaconActionCustom nimbeaconActionCustom) {
                    //log("onCustomAction : " + nimbeaconActionCustom.getData());
                    CustomNotificationManager.showBeaconNotification(nimbeaconActionCustom.getEvent().toString(), nimbeaconActionCustom.getData());

                }
            }, new NimbeaconEventListener() {
                @Override
                public void onOut(Nimbeacon nimbeacon) {

                }

                @Override
                public void onNear(Nimbeacon nimbeacon) {

                }

                @Override
                public void onFar(Nimbeacon nimbeacon) {

                }

                @Override
                public void onImmediate(Nimbeacon nimbeacon) {

                }
            }, true);
        } catch (NimbeaconException e) {
            Log.e("NimbeaconManager", e.toString());
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (position == 0) {
            fragment = new RegisterFragment();
        } else {
            if (NimbeesClient.getUserManager().getUserData() == null) {
                fragment = new EmptyFragment();
            } else {
                switch (position) {
                    case 1:
                        fragment = new InformationFragment();
                        break;
                    case 2:
                        fragment = new NotificationFragment();
                        break;
                    case 3:
                        fragment = new TagFragment();
                        break;
                    case 4:
                        fragment = new BeaconFragment();
                        break;
                    case 5:
                        fragment = new MapFragment();
                        break;
                    case 6:
                        fragment = new ConfigurationFragment();
                        break;
                    default:
                        break;
                }
            }
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    /**
     * This method show the dialog for notification
     */
    private void resolveIntentextras(Bundle extras) {

        String title = extras.getString("title", "");
        String text = extras.getString("text", "");
        Log.d("Extra", extras.toString());
        NotificationDialogFragment notificationDialogFragment = new NotificationDialogFragment(title, text);
        notificationDialogFragment.show(this.getFragmentManager(), "NotificationDialogFragment");
        extras.clear();
    }

    /**
     * This method define the screen transition
     *
     * @param screenName The screen name
     */
    private void handleScreenTransition(String screenName) {

        if (screenName.equals(MAP_SCREEN_NAME)) {
            onNavigationDrawerItemSelected(5);
        } else {
            if (screenName.equals(NOTIFICATIONS_SCREEN_NAME)) {
                onNavigationDrawerItemSelected(2);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Calligraphy apply
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}