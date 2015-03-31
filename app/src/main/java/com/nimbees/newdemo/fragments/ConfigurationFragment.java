package com.nimbees.newdemo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;
import com.nimbees.platform.callbacks.NimbeesCallback;

/**
 * This ConfigurationFragment lets config the most important features of
 * nimBees Platform for test purposes
 * <p/>
 * Say the bee how your app works!
 */

public class ConfigurationFragment extends Fragment {

    /**
     * Name of the Sharedpreferences
     */
    private static final String NIMPREFERENCES = "nimBeesPreferences";

    /**
     * Switch for Beacons.
     */
    private Switch mBeaconSwitch;

    /**
     * Switch for Location.
     */
    private Switch mLocationSwitch;

    /**
     * Switch for Notifications.
     */
    private Switch mNotificationState;

    /**
     * TextView for Beacons Message.
     */
    private TextView mConfigurationBeaconText;

    /**
     * SharedPreferences to store the username.
     */
    private SharedPreferences mPrefs;

    /**
     * Editor of SharedPreferences.
     */
    private SharedPreferences.Editor mEditor;

    /**
     * Default constructor.
     */
    public ConfigurationFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initial config for Sharedpreferences
        mPrefs = this.getActivity().getSharedPreferences(NIMPREFERENCES, Context.MODE_PRIVATE);
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section6));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);

        // Switch element for this fragment
        mBeaconSwitch = (Switch) view.findViewById(R.id.beacons_switch);
        mLocationSwitch = (Switch) view.findViewById(R.id.location_switch);
        mNotificationState = (Switch) view.findViewById(R.id.notification_switch);
        mConfigurationBeaconText = (TextView) view.findViewById(R.id.configuration_beacon_text);

        // Initial setup of switch to show the actual state
        mBeaconSwitch.setChecked(NimbeesClient.getBeaconManager().isMonitoring());
        mLocationSwitch.setChecked(NimbeesClient.getLocationManager().isTracking());
        mNotificationState.setChecked(mPrefs.getBoolean("notifications", true));

        //We check now if the device has BLE
        if(!NimbeesClient.getBeaconManager().deviceSupportsBle()){
            mBeaconSwitch.setVisibility(View.GONE);
            mConfigurationBeaconText.setText(getString(R.string.beacon_error));
        }

        // Switch for Notifications
        mNotificationState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNotificationStatus(mNotificationState.isChecked());
                mEditor = mPrefs.edit();
                mEditor.putBoolean("notifications", mNotificationState.isChecked());
                mEditor.commit();
                Log.d("Notification", mNotificationState.isChecked() ? "Toggling notifications ON" : "Toggling notifications OFF");
            }
        });

        // Switch for Beacons

        mBeaconSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    try {
                        NimbeesClient.getBeaconManager().startMonitoring();
                        Log.d("Bluetooth", "Starting Beacon Service");
                    } catch (NimbeesException e) {
                        Log.d("Bluetooth", "Error starting Beacon Service");
                        mBeaconSwitch.setChecked(false);

                    }
                } else {
                    NimbeesClient.getBeaconManager().stopMonitoring();
                    Log.d("Bluetooth", "Stopping Beacon Service");
                }

            }
        });


        // Switch for Location Manager
        mLocationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationSwitch.isChecked()) {
                    NimbeesClient.getLocationManager().startTracking();
                    Log.d("Location", "Starting Location Tracking");

                } else {
                    NimbeesClient.getLocationManager().stopTracking();
                    Log.d("Location", "Stopping Location Tracking");
                }
            }
        });
        return view;
    }


    /**
     * Changes the status of notifications reception.
     *
     * @param newStatus New status to set.
     */
    private void toggleNotificationStatus(final boolean newStatus) {
        NimbeesClient.getNotificationManager().toggleNotifications(newStatus, new NimbeesCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                mNotificationState.setChecked(newStatus);
            }

            @Override
            public void onFailure(NimbeesException e) {
                mNotificationState.setChecked(!newStatus);
            }
        });
    }

}