package ru.olegsvs.custombatterynotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
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
        if (!BatteryManagerService.isMyServiceRunning(BatteryManagerService.class,this)) {
            Intent intent = new Intent(this, BatteryManagerService.class);
            startService(intent);
        }
    }
}
