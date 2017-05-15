package ru.olegsvs.custombatterynotification;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;

/**
 * Created by olegsvs on 11.05.2017.
 */

public class BatteryManager implements Parcelable {
    private String capacityBattery = null;
    private String statusBattery = null;
    public boolean isSupport = false;

    BatteryManager(String typeBattery, String stateBattery) {
        if((typeBattery != null) && (stateBattery != null))
            if(isSupportCheck(typeBattery) && isSupportCheck(stateBattery)) {
                this.capacityBattery = typeBattery;
                this.statusBattery = stateBattery;
            } else isSupport = false;
    }

    protected BatteryManager(Parcel in) {
        capacityBattery = in.readString();
        statusBattery = in.readString();
        isSupport = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(capacityBattery);
        dest.writeString(statusBattery);
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

    private boolean isSupportCheck(String capacityBattery) {
        File file = new File(capacityBattery);
        if (file.exists()) {
            Log.w(SettingsActivity.TAG, "isSupportCheck:  " + capacityBattery + " exists!");
            return isSupport = true;
        }
        Log.w(SettingsActivity.TAG, "isSupportCheck:  " + capacityBattery + " NOT exists!");
        return isSupport = false;
    }

    public String getCapacity() {
        if(isSupport) {
            File file = new File(capacityBattery);
                return OneLineReader.getValue(file);
        }
        return  "0";
    }

    public String getState() {
        if(isSupport) {
            File file = new File(statusBattery);
            return " " + OneLineReader.getValue(file);
        }
        return  " - ";
    }
}
