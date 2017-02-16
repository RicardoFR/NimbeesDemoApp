package com.nimbees.newdemo.ui.fragments;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.ui.activity.NavigationActivity;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;
import com.nimbees.platform.callbacks.NimbeesRegistrationCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.UUID;

/**
 * This fragment allows the user to register on nimBees using a Google account as username (the
 * username can be anything else, though).
 * <p/>
 * Welcome to the hive!
 */

public class RegisterFragment extends Fragment {

    /**
     * Request Google Accounts.
     */
    private int PICK_ACCOUNT_REQUEST = 0;

    /**
     * Button to register.
     */
    private Button mRegisterButton;

    /**
     * TextView to show registered email.
     */
    private TextView mEmailText;

    /**
     * Wheel show on screen in transition.
     */
    private ProgressWheel mWheel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section0));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // start UI elements
        mRegisterButton = (Button) view.findViewById(R.id.button_register);
        mEmailText = (TextView) view.findViewById(R.id.editText_email);
        mWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);

        // We set in the textview the email stored in SharedPreferences, by default ""
        if (NimbeesClient.getUserManager().getUserData() != null) {
            mEmailText.setText(getString(R.string.user_id) + " " + NimbeesClient.getUserManager().getUserData().getAlias());
            mEmailText.setTextColor(getResources().getColor(android.R.color.black));
            mRegisterButton.setVisibility(View.INVISIBLE);

        } else {
            mEmailText.setText(getString(R.string.not_registered));
            mEmailText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            mRegisterButton.setVisibility(View.VISIBLE);

        }

        mWheel.setVisibility(View.INVISIBLE);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showGoogleAccountPicker();
                //mWheel.setVisibility(View.VISIBLE);
                // random username register
                registerUser(UUID.randomUUID().toString());
                mWheel.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    /**
     * Register a user in nimBees Platform.
     *
     * @param username name or email to register
     */
    private void registerUser(final String username) {

        NimbeesClient.getUserManager().register(username, new NimbeesRegistrationCallback() {
            @Override
            public void onSuccess() {
                mEmailText.setText(getString(R.string.user_id) + " " + username);
                mEmailText.setTextColor(getResources().getColor(android.R.color.black));
                mWheel.setVisibility(View.INVISIBLE);
                mRegisterButton.setVisibility(View.INVISIBLE);
                Snackbar.make(getView(), getString(R.string.user_registered), Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(NimbeesException e) {
                Toast.makeText(getActivity(), getResources().getText(R.string.register_fail), Toast.LENGTH_LONG).show();
                mWheel.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == NavigationActivity.RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            registerUser(accountName);
        } else {
            mWheel.setVisibility(View.INVISIBLE);
        }
    }
}