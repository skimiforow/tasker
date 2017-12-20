package com.isep.tasker.tasker.Services;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.isep.tasker.tasker.Domain.LocationPlace;

import java.util.UUID;

/**
 * Created by davidpinheiro on 19/12/2017.
 */

public class GeofenceUtils {

    private static final int GEOFENCE_RADIUS_IN_METERS = 150;
    private static final int GEOFENCE_EXPIRATION_IN_MILLISECONDS = 86400000; // 1 HOUR

    public static void createGeofence(Activity activity, LocationPlace place){
        GeofencingClient mGeofencingClient = LocationServices.getGeofencingClient(activity);

        Geofence geofence = new Geofence.Builder()
                .setRequestId(UUID.randomUUID().toString())
                .setCircularRegion(
                        place.getLatitude(),
                        place.getLongitude(),
                        GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        GeofencingRequest request = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        PendingIntent mGeofencePendingIntent = PendingIntent.getService(
                activity, 0,
                new Intent(activity, GeofenceTransitionsIntentService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        mGeofencingClient.addGeofences(request, mGeofencePendingIntent);
    }
}
