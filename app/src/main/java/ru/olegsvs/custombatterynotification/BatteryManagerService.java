package ru.olegsvs.custombatterynotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by user on 11.05.2017.
 */

public class BatteryManagerService extends Service{
    private final String BAT1 = "BAT1 ";
    private final String BAT2 = "BAT2 ";
    private NotificationCompat.Builder mBuilder = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(SettingsActivity.TAG, "onDestroy: BatteryManagerService destroy!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(SettingsActivity.TAG, "onStartCommand: BatteryManagerService start");
        createNotify();
        Log.i(SettingsActivity.TAG, "onStartCommand: create and show notifucation");
        return super.onStartCommand(intent, flags, startId);
    }

    private String getResults() {
        if (!BatteryManager.checkJSRSupport()) {
            return (BAT1 + BatteryManager.getValues(BatteryManager.SYS_BATTERY_CAPACITY) + "% "
                         + BatteryManager.getValues(BatteryManager.SYS_BATTERY_STATUS) + " ");

        } else {
            return (BAT1 + BatteryManager.getValues(BatteryManager.SYS_BATTERY_CAPACITY) + "% "
                    + BatteryManager.getValues(BatteryManager.SYS_BATTERY_STATUS) + " "
                    + BAT2 + BatteryManager.getValues(BatteryManager.SYS_BATTERY_CAPACITY_JSR) + " "
                    + BatteryManager.getValues(BatteryManager.SYS_BATTERY_STATUS_JSR) + " ");
        }
    }

    private void createNotify() {

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_menu_zoom)
                        .setContentTitle("Battery")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(getResults()))
                        .setOngoing(true)
                        .setContentText(getResults());
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, SettingsActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SettingsActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(444, mBuilder.build());

        final Handler myHandler;
        myHandler = new Handler();
        Runnable runnable = new Runnable(){

            @Override
            public void run() {

                if(true) {
                    Log.i(SettingsActivity.TAG, "onCreate: " + getResults());
                    mBuilder.setContentText(getResults());
                    mNotificationManager.notify(444, mBuilder.build());
                    myHandler.postDelayed(this, 2000);
                }
            }

        };

        myHandler.postDelayed(runnable, 2000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(SettingsActivity.TAG, "onCreate: creating BatteryManagerService");
    }

}
