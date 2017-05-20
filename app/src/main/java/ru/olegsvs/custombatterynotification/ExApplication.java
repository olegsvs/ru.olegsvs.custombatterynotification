package ru.olegsvs.custombatterynotification;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by oleg.svs on 19.05.2017.
 */

public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Log.i(SettingsActivity.TAG, "onCreate: Application STARTED");
    }
}
