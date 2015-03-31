package com.nimbees.newdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.newdemo.adapters.NotificationAdapter;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.beans.NimbeesMessage;

import java.util.List;

/**
 * This NotificationFragment lets see all notifications received by
 * the device, and which was opened or not.
 *
 * Bzzzz, Bzzzz!
 */

public class NotificationFragment extends Fragment {

    /**
     * RecyclerView to show the notifications.
     */
    private RecyclerView mRecyclerView;

    /**
     * The LinearLayourManager to be used in the adapter.
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * The adapter to show each element of the mMessageList.
     */
    private NotificationAdapter mAdapter;

    /**
     * The list wich contails all messages and is used in the adapter.
     */
    private List<NimbeesMessage> mMessageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Call to the MessageHistory to populate the mMessageList using the adapter
        mMessageList = NimbeesClient.getMessageManager().getMessageHistory(null, null);
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section2));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new NotificationAdapter(mMessageList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
