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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public static String TAG = SettingsActivity.class.getSimpleName();
    
    private BatteryManager mBatteryManager = null;
    public Spinner spinnerBatteries;
    public Spinner capacityFiles;
    public Spinner statusFiles;

    public CheckBox chbAutostartService;
    public SharedPreferences sharedPref;
    public Button intervalSetBTN;
    public EditText intervalET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Log.i(SettingsActivity.TAG, "onCreate:         setupViews();");
        setupViews();
        Log.i(SettingsActivity.TAG, "onCreate:         setupSpinner();");
        setupSpinner();
        Log.i(SettingsActivity.TAG, "onCreate:         loadParams();");
        loadParams();
    }

    public void setupSpinner() {
        if(!ScannerPaths.checkPaths()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ScannerPaths.getPathsPowerSupply());
            spinnerBatteries.setAdapter(adapter);
        } else Toast.makeText(this,ScannerPaths.power_supply + " directory not found",Toast.LENGTH_LONG).show();

        spinnerBatteries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            loadPathsEntry();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    public void setupViews() {
        chbAutostartService = (CheckBox) findViewById(R.id.chbServiceAutoStart);
        spinnerBatteries = (Spinner) findViewById(R.id.spinnerBatteries);
        intervalSetBTN = (Button) findViewById(R.id.intervalSetBTN);
        intervalET = (EditText) findViewById(R.id.intervalET);
        spinnerBatteries = (Spinner) findViewById(R.id.spinnerBatteries);
        capacityFiles = (Spinner) findViewById(R.id.spinnerCapacity);
        statusFiles = (Spinner) findViewById(R.id.spinnerStatus);
    }

    public void loadParams() {
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs batterySelection " + sharedPref.getInt("batterySelection",0));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs serviceRun " + sharedPref.getBoolean("serviceRun", false));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs serviceAutoStart " + sharedPref.getBoolean("serviceAutoStart", false));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs interval " + sharedPref.getInt("interval", 2));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastTypeBattery " + sharedPref.getString("lastTypeBattery", "null"));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastStateBattery " + sharedPref.getString("lastStateBattery", "null"));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs capacityFiles " + sharedPref.getInt("capacityFiles", 0));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs statusFiles " + sharedPref.getInt("statusFiles", 0));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastID " + sharedPref.getInt("lastID", 0));
        Log.i(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastIDString " + sharedPref.getString("lastIDString", ScannerPaths.power_supply));

        chbAutostartService.setChecked(sharedPref.getBoolean("serviceAutoStart", false));
        intervalET.setText(String.valueOf(sharedPref.getInt("interval", 2)));
        spinnerBatteries.setSelection(sharedPref.getInt("lastID", 0));
    }

    public void loadPathsEntry() {
        if((capacityFiles.getAdapter() == null) && (statusFiles.getAdapter() == null)) {
            ArrayAdapter<String> adapterCapacity = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ScannerPaths.getPathsEntryOfPowerSupply(spinnerBatteries.getSelectedItem().toString()));
            capacityFiles.setAdapter(adapterCapacity);

            ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ScannerPaths.getPathsEntryOfPowerSupply(spinnerBatteries.getSelectedItem().toString()));
            statusFiles.setAdapter(adapterStatus);
            capacityFiles.setSelection(sharedPref.getInt("capacityFiles", 0));
            statusFiles.setSelection(sharedPref.getInt("statusFiles", 0));
        } else {
            ArrayAdapter<String> adapterCapacity = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ScannerPaths.getPathsEntryOfPowerSupply(spinnerBatteries.getSelectedItem().toString()));
            capacityFiles.setAdapter(adapterCapacity);

            ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ScannerPaths.getPathsEntryOfPowerSupply(spinnerBatteries.getSelectedItem().toString()));
            statusFiles.setAdapter(adapterStatus);
        }
    }
    public void showNotifyClick(View v){
        mBatteryManager = null;
        Intent intent = new Intent(getApplicationContext(), BatteryManagerService.class);
        stopService(intent);

        mBatteryManager = new BatteryManager(capacityFiles.getSelectedItem().toString(),statusFiles.getSelectedItem().toString() );
        if (mBatteryManager.isSupport) {
            Log.i(SettingsActivity.TAG, "onCreate: isSupported");
            intent.putExtra("BatteryManager", mBatteryManager);
            mBatteryManager = null;
            saveSpinners();
            if(BatteryManagerService.isMyServiceRunning()) {
                stopService(intent);
                startService(intent);
            } else startService(intent);
        } else {mBatteryManager = null; }
    }

    public void saveSpinners() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastTypeBattery",capacityFiles.getSelectedItem().toString());
        editor.putString("lastStateBattery",statusFiles.getSelectedItem().toString());
        editor.putString("lastIDString",spinnerBatteries.getSelectedItem().toString());
        editor.putInt("capacityFiles", (int) capacityFiles.getSelectedItemId());
        editor.putInt("statusFiles", (int) statusFiles.getSelectedItemId());
        editor.putInt("lastID", (int) spinnerBatteries.getSelectedItemId());
        editor.apply();
    }

    public void intervalClick(View v) {
        if(!intervalET.getText().toString().equals("0")) {
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

    public void dismissNotifyClick(View view) {
        if(BatteryManagerService.isMyServiceRunning()) {
            mBatteryManager = null;
            Intent intent = new Intent(getApplicationContext(), BatteryManagerService.class);
            stopService(intent);
        }
    }
}
