package ru.olegsvs.custombatterynotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import static ru.olegsvs.custombatterynotification.BatteryManager.getValues;

public class SettingsActivity extends AppCompatActivity {
    public static String TAG = SettingsActivity.class.getSimpleName();
    private final String batteries[] = {"BAT1" , "BAT2"};

    public Spinner spinnerBatteries;
    public CheckBox chbServiceStatus;
    public CheckBox chbAutostartService;
    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (!BatteryManagerService.isMyServiceRunning()) {
            Intent intent = new Intent(this, BatteryManagerService.class);
            startService(intent);
            Log.i(SettingsActivity.TAG, "onCreate: service NOT running , startService BatteryManagerService");
        }
        chbServiceStatus = (CheckBox) findViewById(R.id.chbServiceStatus);
        chbAutostartService = (CheckBox) findViewById(R.id.chbServiceAutoStart);
        spinnerBatteries = (Spinner) findViewById(R.id.spinnerBatteries);

        sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs " + sharedPref.getInt("batterySelection",0));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs " + sharedPref.getBoolean("serviceRun", false));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs " + sharedPref.getBoolean("serviceAutoStart", false));

        chbAutostartService.setChecked(sharedPref.getBoolean("serviceAutoStart", false));
        chbServiceStatus.setChecked(sharedPref.getBoolean("serviceRun", false));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, batteries);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerBatteries.setAdapter(spinnerArrayAdapter);

        spinnerBatteries.setSelection(sharedPref.getInt("batterySelection",0));
        BatteryManagerService.setBatteryForShow(spinnerBatteries.getSelectedItemPosition());

        spinnerBatteries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if ((position == 1) && (!BatteryManager.checkJSRSupport())) {
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
        });
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
