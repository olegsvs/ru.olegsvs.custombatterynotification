package ru.olegsvs.custombatterynotification;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;

/**
 * Created by olegsvs on 11.05.2017.
 */

public class BatteryManager implements Parcelable {
    public static final String SYS_BATTERY_CAPACITY = "/sys/class/power_supply/battery/capacity";
    public static final String SYS_BATTERY_STATUS = "/sys/class/power_supply/battery/status";
    public static final String SYS_BATTERY_CAPACITY_JSR = "/sys/class/power_supply/batteryjsr/capacity";
    public static final String SYS_BATTERY_STATUS_JSR = "/sys/class/power_supply/batteryjsr/status";
    private String typeBattery = null;
    public boolean isSupport = false;

    BatteryManager(String typeBattery) {
        if(isSupportCheck(typeBattery))
            this.typeBattery = typeBattery;
    }

    protected BatteryManager(Parcel in) {
        typeBattery = in.readString();
        isSupport = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeBattery);
        dest.writeByte((byte) (isSupport ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BatteryManager> CREATOR = new Creator<BatteryManager>() {
        @Override
        public BatteryManager createFromParcel(Parcel in) {
            return new BatteryManager(in);
        }

        @Override
        public BatteryManager[] newArray(int size) {
            return new BatteryManager[size];
        }
    };

    private boolean isSupportCheck(String typeBattery) {
        File file = new File(typeBattery);
        if (file.exists()) {
            Log.w(SettingsActivity.TAG, "isSupportCheck:  " + typeBattery + " exists!");
            return isSupport = true;
        }
        Log.w(SettingsActivity.TAG, "isSupportCheck:  " + typeBattery + " NOT exists!");
        return isSupport = false;
    }

    public String getValues() {
        if(isSupport) {
            File file = new File(typeBattery);
                return OneLineReader.getValue(file);
        }
        return  "0";
    }
}
