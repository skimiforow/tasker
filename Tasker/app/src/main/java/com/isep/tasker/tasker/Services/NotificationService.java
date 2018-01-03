package com.isep.tasker.tasker.Services;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.isep.tasker.tasker.R;

/**
 * Created by davidpinheiro on 02/01/2018.
 */

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("message"));
        sendNotification(remoteMessage.getData().get("tasker_id"));
        new TaskerService().setAlarm(this, remoteMessage.getData().get("tasker_id"));
    }

    public void sendNotification(String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("Tasker")
                        .setContentText(msg);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
}