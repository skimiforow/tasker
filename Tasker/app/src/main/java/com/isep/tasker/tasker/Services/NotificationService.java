package com.isep.tasker.tasker.Services;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        sendNotification(remoteMessage.getData().get("msg"));
        reference.child(remoteMessage.getData().get("taskerId"));
        //new TaskerService().setAlarm(this, remoteMessage.getData().get("taskerId"));
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