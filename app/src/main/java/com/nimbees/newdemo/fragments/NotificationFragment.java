package com.nimbees.newdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.newdemo.adapters.NotificationAdapter;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.beans.NimbeesMessage;

import java.util.Collections;
import java.util.List;

/**
 * NotificationFragment lets see all notifications received by the device, and whether they were opened or not.
 *
 * Bzzzz, Bzzzz!
 */

public class NotificationFragment extends Fragment {

    /** RecyclerView to show the notifications. */
    private RecyclerView mRecyclerView;

    /** View to show when no messages have been received. */
    private TextView mEmptyView;

    /** The LinearLayourManager to be used in the adapter. */
    private LinearLayoutManager mLayoutManager;

    /** The adapter to show each element of the mMessageList. */
    private NotificationAdapter mAdapter;

    /** List that contains all messages and is used in the adapter. */
    private List<NimbeesMessage> mMessageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call to the MessageHistory to populate the mMessageList using the adapter
        mMessageList = NimbeesClient.getMessageManager().getMessageHistory(null, null);
        Collections.reverse(mMessageList);
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Create and configure the views
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter
        mAdapter = new NotificationAdapter(mMessageList);
        mRecyclerView.setAdapter(mAdapter);

        if (mMessageList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }

        return view;
    }

}
