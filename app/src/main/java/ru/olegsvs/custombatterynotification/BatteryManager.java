package ru.olegsvs.custombatterynotification;

/**
 * Created by user on 11.05.2017.
 */

public class BatteryManager {
    private static final String SYS_BATTERY_CAPACITY = "/sys/class/power_supply/battery/capacity";
    private static final String SYS_BATTERY_STATUS = "/sys/class/power_supply/battery/status";
    private static final String SYS_BATTERY_CAPACITY_JSR = "/sys/class/power_supply/batteryjsr/capacity";
    private static final String SYS_BATTERY_STATUS_JSR = "/sys/class/power_supply/batteryjsr/status";
    public boolean IS_JSR_SUPPORT = false;

    BatteryManager() {

    }
}
