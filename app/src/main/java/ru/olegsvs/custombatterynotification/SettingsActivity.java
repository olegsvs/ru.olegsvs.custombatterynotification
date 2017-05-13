package ru.olegsvs.custombatterynotification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    public static String TAG = SettingsActivity.class.getSimpleName();
    
    public BatteryManager mBatteryManager = null;
    public Spinner spinnerBatteries;
    public CheckBox chbServiceStatus;
    public CheckBox chbAutostartService;
    public SharedPreferences sharedPref;
    public Button intervalSetBTN;
    public EditText intervalET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Spinner s = (Spinner) findViewById(R.id.spinnerBatteries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getPaths());
        s.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BatteryManagerService.class);
                startService(intent);
                stopService(intent);
                mBatteryManager = new BatteryManager(s.getSelectedItem().toString() + "/capacity",s.getSelectedItem().toString() + "/status");
                if (mBatteryManager.isSupport) {
                    Log.i(SettingsActivity.TAG, "onCreate: isSupported");

                    intent.putExtra("BatteryManager", mBatteryManager);
                    mBatteryManager = null;
                    startService(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

      /*  mBatteryManager = new BatteryManager(BatteryManager.SYS_BATTERY_CAPACITY,BatteryManager.SYS_BATTERY_STATUS);
        if (mBatteryManager.isSupport) {
            Log.i(SettingsActivity.TAG, "onCreate: isSupported");
            Intent intent = new Intent(this, BatteryManagerService.class);
            intent.putExtra("BatteryManager", mBatteryManager);
            mBatteryManager = null;
            startService(intent);
        }*/

    /**    if (!BatteryManager.isSTDSupportCheck() && !BatteryManager.isJSRSupportCheck()) {
            Log.e(TAG, "onCreate: Application not supported!");
            Intent intent = new Intent(this, BatteryManagerService.class);
            stopService(intent);
            Log.i(SettingsActivity.TAG, "cnbServiceStatusClick: StopService");
            notSupportedDialog();
        }

        if (!BatteryManagerService.isMyServiceRunning()) {
            Intent intent = new Intent(this, BatteryManagerService.class);
            startService(intent);
            Log.i(SettingsActivity.TAG, "onCreate: service NOT running , startService BatteryManagerService");
        } */
       /* chbServiceStatus = (CheckBox) findViewById(R.id.chbServiceStatus);
        chbAutostartService = (CheckBox) findViewById(R.id.chbServiceAutoStart);
        spinnerBatteries = (Spinner) findViewById(R.id.spinnerBatteries);
        intervalSetBTN = (Button) findViewById(R.id.intervalSetBTN);
        intervalET = (EditText) findViewById(R.id.intervalET);

        sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs batterySelection " + sharedPref.getInt("batterySelection",0));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs serviceRun " + sharedPref.getBoolean("serviceRun", false));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs serviceAutoStart " + sharedPref.getBoolean("serviceAutoStart", false));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs interval " + sharedPref.getInt("interval", 2));

        chbAutostartService.setChecked(sharedPref.getBoolean("serviceAutoStart", false));
        chbServiceStatus.setChecked(sharedPref.getBoolean("serviceRun", false));
        intervalET.setText(String.valueOf(sharedPref.getInt("interval", 2)));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, batteries);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerBatteries.setAdapter(spinnerArrayAdapter);

        spinnerBatteries.setSelection(sharedPref.getInt("batterySelection",0));
        BatteryManagerService.setBatteryForShow(spinnerBatteries.getSelectedItemPosition());

        spinnerBatteries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if ((position == 1) && (!BatteryManager.isJSRSupportCheck())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noSupportedJSR), Toast.LENGTH_LONG).show();
                    spinnerBatteries.setSelection(0);
                }
                Log.w(SettingsActivity.TAG, "onItemSelected: " + position);
                BatteryManagerService.setBatteryForShow(position);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("batterySelection" , position);
                editor.apply();
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });  */
    }


    public List<String> getPaths() {
        List<String> paths = new ArrayList<String>();
        File directory = new File("/sys/class/power_supply");

        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; ++i) {
            paths.add(files[i].getAbsolutePath());
        }
        Log.i(SettingsActivity.TAG, "onCreate: " + paths.toString());
        return paths;
    }

    public void notSupportedDialog() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Your Device not supported")
                        .setMessage(getText(R.string.not_supported))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Log.i(SettingsActivity.TAG, "onClick: device_not_supported_exit");
                                System.exit(0);
                            }
                        }).show();
            }
        });
    }

    public void intervalClick(View v) {
        if(intervalET.getText().toString().equals("0") == false) {
            Log.i(SettingsActivity.TAG, "intervalClick: setting interval " + intervalET.getText().toString());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("interval" , Integer.parseInt(intervalET.getText().toString()));
            editor.apply();
            editor.commit();
            BatteryManagerService.setInterval(Integer.parseInt(intervalET.getText().toString()));
        } else Toast.makeText(getApplicationContext(),getString(R.string.intervalWarning),Toast.LENGTH_LONG).show();
    }

    public void cnbAutoStartClick(View v) {
        Log.i(SettingsActivity.TAG, "cnbAutoStartClick: changedState = " + chbAutostartService.isChecked());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("serviceAutoStart" , chbAutostartService.isChecked());
        editor.apply();
        editor.commit();
    }

    public void cnbServiceStatusClick(View v) {
        Log.i(SettingsActivity.TAG, "cnbAutoStartClick: changedState = " + chbServiceStatus.isChecked());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("serviceRun" , chbServiceStatus.isChecked());
        editor.apply();
        editor.commit();

        if (chbServiceStatus.isChecked()) {
            if (!BatteryManagerService.isMyServiceRunning()) {
                Intent intent = new Intent(this, BatteryManagerService.class);
                startService(intent);
                Log.i(SettingsActivity.TAG, "cnbServiceStatusClick: StartService");
            }
        } else {
                Intent intent = new Intent(this, BatteryManagerService.class);
                startService(intent);
                stopService(intent);
                Log.i(SettingsActivity.TAG, "cnbServiceStatusClick: StopService");
        }
    }
}
