package com.appbyabhi.practicequiz;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class NotificationHandler {
    private static final String CHANNEL_ID = "quiz_notifications";
    private static final String CHANNEL_NAME = "quiz_notifications";
    private static final int NOTIFICATION_ID = 100;

    public static void displayNotification(Context context, String subject) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android Oreo and above
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        nm.createNotificationChannel(channel);

        Notification notification;
        notification = new Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle("Quiz started")
                .setContentText("Your " + subject + " quiz has started!")
                .setChannelId(CHANNEL_NAME)
                .setOngoing(true)
                .build();

        nm.notify(NOTIFICATION_ID, notification);
    }

    public static void cancelNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }
}
