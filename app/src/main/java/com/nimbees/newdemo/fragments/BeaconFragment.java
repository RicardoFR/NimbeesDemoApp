package com.nimbees.newdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonRectangle;
import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.newdemo.adapters.BeaconAdapter;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;
import com.nimbees.platform.beans.NimbeesBeacon;
import com.nimbees.platform.callbacks.NimbeesCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * This BeaconFragment lets see all the Beacons defined in your app
 *
 * Feel the Beacon power!
 */

public class BeaconFragment extends Fragment {

    /**
     * Wheel to show load process of the Beacons
     */
    private ProgressWheel mWheel;

    /**
     * Adapter to show List of NimbeesBeacon in RecyclerView.
     */
    private BeaconAdapter mAdapter;

    /**
     * Button to get all Beacons.
     */
    private ButtonRectangle mButtonGetBeacons;

    /**
     * RecyclerView to show beacons in the UI.
     */
    private RecyclerView mRecyclerView;

    /**
     * LinearLayout for Adapters.
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * List of beacons for Adapters.
     */
    private List<NimbeesBeacon> mBeaconsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBeaconsList = new ArrayList<NimbeesBeacon>();
        // Call getBeacons here to load on the UI the Beacons stored
        mBeaconsList = NimbeesClient.getBeaconManager().getBeacons();
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section4));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beacon, container, false);

        // start UI elements
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_beacon);
        mButtonGetBeacons = (ButtonRectangle) view.findViewById(R.id.button_get_beacons);
        mWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel_beacons);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BeaconAdapter(mBeaconsList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mWheel.setVisibility(View.INVISIBLE);
        //  Button to call method to get Beacons from server
        mButtonGetBeacons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWheel.setVisibility(View.VISIBLE);
                getAllBeacons();
            }
        });

        return view;
    }

    /**
     * Add beacons actually in phone to the list showed in the UI
     */
    private void setElementsOnUI() {

        List<NimbeesBeacon> auxList;
        mBeaconsList.clear();
        auxList = NimbeesClient.getBeaconManager().getBeacons();
        for (NimbeesBeacon nimbeesBeacon : auxList) {
            mBeaconsList.add(nimbeesBeacon);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This method call server to get all Beacons and save it in the phone to be
     * recovered with NimbeesClient.getBeaconManager().getBeacons();
     */
    private void getAllBeacons() {

        NimbeesClient.getBeaconManager().loadBeaconsFromServer(new NimbeesCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                setElementsOnUI();
                mWheel.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(NimbeesException e) {
            }
        });
    }
}
