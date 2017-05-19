package ru.olegsvs.custombatterynotification

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    var sharedPref: SharedPreferences? = null

    private var mBatteryManager: BatteryManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_settings)
        try {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setIcon(R.mipmap.ic_launcher)
        } catch (e: NullPointerException) {
            Log.e(TAG, "onCreate: setDisplayShowHomeEnabled " + e.toString())
        }

        sharedPref = getSharedPreferences("Settings", 0)
        Log.i(SettingsActivity.TAG, "onCreate:         setupViews")
        setupViews()
        Log.i(SettingsActivity.TAG, "onCreate:         setupSpinners")
        setupSpinners()
        Log.i(SettingsActivity.TAG, "onCreate:         loadParams")
        loadParams()
    }

    fun setupSpinners() {
        if (!ScannerPaths.checkPaths()) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, ScannerPaths.pathsPowerSupply)
            spinnerBatteries.adapter = adapter
        } else
            Toast.makeText(this, ScannerPaths.power_supply + " directory not found", Toast.LENGTH_LONG).show()

        spinnerBatteries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {

                loadPathsEntry()

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }


    fun setupViews() {
//        chbAutostartService = findViewById(R.id.chbServiceAutoStart) as CheckBox
//        spinnerBatteries = findViewById(R.id.spinnerBatteries) as Spinner
//        intervalSetBTN = findViewById(R.id.intervalSetBTN) as Button
//        intervalET = findViewById(R.id.intervalET) as EditText
//        spinnerBatteries = findViewById(R.id.spinnerBatteries) as Spinner
//        capacityFiles = findViewById(R.id.spinnerCapacity) as Spinner
//        statusFiles = findViewById(R.id.spinnerStatus) as Spinner
    }

    fun loadParams() {
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs batterySelection " + sharedPref!!.getInt("batterySelection", 0))
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs serviceRun " + sharedPref!!.getBoolean("serviceRun", false))
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs serviceAutoStart " + sharedPref!!.getBoolean("serviceAutoStart", false))
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs interval " + sharedPref!!.getInt("interval", 2))
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastTypeBattery " + sharedPref!!.getString("lastTypeBattery", "null")!!)
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastStateBattery " + sharedPref!!.getString("lastStateBattery", "null")!!)
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs capacityFiles " + sharedPref!!.getInt("capacityFiles", 0))
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs statusFiles " + sharedPref!!.getInt("statusFiles", 0))
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastID " + sharedPref!!.getInt("lastID", 0))
        Log.w(SettingsActivity.TAG, "onCreate: loading sharedPrefs lastIDString " + sharedPref!!.getString("lastIDString", ScannerPaths.power_supply)!!)

        chbServiceAutoStart!!.isChecked = sharedPref!!.getBoolean("serviceAutoStart", false)
        intervalET.setText(sharedPref!!.getInt("interval", 2).toString())
        spinnerBatteries.setSelection(sharedPref!!.getInt("lastID", 0))
    }

    fun loadPathsEntry() {
        val adapterCapacity = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, ScannerPaths.getPathsEntryOfPowerSupply(spinnerBatteries.selectedItem.toString())!!)
        val adapterStatus = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, ScannerPaths.getPathsEntryOfPowerSupply(spinnerBatteries.selectedItem.toString())!!)
        if (spinnerCapacity.adapter == null && spinnerStatus.adapter == null) {
            spinnerCapacity.adapter = adapterCapacity
            spinnerStatus.adapter = adapterStatus
            spinnerCapacity.setSelection(sharedPref!!.getInt("capacityFiles", 0))
            spinnerStatus.setSelection(sharedPref!!.getInt("statusFiles", 0))
        } else {
            spinnerCapacity.adapter = adapterCapacity
            spinnerStatus.adapter = adapterStatus
        }
    }

    fun showNotifyClick(v: View) {
        try {
            mBatteryManager = null
            val intent = Intent(applicationContext, BatteryManagerService::class.java)
            stopService(intent)

            mBatteryManager = BatteryManager(spinnerCapacity.selectedItem.toString(), spinnerStatus.selectedItem.toString())
            if (true /*mBatteryManager.getIsSupport()*/) {
                Log.w(SettingsActivity.TAG, "onCreate: isSupported")
                //                intent.putExtra("BatteryManager", mBatteryManager);
                mBatteryManager = null
                saveSpinners()
                if (BatteryManagerService.isMyServiceRunning) {
                    stopService(intent)
                    startService(intent)
                } else
                    startService(intent)
            } else {
                mBatteryManager = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Crashlytics.logException(e)
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }

    fun saveSpinners() {
        val editor = sharedPref!!.edit()
        editor.putString("lastTypeBattery", spinnerCapacity.selectedItem.toString())
        editor.putString("lastStateBattery", spinnerStatus.selectedItem.toString())
        editor.putString("lastIDString", spinnerBatteries.selectedItem.toString())
        editor.putInt("capacityFiles", spinnerCapacity.selectedItemId.toInt())
        editor.putInt("statusFiles", spinnerStatus.selectedItemId.toInt())
        editor.putInt("lastID", spinnerBatteries.selectedItemId.toInt())
        editor.apply()
    }

    fun intervalClick(v: View) {
        if (intervalET.text.toString() != "0") {
            Log.w(SettingsActivity.TAG, "intervalClick: setting interval " + intervalET.text.toString())
            val editor = sharedPref!!.edit()
            editor.putInt("interval", Integer.parseInt(intervalET.text.toString()))
            editor.apply()
            editor.commit()
            BatteryManagerService.interval = Integer.parseInt(intervalET.text.toString())
        } else
            Toast.makeText(applicationContext, getString(R.string.intervalWarning), Toast.LENGTH_LONG).show()
    }

    fun cnbAutoStartClick(v: View) {
        Log.w(SettingsActivity.TAG, "cnbAutoStartClick: changedState = " + chbServiceAutoStart.isChecked)
        val editor = sharedPref!!.edit()
        editor.putBoolean("serviceAutoStart", chbServiceAutoStart.isChecked)
        editor.apply()
        editor.commit()
    }

    fun dismissNotifyClick(view: View) {
        if (BatteryManagerService.isMyServiceRunning) {
            mBatteryManager = null
            val intent = Intent(applicationContext, BatteryManagerService::class.java)
            stopService(intent)
        }
    }

    companion object {
        var TAG = SettingsActivity::class.java.simpleName
    }
}
