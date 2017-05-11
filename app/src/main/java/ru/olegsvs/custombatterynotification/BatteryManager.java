package ru.olegsvs.custombatterynotification;

import java.io.File;

/**
 * Created by user on 11.05.2017.
 */

public class BatteryManager {
    public static final String SYS_BATTERY_CAPACITY = "/sys/class/power_supply/battery/capacity";
    public static final String SYS_BATTERY_STATUS = "/sys/class/power_supply/battery/status";
    public static final String SYS_BATTERY_CAPACITY_JSR = "/sys/class/power_supply/batteryjsr/capacity";
    public static final String SYS_BATTERY_STATUS_JSR = "/sys/class/power_supply/batteryjsr/status";
    public boolean IS_JSR_SUPPORT = false;

    BatteryManager() {

    }

    static public String getValues(String typeBattery) {
        File file = null;
        file = new File(typeBattery);
        if (file.exists()) {
            return OneLineReader.getValue(file);
        }
        return null;
    }
}
