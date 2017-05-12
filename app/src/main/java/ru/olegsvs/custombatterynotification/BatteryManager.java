package ru.olegsvs.custombatterynotification;

import android.util.Log;

import java.io.File;

/**
 * Created by olegsvs on 11.05.2017.
 */

public class BatteryManager {
    public static final String SYS_BATTERY_CAPACITY = "/sys/class/power_supply/battery/capacity";
    public static final String SYS_BATTERY_STATUS = "/sys/class/power_supply/battery/status";
    public static final String SYS_BATTERY_CAPACITY_JSR = "/sys/class/power_supply/batteryjsr/capacity";
    public static final String SYS_BATTERY_STATUS_JSR = "/sys/class/power_supply/batteryjsr/status";
    private static boolean IS_JSR_SUPPORT = false;
    private static boolean IS_STD_SUPPORT = false;

    BatteryManager() {

    }

    static public boolean isJSRSupportCheck() {
        File file = new File(SYS_BATTERY_CAPACITY_JSR);
        if (file.exists()) {
            Log.w(SettingsActivity.TAG, "isJSRSupportCheck:  " + SYS_BATTERY_CAPACITY_JSR + " exists!");
            return IS_JSR_SUPPORT = true;
        }
        Log.w(SettingsActivity.TAG, "isJSRSupportCheck:  " + SYS_BATTERY_CAPACITY_JSR + " NOT exists!");
        return IS_JSR_SUPPORT = false;
    }

    static public boolean isSTDSupportCheck() {
        File file = new File(SYS_BATTERY_CAPACITY);
        if (file.exists()) {
            Log.w(SettingsActivity.TAG, "isSTDSupportCheck:  " + SYS_BATTERY_CAPACITY + " exists!");
            return IS_STD_SUPPORT = true;
        }
        Log.w(SettingsActivity.TAG, "isSTDSupportCheck:  " + SYS_BATTERY_CAPACITY + " NOT exists!");
        return IS_STD_SUPPORT = false;
    }

    static public String getValues(String typeBattery) {
        if ((typeBattery == SYS_BATTERY_CAPACITY_JSR || typeBattery == SYS_BATTERY_STATUS_JSR) && !isJSRSupportCheck()) {
            Log.e(SettingsActivity.TAG, "getValues: " + "ERROR for get values from JSR battery paths, IS_JSR_SUPPORT = " + IS_JSR_SUPPORT);
            return "0";
        }
        if ((typeBattery == SYS_BATTERY_CAPACITY || typeBattery == SYS_BATTERY_STATUS) && !isSTDSupportCheck()) {
            Log.e(SettingsActivity.TAG, "getValues: " + "ERROR for get values from STD battery paths, IS_STD_SUPPORT = " + IS_STD_SUPPORT);
            return "0";
        }
        File file = new File(typeBattery);
          if (file.exists()) {
              Log.i(SettingsActivity.TAG, "getValues: " + typeBattery + " exists!");
              return OneLineReader.getValue(file);
          } else Log.e(SettingsActivity.TAG, "getValues: " + typeBattery + " not exists!");

        return  "0";
    }
}
