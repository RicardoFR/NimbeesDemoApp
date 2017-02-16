package com.nimbees.newdemo.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nimbees.newdemo.R;
import com.nimbees.platform.beacons.model.Nimbeacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 25/02/15.
 * <p/>
 * This adapter works with the RecyclerView of beacons to show each element in the UI
 */

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.BeaconViewHolder> {

    /**
     * The beacon list for adapter
     */
    private List<Nimbeacon> mBeaconsList = new ArrayList<Nimbeacon>();

    public BeaconAdapter(List<Nimbeacon> mBeaconsList) {
        this.mBeaconsList = mBeaconsList;
    }

    @Override
    public int getItemCount() {
        return mBeaconsList.size();
    }

    @Override
    public void onBindViewHolder(BeaconViewHolder beaconViewHolder, int i) {
        Nimbeacon nimbeesBeacon = mBeaconsList.get(i);
        beaconViewHolder.vName.setText(nimbeesBeacon.getName());
        beaconViewHolder.vEnter.setText(nimbeesBeacon.getDescription());
        beaconViewHolder.vExit.setText("Rules : " + nimbeesBeacon.getNimbeaconActionList().size());
    }

    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beacon_item, viewGroup, false);

        return new BeaconViewHolder(itemView, new BeaconViewHolder.IBeaconViewHolderClickListener() {
            @Override
            public void onBeaconClick(Integer position) {
                Log.i("Beacon", String.valueOf(mBeaconsList.get(position).getIdBeacon()));
            }
        });
    }

     static class BeaconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView vName;
         TextView vEnter;
         TextView vExit;
         IBeaconViewHolderClickListener mListener;

         BeaconViewHolder(View v, IBeaconViewHolderClickListener listener) {
            super(v);

            vName = (TextView) v.findViewById(R.id.beacon_name);
            vEnter = (TextView) v.findViewById(R.id.beacon_info);
            vExit = (TextView) v.findViewById(R.id.beacon_rules);

            mListener = listener;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onBeaconClick(getAdapterPosition());
        }

        private interface IBeaconViewHolderClickListener {
            void onBeaconClick(Integer position);
        }
    }
}
