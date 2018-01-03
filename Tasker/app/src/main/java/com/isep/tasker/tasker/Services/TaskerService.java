package com.isep.tasker.tasker.Services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.isep.tasker.tasker.R;

/**
 * Created by davidpinheiro on 02/01/2018.
 */

public class TaskerService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("create geofence");
        sendNotification(context,"criado geofence");
    }

    public void setAlarm(Context context, String taskerId) {
        AlarmManager malarmMngr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TaskerService.class);
        PendingIntent mPendInt = PendingIntent.getBroadcast(context, 0, intent, 0);
        malarmMngr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, mPendInt);
    }

    public void sendNotification(Context context,String msg) {
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
