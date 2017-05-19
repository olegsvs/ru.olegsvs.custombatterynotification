package ru.olegsvs.custombatterynotification

import android.util.Log

import java.io.File
import java.util.ArrayList

/**
 * Created by olegsvs on 13.05.2017.
 */

object ScannerPaths {
    var power_supply = "/sys/class/power_supply"
    private var isError = true

    val pathsPowerSupply: List<String>
        get() {
            val paths = ArrayList<String>()
            val directory = File(power_supply)
            if (directory.exists()) {
                isError = false
                val files = directory.listFiles()
                for (i in files.indices) {
                    paths.add(files[i].absolutePath)
                }
                Log.w(SettingsActivity.TAG, "ScannerPaths: power_supply dirs " + paths.toString())
            } else {
                Log.e(SettingsActivity.TAG, "getPathsPowerSupply: isError")
                isError = true
            }
            return paths
        }

    fun getPathsEntryOfPowerSupply(entry: String): List<String>? {
        if (!isError) {
            val paths = ArrayList<String>()
            val directory = File(entry)

            val files = directory.listFiles()

            for (i in files.indices) {
                paths.add(files[i].absolutePath)
            }
            Log.w(SettingsActivity.TAG, "ScannerPaths: entryFiles " + paths.toString())
            return paths
        }
        return null
    }

    fun checkPaths(): Boolean {
        pathsPowerSupply
        return isError
    }
}
