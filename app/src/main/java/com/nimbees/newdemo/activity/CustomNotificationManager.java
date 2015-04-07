package com.nimbees.newdemo.activity;

import android.content.Context;
import android.os.Bundle;

import com.nimbees.platform.NimbeesNotificationManager;

/**
 * This CustomNotificationManager lets handle a CustomMessage to do what you want with it
 * and a handleScreenTransition to open the app and show any content to the user.
 * <p/>
 * Simple & Funny
 */

public class CustomNotificationManager extends NimbeesNotificationManager {

    /** Screen name received in the notification for the Maps screen. */
    private String MAP_SCREEN_NAME = "MAP";

    /**
     * Default constructor.
     *
     * @param context Application context.
     */
    public CustomNotificationManager(Context context) {
        super(context);
    }

    /**
     * Receive a custom Message, we can do anything here with it.
     *
     * @param idNotification the unique id of the notification
     * @param content        the content of the message
     */
    @Override
    public void handleCustomMessage(long idNotification, String content) {
        // Show the notification using a custom Activity... or do anything else you want with it!
        Bundle extras = new Bundle();
        extras.putString(CustomNotificationDisplayActivity.KEY_MESSAGE_CONTENT, content);
        showNotification(idNotification, content, CustomNotificationDisplayActivity.class, extras);
    }

    /**
     * Receive a Screen Transition Message.
     *
     * In this example, we call the NavigationActivity and add in the arguments Bundle the
     * screen that we want to load when the user opens the notification.
     *
     * @param idNotification the unique id of the notification
     * @param content        the content of the message
     * @param screen         the name of the screen that we want to load.
     */
    @Override
    public void handleScreenTransitionMessage(long idNotification, String content, String screen) {

        // Show the MAPS screen if it has been received, show a standard notification otherwise.
        if (screen.equals(MAP_SCREEN_NAME)) {
            Bundle args = new Bundle();
            args.putInt(NavigationActivity.KEY_SCREEN_TO_SHOW, NavigationActivity.FRAGMENT_MAP);
            showNotification(idNotification, content, NavigationActivity.class, args);
        } else {
            showNotification(idNotification, content);
        }
    }
}