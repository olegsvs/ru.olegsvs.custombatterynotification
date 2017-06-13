package ru.olegsvs.custombatterynotification;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olegsvs on 13.05.2017.
 */

public class ScannerPaths {
    public static final String power_supply = "/sys/class/power_supply"; //standard battery path value
    private static boolean isError = true; //compatibility for power_supply path

    static public List<String> getPathsPowerSupply() { //return all dirs in path /sys/class/power_supply/
        List<String> paths = new ArrayList<String>(); //make array list with dirs on /sys/class/power_supply/
        File directory = new File(power_supply);
        if(directory.exists()) {
            isError = false;
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; ++i) {
                paths.add(files[i].getAbsolutePath()); //add dirs to list paths
            }
            Log.w(SettingsActivity.TAG, "ScannerPaths: power_supply dirs " + paths.toString()); //output of all found folders
        } else {
            Log.e(SettingsActivity.TAG, "getPathsPowerSupply: isError"); //not compatibility with standart path
            isError = true;
        }
        return paths;
    }

    static public List<String> getPathsEntryOfPowerSupply(String entry) { //return all subdirs & subfiles on path /sys/class/power_supply/
        if(!isError) {
            List<String> paths = new ArrayList<String>(); //make array list with subdirs & subfiles on /sys/class/power_supply/
            File directory = new File(entry);

            File[] files = directory.listFiles();

            for (int i = 0; i < files.length; ++i) {
                paths.add(files[i].getAbsolutePath()); //add dirs to list paths
            }
            Log.w(SettingsActivity.TAG, "ScannerPaths: entryFiles " + paths.toString());
            return paths;
        }
        return null;
    }
    static boolean checkPaths() {
        getPathsPowerSupply(); //check for compatibility with path /sys/class/power_supply/
        return isError;
    }
}
