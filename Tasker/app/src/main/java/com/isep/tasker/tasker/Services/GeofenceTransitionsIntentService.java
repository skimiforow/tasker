package com.isep.tasker.tasker.Services;

/**
 * Created by davidpinheiro on 19/12/2017.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.isep.tasker.tasker.LoginActivity;
import com.isep.tasker.tasker.R;

/**
 * Created by davidpinheiro on 10/12/2017.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private final String TAG = "GEOFENCE";

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    protected void onHandleIntent(Intent intent) {
        sendNotification(intent);
    }

    private void sendNotification(Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        new Intent(this, LoginActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(intent.getStringExtra("title"))
                        .setContentText(intent.getStringExtra("description"))
                        .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }

}
