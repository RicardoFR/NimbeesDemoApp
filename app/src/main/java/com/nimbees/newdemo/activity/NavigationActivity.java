package com.nimbees.newdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.fragments.BeaconFragment;
import com.nimbees.newdemo.fragments.ConfigurationFragment;
import com.nimbees.newdemo.fragments.EmptyFragment;
import com.nimbees.newdemo.fragments.MapFragment;
import com.nimbees.newdemo.fragments.NavigationDrawerFragment;
import com.nimbees.newdemo.fragments.NotificationFragment;
import com.nimbees.newdemo.fragments.RegisterFragment;
import com.nimbees.newdemo.fragments.TagFragment;
import com.nimbees.platform.NimbeesClient;

/**
 * Main activity of the application.
 */
public class NavigationActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Intent mIntent;
    private Toolbar mToolbar;

    public static final String KEY_SCREEN_TO_SHOW = "SCREEN_KEY";
    public static final int FRAGMENT_MAP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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
        mNavigationDrawerFragment.saveContainerIds(
                R.id.navigation_drawer,
                R.id.drawer_layout);

//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.nimbees_blue));
//
//        //Set up the Action Bar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nimbees_yellow)));
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
                        fragment = new NotificationFragment();
                        break;
                    case 2:
                        fragment = new TagFragment();
                        break;
                    case 3:
                        fragment = new BeaconFragment();
                        break;
                    case 4:
                        fragment = new MapFragment();
                        break;
                    case 5:
                        fragment = new ConfigurationFragment();
                        break;
                    default:
                        break;
                }
            }
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }
}
