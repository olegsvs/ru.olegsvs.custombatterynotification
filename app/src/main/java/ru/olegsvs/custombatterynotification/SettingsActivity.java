package ru.olegsvs.custombatterynotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import static ru.olegsvs.custombatterynotification.BatteryManager.getValues;

public class SettingsActivity extends AppCompatActivity {
    public static String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView tvBattery = (TextView) findViewById(R.id.tvBattery);
        if (BatteryManager.checkJSRSupport()) Log.w(TAG, "onCreate: " + "SUPPORT" );
        tvBattery.setText("BAT1 " +  BatteryManager.getValues(BatteryManager.SYS_BATTERY_CAPACITY) + "%\n"
                +BatteryManager.getValues(BatteryManager.SYS_BATTERY_STATUS) +
                "\nBAT2 " +  BatteryManager.getValues(BatteryManager.SYS_BATTERY_CAPACITY_JSR) + "%\n"
                +BatteryManager.getValues(BatteryManager.SYS_BATTERY_STATUS_JSR));
        Log.i(SettingsActivity.TAG, "onCreate: " + tvBattery.getText());
    /*    NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_menu_zoom)
                        .setContentTitle("Battery")
                        .setContentText("BAT1: null\nBAT2: null");
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
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(444, mBuilder.build()); */
    }
}
