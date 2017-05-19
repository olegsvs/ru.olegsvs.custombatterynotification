package ru.olegsvs.custombatterynotification

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

import java.io.File

/**
 * Created by olegsvs on 11.05.2017.
 */

class BatteryManager : Parcelable {
    private var capacityBattery: String? = null
    private var statusBattery: String? = null
    var isSupport = false

    internal constructor(typeBattery: String?, stateBattery: String?) {
        if (typeBattery != null && stateBattery != null)
            if (isSupportCheck(typeBattery) && isSupportCheck(stateBattery)) {
                this.capacityBattery = typeBattery
                this.statusBattery = stateBattery
            } else
                isSupport = false
    }

    protected constructor(`in`: Parcel) {
        capacityBattery = `in`.readString()
        statusBattery = `in`.readString()
        isSupport = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(capacityBattery)
        dest.writeString(statusBattery)
        dest.writeByte((if (isSupport) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun isSupportCheck(capacityBattery: String): Boolean {
        val file = File(capacityBattery)
        if (file.exists()) {
            Log.w(SettingsActivity.TAG, "isSupportCheck:  $capacityBattery exists!")
            isSupport = true
            return isSupport
        }
        Log.w(SettingsActivity.TAG, "isSupportCheck:  $capacityBattery NOT exists!")
        isSupport = false
        return isSupport
    }

    val capacity: String
        get() {
            if (isSupport) {
                val file = File(capacityBattery!!)
                return OneLineReader.getValue(file)
            }
            return "0"
        }

    val state: String
        get() {
            if (isSupport) {
                val file = File(statusBattery!!)
                return " " + OneLineReader.getValue(file)
            }
            return " - "
        }

    companion object {

        val CREATOR: Parcelable.Creator<BatteryManager> = object : Parcelable.Creator<BatteryManager> {
            override fun createFromParcel(`in`: Parcel): BatteryManager {
                return BatteryManager(`in`)
            }

            override fun newArray(size: Int): Array<out BatteryManager?> = arrayOfNulls(size)
        }
    }
}
