package com.nimbees.newdemo.fragments;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;
import com.nimbees.platform.callbacks.NimbeesRegistrationCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * This fragment allows the user to register on nimBees using a Google account as username (the
 * username can be anything else, though).
 *
 * Welcome to the hive!
 */

public class RegisterFragment extends Fragment {

    /** Request Google Accounts. */
    private int PICK_ACCOUNT_REQUEST = 0;

    /**
     * Button to register.
     */
    private ButtonRectangle mRegisterButton;

    /**
     * TextView to show registered email.
     */
    private TextView mEmailText;

    /**
     *  Wheel show on screen in transition.
     */
    private ProgressWheel mWheel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section1));


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // start UI elements
        mRegisterButton = (ButtonRectangle) view.findViewById(R.id.button_register);
        mEmailText = (TextView) view.findViewById(R.id.editText_email);
        mWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);

        // We set in the textview the email stored in SharedPreferences, by default ""
        if (NimbeesClient.getUserManager().getUserData() != null) {
            mEmailText.setText(NimbeesClient.getUserManager().getUserData().getAlias());
            mEmailText.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            mEmailText.setText(getString(R.string.not_registered));
            mEmailText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        mWheel.setVisibility(View.INVISIBLE);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoogleAccountPicker();
                mWheel.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    /**
     * Register a user in nimBees Platform.
     *
     * @param username name or email to register
     * @param v        the view
     */
    private void registerUser(final String username, View v) {

        NimbeesClient.getUserManager().register(username, new NimbeesRegistrationCallback() {
            @Override
            public void onSuccess() {
                mEmailText.setText(username);
                mEmailText.setTextColor(getResources().getColor(android.R.color.black));
                mWheel.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(NimbeesException e) {
                Toast.makeText(getActivity(), getResources().getText(R.string.register_fail), Toast.LENGTH_LONG).show();
                mWheel.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Shows the Google account picker to let the user pick an account.
     */
    private void showGoogleAccountPicker() {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(googlePicker, PICK_ACCOUNT_REQUEST);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == NavigationActivity.RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            registerUser(accountName, getView());
        } else {
            mWheel.setVisibility(View.INVISIBLE);
        }
    }
}