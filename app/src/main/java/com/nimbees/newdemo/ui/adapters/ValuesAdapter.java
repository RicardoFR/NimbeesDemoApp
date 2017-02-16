package com.nimbees.newdemo.ui.adapters;

/**
 * This adapter works with the RecyclerView of values from Tags to show each element in the UI
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nimbees.newdemo.R;

import java.util.List;

public class ValuesAdapter extends RecyclerView.Adapter<ValuesAdapter.ValuesViewHolder> {

    /**
     * The item list to show in the adapter
     */
    private List<String> items;

    public ValuesAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ValuesViewHolder ValuesViewHolder, int i) {

        ValuesViewHolder.vValue.setText(items.get(i));
    }

    @Override
    public ValuesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tag_list_item, viewGroup, false);

        return new ValuesViewHolder(itemView);
    }

    public static class ValuesViewHolder extends RecyclerView.ViewHolder {

        TextView vValue;

        public ValuesViewHolder(View v) {
            super(v);
            vValue = (TextView) v.findViewById(R.id.tag_value);
        }
    }
}