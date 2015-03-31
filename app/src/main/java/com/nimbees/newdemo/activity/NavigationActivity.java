package com.nimbees.newdemo.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.fragments.BeaconFragment;
import com.nimbees.newdemo.fragments.ConfigurationFragment;
import com.nimbees.newdemo.fragments.MapFragment;
import com.nimbees.newdemo.fragments.NavigationDrawerFragment;
import com.nimbees.newdemo.fragments.NotificationFragment;
import com.nimbees.newdemo.fragments.RegisterFragment;
import com.nimbees.newdemo.fragments.TagFragment;


public class NavigationActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
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

        mTitle = getTitle();
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

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.nimbeesBlue));

        //Set up the Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nimbeesYellow)));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                fragment = new RegisterFragment();
                break;
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
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }
}
