package ru.olegsvs.custombatterynotification;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;

/**
 * Created by olegsvs on 11.05.2017.
 */

public class BatteryManager implements Parcelable {
    private String typeBattery = null;
    private String stateBattery = null;
    public boolean isSupport = false;

    BatteryManager(String typeBattery, String stateBattery) {
        if((typeBattery != null) && (stateBattery != null))
            if(isSupportCheck(typeBattery) && isSupportCheck(stateBattery)) {
                this.typeBattery = typeBattery;
                this.stateBattery = stateBattery;
            } else isSupport = false;
    }

    protected BatteryManager(Parcel in) {
        typeBattery = in.readString();
        stateBattery = in.readString();
        isSupport = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeBattery);
        dest.writeString(stateBattery);
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

    public String getCapacity() {
        if(isSupport) {
            File file = new File(typeBattery);
                return OneLineReader.getValue(file);
        }
        return  "0";
    }

    public String getState() {
        if(isSupport) {
            File file = new File(stateBattery);
            return " " + OneLineReader.getValue(file);
        }
        return  " - ";
    }
}
