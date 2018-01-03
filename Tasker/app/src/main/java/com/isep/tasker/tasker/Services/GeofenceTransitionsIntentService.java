package com.isep.tasker.tasker.Services;

/**
 * Created by davidpinheiro on 19/12/2017.
 */

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.isep.tasker.tasker.LoginActivity;
import com.isep.tasker.tasker.MainActivity;
import com.isep.tasker.tasker.R;

import java.util.Calendar;
import java.util.List;

import static com.google.android.gms.location.GeofencingEvent.*;

/**
 * Created by davidpinheiro on 10/12/2017.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private final String TAG = "GEOFENCE";

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = fromIntent(intent);
        /*
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }*/

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
           /*
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );
            */

            // Send notification and log the transition details.
            sendNotification(intent);
            //Log.i(TAG, geofenceTransitionDetails);
        }
    }

    private void sendNotification(Intent intent) {
        String id =  intent.getStringExtra("id");

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
