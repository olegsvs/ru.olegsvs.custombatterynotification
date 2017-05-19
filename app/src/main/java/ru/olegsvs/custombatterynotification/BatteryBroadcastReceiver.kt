package ru.olegsvs.custombatterynotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log

/**
 * Created by olegsvs on 11.05.2017.
 */

class BatteryBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {

            val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            if (sharedPref.getBoolean("serviceAutoStart", false)) {
                val myIntent = Intent(context, BatteryManagerService::class.java)
                context.startService(myIntent)
                Log.w(SettingsActivity.TAG, "BatteryBroadcastReceiver: BatteryBroadcastReceiver exec")
            } else {
                Log.w(SettingsActivity.TAG, "BatteryBroadcastReceiver: autostart service is disabled")
            }
        }
    }
}
