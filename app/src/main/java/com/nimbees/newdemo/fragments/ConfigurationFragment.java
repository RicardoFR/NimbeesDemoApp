package com.nimbees.newdemo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;
import com.nimbees.platform.callbacks.NimbeesCallback;

/**
 * This ConfigurationFragment let you config the most important features of
 * nimBees Platform for test purposes.
 *
 * <p/>
 * Tell the bee how your app works!
 */
public class ConfigurationFragment extends Fragment {

    /** Name of the SharedPreferences object used to keep track of the notifications setting. */
    private static final String NIMPREFERENCES = "nimBeesPreferences";

    /** Key used to save the notifications setting. */
    private static final String KEY_NOTIFICATIONS = "notifications";

    /** Switch for Beacons. */
    private Switch mBeaconSwitch;

    /** Switch for Location. */
    private Switch mLocationSwitch;

    /** Switch for Notifications. */
    private Switch mNotificationState;

    /** TextView for Beacons Message. */
    private TextView mConfigurationBeaconText;

    /** SharedPreferences instance to store the notification setting for the user locally. */
    private SharedPreferences mPrefs;

    /** SharedPreferences editor. */
    private SharedPreferences.Editor mEditor;

    /** Default constructor. */
    public ConfigurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = this.getActivity().getSharedPreferences(NIMPREFERENCES, Context.MODE_PRIVATE);
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section6));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);

        // Switch elements for this fragment
        mBeaconSwitch = (Switch) view.findViewById(R.id.beacons_switch);
        mLocationSwitch = (Switch) view.findViewById(R.id.location_switch);
        mNotificationState = (Switch) view.findViewById(R.id.notification_switch);
        mConfigurationBeaconText = (TextView) view.findViewById(R.id.configuration_beacon_text);

        // Initial setup of switches to show the actual state
        mLocationSwitch.setChecked(NimbeesClient.getLocationManager().isTracking());
        mNotificationState.setChecked(mPrefs.getBoolean("notifications", true));

        // Check whether the device supports Bluetooth Low Energy
        if (NimbeesClient.getBeaconManager().deviceSupportsBle()) {
            mBeaconSwitch.setChecked(NimbeesClient.getBeaconManager().isMonitoring());
            mBeaconSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        try {
                            NimbeesClient.getBeaconManager().startMonitoring();
                        } catch (NimbeesException e) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error_starting_beacon_monitoring) + e.getMessage(), Toast.LENGTH_LONG).show();
                            mBeaconSwitch.setChecked(false);
                        }
                    } else {
                        NimbeesClient.getBeaconManager().stopMonitoring();
                    }
                }
            });
        } else {
            mBeaconSwitch.setVisibility(View.GONE);
            mConfigurationBeaconText.setText(getString(R.string.beacon_error));
        }

        // Switch for Notifications
        mNotificationState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNotificationStatus(mNotificationState.isChecked());
            }
        });

        // Switch for Location Manager
        mLocationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationSwitch.isChecked()) {
                    NimbeesClient.getLocationManager().startTracking();

                } else {
                    NimbeesClient.getLocationManager().stopTracking();
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

                mEditor = mPrefs.edit();
                mEditor.putBoolean(KEY_NOTIFICATIONS, newStatus);
                mEditor.apply();
            }

            @Override
            public void onFailure(NimbeesException e) {
                mNotificationState.setChecked(!newStatus);
                mEditor = mPrefs.edit();
                mEditor.putBoolean(KEY_NOTIFICATIONS, !newStatus);
                mEditor.apply();
            }
        });
    }

}