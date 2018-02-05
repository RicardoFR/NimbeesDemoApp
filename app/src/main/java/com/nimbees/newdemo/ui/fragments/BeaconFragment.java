package com.nimbees.newdemo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.ui.activity.NavigationActivity;
import com.nimbees.newdemo.ui.adapters.BeaconAdapter;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;
import com.nimbees.platform.beacons.model.Nimbeacon;
import com.nimbees.platform.callbacks.NimbeaconDownloadCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;


/**
 * This BeaconFragment lets you see all the Beacons defined in your app.
 * <p/>
 * Feel the Beacon power!
 */

public class BeaconFragment extends Fragment {

    /**
     * Wheel to show load process of the Beacons.
     */
    private ProgressWheel mWheel;

    /**
     * Adapter to show List of NimbeesBeacon in RecyclerView.
     */
    private BeaconAdapter mAdapter;

    /**
     * Button to get all Beacons.
     */
    private Button mButtonGetBeacons;

    /**
     * RecyclerView to show beacons in the UI.
     */
    private RecyclerView mRecyclerView;

    /**
     * View to show when no beacons are saved.
     */
    private TextView mEmptyView;

    /**
     * LinearLayout for Adapters.
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * List of beacons for Adapters.
     */
    private List<Nimbeacon> mBeaconsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section4));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beacon, container, false);

        // start UI elements
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_beacon);
        mButtonGetBeacons = (Button) view.findViewById(R.id.button_get_beacons);
        mWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel_beacons);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);

        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mWheel.setVisibility(View.INVISIBLE);

        // Load beacons from the server when clicking the Get Beacons button
        mButtonGetBeacons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWheel.setVisibility(View.VISIBLE);
                getAllBeacons();
            }
        });

        showBeaconList();
        ((NavigationActivity)getActivity()).scanBeacons();


        return view;
    }


    /**
     * Show the list of locally saved beacons.
     */
    private void showBeaconList() {
        if (mBeaconsList == null) {
            mBeaconsList = new ArrayList<>();
        } else {
            mBeaconsList.clear();
        }

        // Creates the adapter
        if (mAdapter == null) {
            mAdapter = new BeaconAdapter(mBeaconsList);
            mRecyclerView.setAdapter(mAdapter);
        }

        mBeaconsList.addAll(NimbeesClient.getBeaconManager().getLocalBeacons());
        if (mBeaconsList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Calls the server to retrieve all the beacons and save them locally. They can be recovered later by calling
     * NimbeesClient.getBeaconManager().getBeacons();
     */
    private void getAllBeacons() {

        // Remove all beacons before download updates
        NimbeesClient.getBeaconManager().clearLocalBeacons();

        NimbeesClient.getBeaconManager().downloadBeaconsAndRules(new NimbeaconDownloadCallback() {
            @Override
            public void onSuccess(int i) {
                showBeaconList();
                mWheel.setVisibility(View.INVISIBLE);
                ((NavigationActivity)getActivity()).scanBeacons();
            }

            @Override
            public void onFailure(NimbeesException e) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_loading_beacons), Toast.LENGTH_LONG).show();
                mWheel.setVisibility(View.INVISIBLE);
            }
        });
    }
}
