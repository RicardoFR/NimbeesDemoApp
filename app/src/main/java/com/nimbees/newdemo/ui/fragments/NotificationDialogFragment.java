package com.nimbees.newdemo.ui.fragments;


import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.nimbees.newdemo.R;

import java.text.SimpleDateFormat;


public class NotificationDialogFragment extends DialogFragment {

    /**
     * Keys used to receive the title and the text for the notification.
     */
    private static String KEY_NOTIFICATION_ID = "NOTIFICATION_ID";

    /**
     * Title
     */
    private static String mTitle;

    /**
     * Text
     */
    private String mText;

    /**
     * The id notification
     */
    private Long notificationId;

    /**
     * The text of the notification
     */
    private TextView mTitleView;

    /**
     * The text of the notification
     */
    private TextView mTextView;

    /**
     * Date format
     */
    private SimpleDateFormat printFormat;

    public static NotificationDialogFragment newInstance(Long notificationId) {
        NotificationDialogFragment f = new NotificationDialogFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_NOTIFICATION_ID, notificationId);
        f.setArguments(args);
        return f;
    }

    public NotificationDialogFragment() {
    }

    // Required empty public constructor
    @SuppressLint("ValidFragment")
    public NotificationDialogFragment(String title, String text) {
        mTitle = title;
        mText = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printFormat = new SimpleDateFormat("HH:mm");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification_display_dialog, container, false);
        // Inflate the layout for this fragment

        mTitleView = (TextView) v.findViewById(R.id.fragment_notification_title);
        mTextView = (TextView) v.findViewById(R.id.fragment_notification_text);

        mTitleView.setText(mTitle);
        mTextView.setText(mText);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

}
