package com.nimbees.newdemo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.ui.activity.NavigationActivity;

/**
 * Empty fragment shown when the user is not still registered on nimBees.
 */
public class EmptyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_empty));
        return view;
    }
}
