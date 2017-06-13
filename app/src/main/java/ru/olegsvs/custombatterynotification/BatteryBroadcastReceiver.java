package ru.olegsvs.custombatterynotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by olegsvs on 11.05.2017.
 */

public class BatteryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) { //check intent with action BOOT_COMPLETED
            SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE); //load prefs to check parameters
            if(sharedPref.getBoolean("serviceAutoStart", false)) { //if auto start is enabled
                Intent myIntent = new Intent(context, BatteryManagerService.class);
                context.startService(myIntent); //start service
                Log.w(SettingsActivity.TAG, "BatteryBroadcastReceiver: onReceive exec");
            } else {
                Log.w(SettingsActivity.TAG, "BatteryBroadcastReceiver: autostart service is disabled"); //if auto start is disabled
            }
        }
    }
}
