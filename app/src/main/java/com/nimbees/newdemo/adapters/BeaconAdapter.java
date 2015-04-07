package com.nimbees.newdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(BeaconViewHolder beaconViewHolder, int i) {
        NimbeesBeacon nimbeesBeacon = mBeaconsList.get(i);
        beaconViewHolder.vName.setText(nimbeesBeacon.getName());
        beaconViewHolder.vEnter.setText(nimbeesBeacon.getEnterMessage());
        beaconViewHolder.vExit.setText(nimbeesBeacon.getExitMessage());
    }

    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beacon_item, viewGroup, false);

        return new BeaconViewHolder(itemView, new BeaconViewHolder.IBeaconViewHolderClickListener() {
            @Override
            public void onBeaconClick(Integer position) {
                Log.i("COSITO", String.valueOf(mBeaconsList.get(position).getId()));
            }
        });
    }

    public static class BeaconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView vName;
        public TextView vEnter;
        public TextView vExit;
        public IBeaconViewHolderClickListener mListener;

        public BeaconViewHolder(View v, IBeaconViewHolderClickListener listener) {
            super(v);

            vName = (TextView) v.findViewById(R.id.beacon_name);
            vEnter = (TextView) v.findViewById(R.id.beacon_enter);
            vExit = (TextView) v.findViewById(R.id.beacon_exit);

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
