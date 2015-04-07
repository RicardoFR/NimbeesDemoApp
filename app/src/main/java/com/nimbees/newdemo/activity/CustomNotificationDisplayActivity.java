package com.nimbees.newdemo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nimbees.newdemo.R;


public class CustomNotificationDisplayActivity extends ActionBarActivity {

    public static final String KEY_MESSAGE_CONTENT = "CONTENT";
    private TextView mContentView;
    private Button mButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_custom_notification_display);

        mContentView = (TextView) findViewById(R.id.notification_content);
        mButtonView = (Button) findViewById(R.id.notification_button);

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(KEY_MESSAGE_CONTENT)) {
            mContentView.setText(getIntent().getExtras().getString(KEY_MESSAGE_CONTENT));
        } else {
            mContentView.setText("-");
        }

        mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            //finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }
}
