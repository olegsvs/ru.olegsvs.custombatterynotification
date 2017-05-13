package ru.olegsvs.custombatterynotification;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olegsvs on 13.05.2017.
 */

public class ScannerPaths {
    public static String power_supply = "/sys/class/power_supply";
    private static boolean isError = true;

    static public List<String> getPathsPowerSupply() {
        List<String> paths = new ArrayList<String>();
        File directory = new File(power_supply);
        if(directory.exists()) {
            isError = false;
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; ++i) {
                paths.add(files[i].getAbsolutePath());
            }
            Log.i(SettingsActivity.TAG, "ScannerPaths: power_supply dirs " + paths.toString());
        } else {
            Log.e(SettingsActivity.TAG, "getPathsPowerSupply: isError");
            isError = true;
        }
        return paths;
    }

    static public List<String> getPathsEntryOfPowerSupply(String entry) {
        if(!isError) {
            List<String> paths = new ArrayList<String>();
            File directory = new File(entry);

            File[] files = directory.listFiles();

            for (int i = 0; i < files.length; ++i) {
                paths.add(files[i].getAbsolutePath());
            }
            Log.i(SettingsActivity.TAG, "ScannerPaths: entryFiles " + paths.toString());
            return paths;
        }
        return null;
    }
    static boolean checkPaths() {
        getPathsPowerSupply();
        return isError;
    }
}
