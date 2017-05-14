package ru.olegsvs.custombatterynotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by olegsvs on 11.05.2017.
 */

public class BatteryManagerService extends Service{
    private int iconRes[] = {
            R.drawable.battery_green_0,R.drawable.battery_green_1,
            R.drawable.battery_green_2,R.drawable.battery_green_3,
            R.drawable.battery_green_4,R.drawable.battery_green_5,
            R.drawable.battery_green_6,R.drawable.battery_green_7,
            R.drawable.battery_green_8,R.drawable.battery_green_9,
            R.drawable.battery_green_10,R.drawable.battery_green_11,
            R.drawable.battery_green_12,R.drawable.battery_green_13,
            R.drawable.battery_green_14,R.drawable.battery_green_15,
            R.drawable.battery_green_16,R.drawable.battery_green_17,
            R.drawable.battery_green_18,R.drawable.battery_green_19,
            R.drawable.battery_green_20,R.drawable.battery_green_21,
            R.drawable.battery_green_22,R.drawable.battery_green_23,
            R.drawable.battery_green_24,R.drawable.battery_green_25,
            R.drawable.battery_green_26,R.drawable.battery_green_27,
            R.drawable.battery_green_28,R.drawable.battery_green_29,
            R.drawable.battery_green_30,R.drawable.battery_green_31,
            R.drawable.battery_green_32,R.drawable.battery_green_33,
            R.drawable.battery_green_34,R.drawable.battery_green_35,
            R.drawable.battery_green_36,R.drawable.battery_green_37,
            R.drawable.battery_green_38,R.drawable.battery_green_39,
            R.drawable.battery_green_40,R.drawable.battery_green_41,
            R.drawable.battery_green_42,R.drawable.battery_green_43,
            R.drawable.battery_green_44,R.drawable.battery_green_45,
            R.drawable.battery_green_46,R.drawable.battery_green_47,
            R.drawable.battery_green_48,R.drawable.battery_green_49,
            R.drawable.battery_green_50,R.drawable.battery_green_51,
            R.drawable.battery_green_52,R.drawable.battery_green_53,
            R.drawable.battery_green_54,R.drawable.battery_green_55,
            R.drawable.battery_green_56,R.drawable.battery_green_57,
            R.drawable.battery_green_58,R.drawable.battery_green_59,
            R.drawable.battery_green_60,R.drawable.battery_green_61,
            R.drawable.battery_green_62,R.drawable.battery_green_63,
            R.drawable.battery_green_64,R.drawable.battery_green_65,
            R.drawable.battery_green_66,R.drawable.battery_green_67,
            R.drawable.battery_green_68,R.drawable.battery_green_69,
            R.drawable.battery_green_70,R.drawable.battery_green_71,
            R.drawable.battery_green_72,R.drawable.battery_green_73,
            R.drawable.battery_green_74,R.drawable.battery_green_75,
            R.drawable.battery_green_76,R.drawable.battery_green_77,
            R.drawable.battery_green_78,R.drawable.battery_green_79,
            R.drawable.battery_green_80,R.drawable.battery_green_81,
            R.drawable.battery_green_82,R.drawable.battery_green_83,
            R.drawable.battery_green_84,R.drawable.battery_green_85,
            R.drawable.battery_green_86,R.drawable.battery_green_87,
            R.drawable.battery_green_88,R.drawable.battery_green_89,
            R.drawable.battery_green_90,R.drawable.battery_green_91,
            R.drawable.battery_green_92,R.drawable.battery_green_93,
            R.drawable.battery_green_94,R.drawable.battery_green_95,
            R.drawable.battery_green_96,R.drawable.battery_green_97,
            R.drawable.battery_green_98,R.drawable.battery_green_99,
            R.drawable.battery_green_100

    };
    private final int NOTIFICATION_CUSTOM_BATTERY = 444;
    private int BAT_CAPACITY = 0;
    private static int interval = 2000;
    private final String PERCENT= "%";
    private final String UNRECOGNIZED_VALUES= "Unrecognized values";

    private static boolean IS_STARTED = false;

    private NotificationCompat.Builder mBuilder = null;
    Handler myHandler = null;
    NotificationManager mNotificationManager = null;
    BatteryManager mBatteryManager = null;
    Runnable runnable = null;

    public static boolean isMyServiceRunning() {
        Log.i(SettingsActivity.TAG, "BatteryManagerService started ? " + IS_STARTED);
        return IS_STARTED;
    }

    public static void setInterval(int interval) {
        BatteryManagerService.interval = interval * 1000;
        Log.i(SettingsActivity.TAG, "BatteryManagerService setInterval: " + BatteryManagerService.interval);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_STARTED = false;
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        mBatteryManager = null;
        if(myHandler != null)
        myHandler.removeCallbacks(runnable);
        Log.i(SettingsActivity.TAG, "BatteryManagerService onDestroy: BatteryManagerService destroy!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Log.i(SettingsActivity.TAG, "onStartCommand: BatteryManagerService start");
        try {
            mBatteryManager = intent.getParcelableExtra("BatteryManager");
        } catch (Exception e) { Log.e(SettingsActivity.TAG, "onStartCommand: getParcelableExtra " + e); }

            if(mBatteryManager == null) {
                String lastTypeBattery = sharedPref.getString("lastTypeBattery", "null");
                String lastStateBattery = sharedPref.getString("lastStateBattery", "null");
                mBatteryManager = new BatteryManager(lastTypeBattery, lastStateBattery);
            }
            if(mBatteryManager.isSupport) {
                Log.i(SettingsActivity.TAG, "BatteryManagerService onStartCommand:  " + mBatteryManager.isSupport);

                 mNotificationManager =
                        (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                interval = 1000 * sharedPref.getInt("interval", 2);
                Log.i(SettingsActivity.TAG, "onStartCommand: load interval = " + interval);
                createNotify();
                Log.i(SettingsActivity.TAG, "onStartCommand: create and show notification");
                return super.onStartCommand(intent, flags, startId);
            } else stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private String getResults() {
        try{
        BAT_CAPACITY = Integer.parseInt(this.mBatteryManager.getCapacity());
            if(!(BAT_CAPACITY >= 0 && BAT_CAPACITY <= 100)){
                Crashlytics.log("getResults: BAT_CAPACITY index error [0::100]! " + BAT_CAPACITY);
                Log.e(SettingsActivity.TAG, "getResults: BAT_CAPACITY index error [0::100]! " + BAT_CAPACITY);
                Toast.makeText(getApplicationContext(),"BAT_CAPACITY index error [0::100] = " + BAT_CAPACITY,Toast.LENGTH_LONG).show();
                                this.mBatteryManager = null;
                stopSelf();
                return UNRECOGNIZED_VALUES;

            }
        return BAT_CAPACITY + PERCENT + this.mBatteryManager.getState(); } catch (Exception e)  {
            Crashlytics.logException(new Throwable("getResults: Unrecognized values! " + e.toString()));
            Log.e(SettingsActivity.TAG, "getResults: Unrecognized values! " + e.toString());
            Toast.makeText(getApplicationContext(),"Unrecognized values! " + e.toString(),Toast.LENGTH_LONG).show();
            this.mBatteryManager = null;
            stopSelf();
            return UNRECOGNIZED_VALUES;
        }

    }

    private void createNotify() {
        try {
            int color = 0xff123456;
            mBuilder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle("Battery")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(getResults()))
                            .setOngoing(true)
                            .setColor(color)
                            .setSmallIcon(iconRes[BAT_CAPACITY])
                            .setWhen(0)
                            .setContentText(getResults());
            Intent resultIntent = new Intent(this, SettingsActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SettingsActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            mNotificationManager.notify(NOTIFICATION_CUSTOM_BATTERY, mBuilder.build());

            myHandler = new Handler();
            Log.i(SettingsActivity.TAG, "handler started");
            Log.i(SettingsActivity.TAG, "state : " + getResults());
            runnable = new Runnable(){

                @Override
                public void run() {
                    if(IS_STARTED) {
                        mBuilder.setContentText(getResults());
                        mBuilder.setSmallIcon(iconRes[BAT_CAPACITY]);
                        mNotificationManager.notify(NOTIFICATION_CUSTOM_BATTERY, mBuilder.build());
                        myHandler.postDelayed(runnable, interval);
                    }
                }

            };

            myHandler.postDelayed(runnable, interval);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IS_STARTED = true;
        Log.i(SettingsActivity.TAG, "BatteryManagerService onCreate: creating BatteryManagerService, IS_STARTED = " + IS_STARTED);
    }
}
