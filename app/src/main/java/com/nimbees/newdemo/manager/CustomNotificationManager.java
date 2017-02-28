package com.nimbees.newdemo.manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.nimbees.newdemo.App;
import com.nimbees.newdemo.R;
import com.nimbees.newdemo.ui.activity.NavigationActivity;
import com.nimbees.platform.NimbeesNotificationManager;

import java.util.Map;

/**
 * This CustomNotificationManager lets handle a CustomMessage to do what you want with it
 * and a handleScreenTransition to open the app and show any content to the user.
 * <p/>
 * Simple & Funny
 */

public class CustomNotificationManager extends NimbeesNotificationManager {

    /**
     * Screen name received in the notification for the Maps screen.
     */
    public static String MAP_SCREEN_NAME = "MAP";
    public static String NOTIFICATIONS_SCREEN_NAME = "NOTIFICATIONS";

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
    public void handleCustomMessage(long idNotification, String content, Map<String, String> additionalContent) {
        super.handleCustomMessage(idNotification, content, additionalContent);
        showNotification(idNotification, content, additionalContent);
    }

    /**
     * Receive a Screen Transition Message.
     * <p/>
     * In this example, we call the NavigationActivity and add in the arguments Bundle the
     * screen that we want to load when the user opens the notification.
     *
     * @param idNotification the unique id of the notification
     * @param content        the content of the message
     * @param screen         the name of the screen that we want to load.
     */
    @Override
    public void handleScreenTransitionMessage(long idNotification, String content, String screen, Map<String, String> additionalContent) {
        super.handleScreenTransitionMessage(idNotification, content, screen, additionalContent);
        // Show the MAPS screen if it has been received, show a standard notification otherwise.
        if (screen.equals(MAP_SCREEN_NAME) || screen.equals(NOTIFICATIONS_SCREEN_NAME)) {
            Bundle args = new Bundle();
            args.putString(NavigationActivity.KEY_SCREEN_TO_SHOW, screen);
            //   showNotificationWithScreenTransitionHandle(idNotification,content,args);
            //   Intent intent = new Intent(App.getContext(), NavigationActivity.class);
            //    intent.putExtras(args);
            //    App.getContext().startActivity(intent);
            showNotification(idNotification, content, additionalContent, NavigationActivity.class, args);
        } else {
            showNotification(idNotification, content, additionalContent);
        }
    }

    /**
     * This method shows a notification
     *
     * @param title The notification title
     * @param text  The notification text
     */
    public static void showBeaconNotification(String title, String text) {

        vibrateAndBeep();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.getContext())
                .setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.ic_launcher)))
                .setSmallIcon(R.drawable.ic_stat_bee) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(text) // message for notification
                .setAutoCancel(true); // clear notification after click

        Intent intent = new Intent(App.getContext(), NavigationActivity.class);
        // Extra info for beacon
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        PendingIntent pi = PendingIntent.getActivity(App.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    /**
     * This method play the Deafult notification sound and vibrates the telephone
     */
    private static void vibrateAndBeep() {

        // Vibration and sound
        Vibrator v = (Vibrator) App.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for x milliseconds
        v.vibrate(1000);
        v.vibrate(500);
        v.vibrate(1000);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(App.getContext(), notification);
            r.play();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}