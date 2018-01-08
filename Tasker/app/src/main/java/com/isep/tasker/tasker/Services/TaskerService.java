package com.isep.tasker.tasker.Services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.isep.tasker.tasker.Domain.Note;
import com.isep.tasker.tasker.R;

/**
 * Created by davidpinheiro on 02/01/2018.
 */

public class TaskerService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("create geofence");
        Bundle args = intent.getBundleExtra("bundle");
        Note note = (Note) args.getSerializable("note");
        note.getReminder().getListLocations().forEach(location -> GeofenceUtils.createGeofence(new Activity(), location, "teste", note));
        //sendNotification(context,"criado geofence");
    }

    public void setAlarm(Context context, String taskerId, Note note) {
        AlarmManager malarmMngr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);

        Intent intent = new Intent(context, TaskerService.class);
        intent.putExtra("bundle", bundle);

        PendingIntent mPendInt = PendingIntent.getBroadcast(context, 0, intent, 0);
        malarmMngr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, mPendInt);
    }

    public void sendNotification(Context context, String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("Tasker")
                        .setContentText(msg);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
}
