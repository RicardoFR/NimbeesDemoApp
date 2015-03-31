package com.nimbees.newdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nimbees.newdemo.R;
import com.nimbees.platform.beans.NimbeesBeacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 25/02/15.
 * <p/>
 * This adapter works with the RecyclerView of beacons to show each element in the UI
 */

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.BeaconViewHolder> {

    private List<NimbeesBeacon> mBeaconsList = new ArrayList<NimbeesBeacon>();

    public BeaconAdapter(List<NimbeesBeacon> mBeaconsList) {
        this.mBeaconsList = mBeaconsList;
    }

    @Override
    public int getItemCount() {
        return mBeaconsList.size();
    }

    @Override
    public void onBindViewHolder(BeaconViewHolder BeaconViewHolder, int i) {

        NimbeesBeacon nimbeesBeacon = mBeaconsList.get(i);
        BeaconViewHolder.vName.setText(nimbeesBeacon.getName());
        BeaconViewHolder.vEnter.setText(nimbeesBeacon.getEnterMessage());
        BeaconViewHolder.vExit.setText(nimbeesBeacon.getExitMessage());
    }

    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beacon_item, viewGroup, false);

        return new BeaconViewHolder(itemView);
    }

    public static class BeaconViewHolder extends RecyclerView.ViewHolder {

        TextView vName;
        TextView vEnter;
        TextView vExit;

        public BeaconViewHolder(View v) {
            super(v);

            vName = (TextView) v.findViewById(R.id.beacon_name);
            vEnter = (TextView) v.findViewById(R.id.beacon_enter);
            vExit = (TextView) v.findViewById(R.id.beacon_exit);
        }
    }
}
