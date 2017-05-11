package ru.olegsvs.custombatterynotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by user on 11.05.2017.
 */

public class BatteryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent myIntent = new Intent(context, BatteryManagerService.class);
            context.startService(myIntent);
            Log.i(SettingsActivity.TAG, "onReceive: BatteryBroadcastReceiver exec");
        }
    }
}
