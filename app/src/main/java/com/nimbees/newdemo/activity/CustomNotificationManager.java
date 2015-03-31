package com.nimbees.newdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nimbees.platform.NimbeesNotificationManager;

/**
 * This CustomNotificationManager lets handle a CustomMessage to do what you want with it
 * and a handleScreenTransition to open the app and show any content to the user.
 * <p/>
 * Simple & Funny
 */

public class CustomNotificationManager extends NimbeesNotificationManager {

    /**
     * The name received in the notification to check it.
     */
    private String MAP_SCREEN_NAME = "MAP";

    /**
     * The context.
     */
    private Context mContext;

    public CustomNotificationManager(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * Receive a custom Message and we can do anything here with it (by default, we call showNotification)
     *
     * @param idNotification the unique id of the notification
     * @param content        the content of the message
     */
    @Override
    public void handleCustomMessage(long idNotification, String content) {
        // Insert your code here and do cool things
        showNotification(idNotification, content);

    }

    /**
     * In this example, in handleScreenTransition we call the NavigationActivity and we add
     * in the Bundle the screen that we want to load when the device receive the notification.
     *
     * @param idNotification the unique id of the notification
     * @param content        the content of the message
     * @param screen         the name of the screen that we want to load.
     */
    @Override
    public void handleScreenTransitionMessage(long idNotification, String content, String screen) {

        if (screen.equals(MAP_SCREEN_NAME)) {

            //creation of the bundle to attach it to the intent
            Bundle args = new Bundle();
            args.putInt(NavigationActivity.KEY_SCREEN_TO_SHOW, NavigationActivity.FRAGMENT_MAP);
            showNotification(idNotification, content, NavigationActivity.class, args);

        } else {

            showNotification(idNotification, content);
        }
    }
}