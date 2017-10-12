package com.mahmoodms.bluetooth.exgsinglechannel;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.SimpleXYSeries;
import com.beele.BluetoothLe;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mahmoodms on 5/31/2016.
 */

public class DeviceControlActivity extends Activity implements BluetoothLe.BluetoothLeListener {
    // Graphing Variables:
    private GraphAdapter mGraphAdapterCh1;
    private GraphAdapter mGraphAdapterCh2;
    private GraphAdapter mGraphAdapterMotionAX;
    private GraphAdapter mGraphAdapterMotionAY;
    private GraphAdapter mGraphAdapterMotionAZ;
//    private GraphAdapter mGraphAdapterMotionGyroRes;
    public XYPlotAdapter mPlotAdapter;
    public XYPlotAdapter mPlotAdapter2;
    public XYPlotAdapter mMotionPlotAdapter;
    public static Redrawer redrawer;
    private boolean plotImplicitXVals = false;
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    //LocalVars
    private String mDeviceName;
    private String mDeviceAddress;
    private boolean mConnected;
    //Class instance variable
    private BluetoothLe mBluetoothLe;
    private BluetoothManager mBluetoothManager = null;
    //Connecting to Multiple Devices
    private String[] deviceMacAddresses = null;
    private BluetoothDevice[] mBluetoothDeviceArray = null;
    private BluetoothGatt[] mBluetoothGattArray = null;
    private BluetoothGattService mLedService = null;
    private int mWheelchairGattIndex;

    //Layout - TextViews and Buttons
    private TextView mBatteryLevel;
    private TextView mDataRate;
    private Button mExportButton;
    private Switch mFilterSwitch;
    private long mLastTime;
    private long mLastTime2;
    private long mCurrentTime;
    private long mCurrentTime2;
    private long mClassTime; //DON'T DELETE!!!

    private boolean filterData = false;
    private int points = 0;
    private Menu menu;

    //RSSI:
    private static final int RSSI_UPDATE_TIME_INTERVAL = 2000;
    private Handler mTimerHandler = new Handler();
    private boolean mTimerEnabled = false;

    //Data Variables:
    private int batteryWarning = 20;//
    private String fileTimeStamp = "";
    private double dataRate;
    private double mEOGClass = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        //Set orientation of device based on screen type/size:
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Recieve Intents:
        Intent intent = getIntent();
        deviceMacAddresses = intent.getStringArrayExtra(MainActivity.INTENT_DEVICES_KEY);
        String[] deviceDisplayNames = intent.getStringArrayExtra(MainActivity.INTENT_DEVICES_NAMES);
        mDeviceName = deviceDisplayNames[0];
        mDeviceAddress = deviceMacAddresses[0];
        Log.d(TAG, "Device Names: " + Arrays.toString(deviceDisplayNames));
        Log.d(TAG, "Device MAC Addresses: " + Arrays.toString(deviceMacAddresses));
        Log.d(TAG, Arrays.toString(deviceMacAddresses));
        //Set up action bar:
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6078ef")));
        //Flag to keep screen on (stay-awake):
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Set up TextViews
        mExportButton = (Button) findViewById(R.id.button_export);
        mFilterSwitch = (Switch) findViewById(R.id.filterSwitch);
        mBatteryLevel = (TextView) findViewById(R.id.batteryText);
        mDataRate = (TextView) findViewById(R.id.dataRate);
        mDataRate.setText("...");
        //Initialize Bluetooth
        ActionBar ab = getActionBar();
        ab.setTitle(mDeviceName);
        ab.setSubtitle(mDeviceAddress);
        initializeBluetoothArray();
        // Initialize our XYPlot reference:
        mGraphAdapterMotionAX = new GraphAdapter(375, "Acc X", false, false, Color.RED); //Color.parseColor("#19B52C") also, RED, BLUE, etc.
        mGraphAdapterMotionAX.plotData = true;
        mGraphAdapterMotionAX.setPointWidth((float)2);
        mGraphAdapterMotionAY = new GraphAdapter(375, "Acc Y", false, false, Color.BLUE); //Color.parseColor("#19B52C") also, RED, BLUE, etc.
        mGraphAdapterMotionAY.plotData = true;
        mGraphAdapterMotionAY.setPointWidth((float)2);
        mGraphAdapterMotionAZ = new GraphAdapter(375, "Acc Z", false, false, Color.GREEN); //Color.parseColor("#19B52C") also, RED, BLUE, etc.
        mGraphAdapterMotionAZ.plotData = true;
        mGraphAdapterMotionAZ.setPointWidth((float)2);
//        mGraphAdapterMotionGyroRes = new GraphAdapter(125, "Gyro Resultant", false, false, Color.GRAY); //Color.parseColor("#19B52C") also, RED, BLUE, etc.
//        mGraphAdapterMotionGyroRes.plotData = true;
//        mGraphAdapterMotionGyroRes.setPointWidth((float)2);
        //PLOT CH1 By default
        mGraphAdapterCh1 = new GraphAdapter(1000, "ECG Data Ch 1", false, false, Color.BLUE); //Color.parseColor("#19B52C") also, RED, BLUE, etc.
        mGraphAdapterCh1.plotData = true;
        mGraphAdapterCh1.setPointWidth((float) 2);
        mGraphAdapterCh2 = new GraphAdapter(1000, "ECG Data Ch 2", false, false, Color.RED); //Color.parseColor("#19B52C") also, RED, BLUE, etc.
        mGraphAdapterCh2.plotData = true;
        mGraphAdapterCh2.setPointWidth((float) 2);

        if (filterData) mPlotAdapter.filterData();
        if (filterData) mPlotAdapter2.filterData();
        if (plotImplicitXVals) mGraphAdapterCh1.series.useImplicitXVals();
        if (plotImplicitXVals) mGraphAdapterCh2.series.useImplicitXVals();
        mPlotAdapter = new XYPlotAdapter(findViewById(R.id.eegPlot), plotImplicitXVals, 1000);
        mPlotAdapter2 = new XYPlotAdapter(findViewById(R.id.ecgPlot), plotImplicitXVals, 1000);
        mPlotAdapter.xyPlot.addSeries(mGraphAdapterCh1.series, mGraphAdapterCh1.lineAndPointFormatter);
        mPlotAdapter2.xyPlot.addSeries(mGraphAdapterCh2.series, mGraphAdapterCh2.lineAndPointFormatter);

        if (filterData) mMotionPlotAdapter.filterData();
        if (plotImplicitXVals) mGraphAdapterMotionAX.series.useImplicitXVals();
        mMotionPlotAdapter = new XYPlotAdapter(findViewById(R.id.MotionPlot), plotImplicitXVals, 375, 12, "Motion Sensor Data (g)");
        mMotionPlotAdapter.xyPlot.addSeries(mGraphAdapterMotionAX.series, mGraphAdapterMotionAX.lineAndPointFormatter);
        mMotionPlotAdapter.xyPlot.addSeries(mGraphAdapterMotionAY.series, mGraphAdapterMotionAY.lineAndPointFormatter);
        mMotionPlotAdapter.xyPlot.addSeries(mGraphAdapterMotionAZ.series, mGraphAdapterMotionAZ.lineAndPointFormatter);
//        mMotionPlotAdapter.xyPlot.addSeries(mGraphAdapterMotionGyroRes.series, mGraphAdapterMotionGyroRes.lineAndPointFormatter);

        redrawer = new Redrawer(
                Arrays.asList(new Plot[]{mPlotAdapter.xyPlot, mPlotAdapter2.xyPlot, mMotionPlotAdapter.xyPlot}),
                60, false);
        mExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveDataFile(true);
                } catch (IOException e) {
                    Log.e(TAG, "IOException in saveDataFile");
                    e.printStackTrace();
                }
                ArrayList<Uri> files = new ArrayList<Uri>();
                Uri uii;
                Uri uii2;
                uii = Uri.fromFile(file);
                uii2 = Uri.fromFile(file2);
                files.add(uii);
                files.add(uii2);
                Intent exportData = new Intent(Intent.ACTION_SEND_MULTIPLE);
                exportData.putExtra(Intent.EXTRA_SUBJECT, "ECG Sensor Data Export Details");
                exportData.putParcelableArrayListExtra(Intent.EXTRA_STREAM,files);
                exportData.setType("text/html");
                startActivity(exportData);
            }
        });
        makeFilterSwitchVisible(false);
        mFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterData = isChecked;
            }
        });
        mLastTime = System.currentTimeMillis();
        mLastTime2 = System.currentTimeMillis();
        mClassTime = System.currentTimeMillis();
        ToggleButton ch1 = (ToggleButton) findViewById(R.id.toggleButtonCh1);
        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mGraphAdapterCh1.setPlotData(b);
                mGraphAdapterCh2.setPlotData(b);
            }
        });
//        mMediaBeep = MediaPlayer.create(this, R.raw.beep_01a);
    }

    public String getTimeStamp() {
        return new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());
    }

    private boolean fileSaveInitialized = false;
    private CSVWriter csvWriter;
    private CSVWriter csvWriter2;
    private File file;
    private File file2;
    private File root;

    /**
     * @param terminate - if True, terminates CSVWriter Instance
     * @throws IOException
     */
    public void saveDataFile(boolean terminate) throws IOException {
        if (terminate && fileSaveInitialized) {
            csvWriter.flush();
            csvWriter.close();
            csvWriter2.flush();
            csvWriter2.close();
            fileSaveInitialized = false;
        }
    }

    /**
     * Initializes CSVWriter For Saving Data.
     *
     * @throws IOException bc
     */
    public void saveDataFile() throws IOException {
        root = Environment.getExternalStorageDirectory();
        fileTimeStamp = "ECG_2Ch_2kHz_" + getTimeStamp();
        String fTS2 = "MPU_31.25Hz" + getTimeStamp();
        if (root.canWrite()) {
            File dir = new File(root.getAbsolutePath() + "/ECGData");
            dir.mkdirs();
            file = new File(dir, fileTimeStamp + ".csv");
            File dir2 = new File(root.getAbsolutePath() + "/MotionSensorData");
            dir2.mkdirs();
            file2 = new File(dir2, fTS2 + ".csv");
            if (file.exists() && !file.isDirectory()) {
                Log.d(TAG, "File " + file.toString() + " already exists - appending data");
                FileWriter fileWriter = new FileWriter(file, true);
                csvWriter = new CSVWriter(fileWriter);
            } else {
                csvWriter = new CSVWriter(new FileWriter(file));
            }

            if (file2.exists() && !file2.isDirectory()) {
                Log.d(TAG, "File " + file2.toString() + " already exists - appending data");
                FileWriter fileWriter = new FileWriter(file2, true);
                csvWriter2 = new CSVWriter(fileWriter);
            } else {
                csvWriter2 = new CSVWriter(new FileWriter(file2));
            }

            fileSaveInitialized = true;
        }
    }

    public void exportFileWithClass(double eegData1) throws IOException {
        if (fileSaveInitialized) {
            String[] writeCSVValue = new String[1];
            writeCSVValue[0] = eegData1 + "";
            csvWriter.writeNext(writeCSVValue, false);
        }
    }

    public void exportDataToFile(double ch1, double ch2) throws IOException {
        if (fileSaveInitialized) {
            String[] writeCSVValue = new String[2];
            writeCSVValue[0] = ch1 + "";
            writeCSVValue[1] = ch2 + "";
            csvWriter.writeNext(writeCSVValue, false);
        }
    }
    public void exportDataToFileWithTimeStamp(double timestamp, double ch1, double ch2) throws IOException {
        if (fileSaveInitialized) {
            String[] writeCSVValue = new String[3];
            writeCSVValue[0] = timestamp + "";
            writeCSVValue[1] = ch1 + "";
            writeCSVValue[2] = ch2 + "";
            csvWriter.writeNext(writeCSVValue, false);
        }
    }

    public void exportFileMPU(double timestamp, double AcX, double AcY, double AcZ, double GyX, double GyY, double GyZ) throws IOException {
        if (fileSaveInitialized) {
            String[] writeCSVValue = new String[7];
            writeCSVValue[0] = timestamp + "";
            writeCSVValue[1] = AcX + "";
            writeCSVValue[2] = AcY + "";
            writeCSVValue[3] = AcZ + "";
            writeCSVValue[4] = GyX + "";
            writeCSVValue[5] = GyY + "";
            writeCSVValue[6] = GyZ + "";
            csvWriter2.writeNext(writeCSVValue, false);
        }
    }

    @Override
    public void onResume() {
        makeFilterSwitchVisible(true);
//        jmainInitialization(false);
        String fileTimeStampConcat = "EEGSensorData_" + getTimeStamp();
        Log.d("onResume-timeStamp", fileTimeStampConcat);
        if (!fileSaveInitialized) {
            try {
                saveDataFile();
            } catch (IOException ex) {
                Log.e("IOEXCEPTION:", ex.toString());
            }
        }
        redrawer.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        redrawer.pause();
        makeFilterSwitchVisible(false);
        super.onPause();
    }

    private void initializeBluetoothArray() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothDeviceArray = new BluetoothDevice[deviceMacAddresses.length];
        mBluetoothGattArray = new BluetoothGatt[deviceMacAddresses.length];
        Log.d(TAG, "Device Addresses: " + Arrays.toString(deviceMacAddresses));
        if (deviceMacAddresses != null) {
            for (int i = 0; i < deviceMacAddresses.length; i++) {
                mBluetoothDeviceArray[i] = mBluetoothManager.getAdapter().getRemoteDevice(deviceMacAddresses[i]);
            }
        } else {
            Log.e(TAG, "No Devices Queued, Restart!");
            Toast.makeText(this, "No Devices Queued, Restart!", Toast.LENGTH_SHORT).show();
        }
        mBluetoothLe = new BluetoothLe(this, mBluetoothManager, this);
        for (int i = 0; i < mBluetoothDeviceArray.length; i++) {
            mBluetoothGattArray[i] = mBluetoothLe.connect(mBluetoothDeviceArray[i], false);
            Log.e(TAG, "Connecting to Device: " + String.valueOf(mBluetoothDeviceArray[i].getName() + " " + mBluetoothDeviceArray[i].getAddress()));
            if ("WheelchairControl".equals(mBluetoothDeviceArray[i].getName())) {
                mWheelchairGattIndex = i;
                Log.e(TAG, "mWheelchairGattIndex: " + mWheelchairGattIndex);
            }
        }
    }

    private void setNameAddress(String name_action, String address_action) {
        MenuItem name = menu.findItem(R.id.action_title);
        MenuItem address = menu.findItem(R.id.action_address);
        name.setTitle(name_action);
        address.setTitle(address_action);
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        redrawer.finish();
        disconnectAllBLE();
        try {
            saveDataFile(true);
        } catch (IOException e) {
            Log.e(TAG, "IOException in saveDataFile");
            e.printStackTrace();
        }
        stopMonitoringRssiValue();
        super.onDestroy();
    }

    private void disconnectAllBLE() {
        if (mBluetoothLe != null) {
            for (BluetoothGatt bluetoothGatt : mBluetoothGattArray) {
                mBluetoothLe.disconnect(bluetoothGatt);
                mConnected = false;
                resetMenuBar();
            }
        }
    }

    private void resetMenuBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            if (menu != null) {
                menu.findItem(R.id.menu_connect).setVisible(true);
                menu.findItem(R.id.menu_disconnect).setVisible(false);
            }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_control, menu);
        getMenuInflater().inflate(R.menu.actionbar_item, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        this.menu = menu;
        setNameAddress(mDeviceName, mDeviceAddress);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                if (mBluetoothLe != null) {
                    initializeBluetoothArray();
                }
                connect();
                return true;
            case R.id.menu_disconnect:
                if (mBluetoothLe != null) {
                    disconnectAllBLE();
                }
                return true;
            case android.R.id.home:
                if (mBluetoothLe != null) {
                    disconnectAllBLE();
                }
                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connect() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MenuItem menuItem = menu.findItem(R.id.action_status);
                menuItem.setTitle("Connecting...");
            }
        });
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.i(TAG, "onServicesDiscovered");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            for (BluetoothGattService service : gatt.getServices()) {
                if ((service == null) || (service.getUuid() == null)) {
                    continue;
                }
                if (AppConstant.SERVICE_DEVICE_INFO.equals(service.getUuid())) {
                    //Read the device serial number
                    mBluetoothLe.readCharacteristic(gatt, service.getCharacteristic(AppConstant.CHAR_SERIAL_NUMBER));
                    //Read the device software version
                    mBluetoothLe.readCharacteristic(gatt, service.getCharacteristic(AppConstant.CHAR_SOFTWARE_REV));
                }
                if (AppConstant.SERVICE_WHEELCHAIR_CONTROL.equals(service.getUuid())) {
                    mLedService = service;
                    Log.i(TAG, "BLE Wheelchair Control Service found");
                }

                if (AppConstant.SERVICE_3CH_EMG_SIGNAL.equals(service.getUuid())) {
                    makeFilterSwitchVisible(true);
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_3CH_EMG_SIGNAL_CH1), true);
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_3CH_EMG_SIGNAL_CH2), true);
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_3CH_EMG_SIGNAL_CH3), true);
                }

                if (AppConstant.SERVICE_EEG_SIGNAL.equals(service.getUuid())) {
                    makeFilterSwitchVisible(true);
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH1_SIGNAL), true);
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH2_SIGNAL) != null)
                        mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH2_SIGNAL), true);
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH3_SIGNAL) != null)
                        mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH3_SIGNAL), true);
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH4_SIGNAL) != null) {
                        mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH4_SIGNAL), true);
                    }
                }

                if (AppConstant.SERVICE_EEG_GENERIC.equals(service.getUuid())) {
                    makeFilterSwitchVisible(true);
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH1_SIGNAL), true);
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH2_SIGNAL) != null) mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH2_SIGNAL), true);
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH3_SIGNAL) != null) mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH3_SIGNAL), true);
                }

                if (AppConstant.SERVICE_EOG_SIGNAL.equals(service.getUuid())) {
                    makeFilterSwitchVisible(true);
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EOG_CH1_SIGNAL), true);
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EOG_CH2_SIGNAL), true);
                    for (BluetoothGattCharacteristic c : service.getCharacteristics()) {
                        if (AppConstant.CHAR_EOG_CH3_SIGNAL.equals(c.getUuid())) {
                            mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_EOG_CH3_SIGNAL), true);
                        }
                    }
                }

                if (AppConstant.SERVICE_BATTERY_LEVEL.equals(service.getUuid())) { //Read the device battery percentage
                    mBluetoothLe.readCharacteristic(gatt, service.getCharacteristic(AppConstant.CHAR_BATTERY_LEVEL));
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_BATTERY_LEVEL), true);
                }

                if (AppConstant.SERVICE_MPU.equals(service.getUuid())) {
                    mBluetoothLe.setCharacteristicNotification(gatt, service.getCharacteristic(AppConstant.CHAR_MPU_COMBINED), true);
                }
            }
        }
    }

    private void makeFilterSwitchVisible(final boolean visible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            if (visible) {
                mFilterSwitch.setVisibility(View.VISIBLE);
                mExportButton.setVisibility(View.VISIBLE);
            } else {
                mExportButton.setVisibility(View.INVISIBLE);
                mFilterSwitch.setVisibility(View.INVISIBLE);
            }
            }
        });
    }

    private int batteryLevel = -1;

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.i(TAG, "onCharacteristicRead");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (AppConstant.CHAR_BATTERY_LEVEL.equals(characteristic.getUuid())) {
                batteryLevel = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0);
                updateBatteryStatus(batteryLevel);
                Log.i(TAG, "Battery Level :: " + batteryLevel);
            }
        } else {
            Log.e(TAG, "onCharacteristic Read Error" + status);
        }
    }
    public static final double INCREMENT_31_25 = 0.032;
    public static final double INCREMENT_2K = 0.0005;
    public static final int DIV_250 = 8;
    public static final double INCREMENT_4K = 0.00025;
    public static final double INCREMENT_8K = 0.000125;
    private boolean eeg_ch1_data_on = false;
    private boolean eeg_ch2_data_on = false;
    private boolean synchronized_2ch = false;
    private final int BUFFER_SIZE = 60;
    private short dataptCh1 = 0;
    private short dataptCh2 = 0;
    private double[] mDataPlotBufferCh1 = new double[BUFFER_SIZE];
    private double[] mDataPlotBufferCh2 = new double[BUFFER_SIZE];
    private int timestampIdxECG = 0;
    private int timestampIdxMPU = 0;
    private double[] timestamps;
    private double[] timestampsMPU;
    private int[] ecgCh1;
    private int[] ecgCh2;
    private short mDataPlotBufferIndexCh1 = 0;
    private short mDataPlotBufferIndexCh2 = 0;
    private int mGraphPlotIndexCh1 = 0; //ECG
    private int mGraphPlotIndexCh2 = 0; //ECG
    private int mMotionGraphPlotIndex = 0;
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        //TODO: ADD BATTERY MEASURE CAPABILITY IN FIRMWARE: (ble_ADC)
        if (AppConstant.CHAR_BATTERY_LEVEL.equals(characteristic.getUuid())) {
            batteryLevel = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0);
            updateBatteryStatus(batteryLevel);
            Log.i(TAG, "Battery Level :: " + batteryLevel);
        }

        if (AppConstant.CHAR_EEG_CH1_SIGNAL.equals(characteristic.getUuid())) {
            byte[] dataEEGBytes = characteristic.getValue();
            if (!eeg_ch1_data_on) {
                eeg_ch1_data_on = true;
            }
            getDataRateBytes(dataEEGBytes.length);
            ecgCh1 = new int[dataEEGBytes.length/3];
            timestamps = new double[dataEEGBytes.length/3];
            for (int i = 0; i < dataEEGBytes.length/3; i++) {
                ecgCh1[i] = unsignedToSigned(unsignedBytesToInt(dataEEGBytes[3*i+2],dataEEGBytes[3*i+1],dataEEGBytes[3*i]),24);
                timestamps[i] = (double) timestampIdxECG *(INCREMENT_2K);
                if(dataptCh1 == DIV_250) { //32 for 8k, 16 for 4k, 8 for 2k
                    double doubleValue = ((double)ecgCh1[i]/8388607.0)*2.25;
                    dataptCh1 = 0;
                    if(mDataPlotBufferIndexCh1 == BUFFER_SIZE) {
                        mDataPlotBufferIndexCh1 = 0;
                        for (int j = 0; j < BUFFER_SIZE; j++) {
                            mGraphAdapterCh1.addDataPoint(mDataPlotBufferCh1[j], mGraphPlotIndexCh1 - BUFFER_SIZE + j);
                        }
                    }
                    mDataPlotBufferCh1[mDataPlotBufferIndexCh1] = doubleValue;
                    mDataPlotBufferIndexCh1++;
                    mGraphPlotIndexCh1++;
                }
                if(synchronized_2ch) {
                    dataptCh1++;
                    timestampIdxECG++;
                }
            }
        }

        if (AppConstant.CHAR_EEG_CH2_SIGNAL.equals(characteristic.getUuid())) {
            byte[] dataEEGBytes = characteristic.getValue();
            if (!eeg_ch2_data_on) {
                eeg_ch2_data_on = true;
            }
            getDataRateBytes(dataEEGBytes.length);
            ecgCh2 = new int[dataEEGBytes.length/3];
            for (int i = 0; i < dataEEGBytes.length/3; i++) {
                ecgCh2[i] = unsignedToSigned(unsignedBytesToInt(dataEEGBytes[3*i+2],dataEEGBytes[3*i+1],dataEEGBytes[3*i]),24);
                if(dataptCh2 == DIV_250) { //32 for 8k, 16 for 4k
                    double doubleValue = ((double)ecgCh2[i]/8388607.0)*2.25;
                    dataptCh2 = 0;
                    if(mDataPlotBufferIndexCh2 == BUFFER_SIZE) {
                        mDataPlotBufferIndexCh2 = 0;
                        for (int j = 0; j < BUFFER_SIZE; j++) {
                            mGraphAdapterCh2.addDataPoint(mDataPlotBufferCh2[j], mGraphPlotIndexCh2 - BUFFER_SIZE + j);
                        }
                    }
                    mDataPlotBufferCh2[mDataPlotBufferIndexCh2] = doubleValue;
                    mDataPlotBufferIndexCh2++;
                    mGraphPlotIndexCh2++;
                }
                if(synchronized_2ch) dataptCh2++;
            }
        }

        if(AppConstant.CHAR_MPU_COMBINED.equals(characteristic.getUuid())) {
            byte[] dataMPU = characteristic.getValue();
            getDataRateBytes2(dataMPU.length); //+=240
            int IntArray[] = new int[dataMPU.length/2];
            double doublesArray[] = new double[dataMPU.length/2];
            timestampsMPU = new double[dataMPU.length/12];
            for (int i = 0; i < dataMPU.length/2; i++) {
                IntArray[i] = unsignedToSigned(unsignedBytesToInt(dataMPU[2*i+1],dataMPU[2*i]),16);

            }
            for (int i = 0; i < IntArray.length/*120*/; i+=6) {
                doublesArray[i] = 32*(double)IntArray[i]/65535.0;
                doublesArray[i+1] = 32*(double)IntArray[i+1]/65535.0;
                doublesArray[i+2] = 32*(double)IntArray[i+2]/65535.0;
                doublesArray[i+3] = 4000*(double)IntArray[i+3]/65535.0;
                doublesArray[i+4] = 4000*(double)IntArray[i+4]/65535.0;
                doublesArray[i+5] = 4000*(double)IntArray[i+5]/65535.0;
                mGraphAdapterMotionAX.addDataPointGeneric(mMotionGraphPlotIndex, doublesArray[i], 0.032);
                mGraphAdapterMotionAY.addDataPointGeneric(mMotionGraphPlotIndex, doublesArray[i+1], 0.032);
                mGraphAdapterMotionAZ.addDataPointGeneric(mMotionGraphPlotIndex, doublesArray[i+2], 0.032);
                mMotionGraphPlotIndex++;
            }
            for (int i = 0; i < dataMPU.length/12; i++) {
                timestampsMPU[i] = (double)timestampIdxMPU*(INCREMENT_31_25);
                timestampIdxMPU++;
            }
            writeToDiskMPU(timestampsMPU, doublesArray);
        }

        if (eeg_ch1_data_on && eeg_ch2_data_on) {

            eeg_ch1_data_on = false;
            eeg_ch2_data_on = false;
            if(synchronized_2ch && ecgCh1!=null && ecgCh2!=null) {
                writeToDisk24(timestamps, ecgCh1, ecgCh2);
            }
            synchronized_2ch = true;
        }

    }

    private double findGraphMax(SimpleXYSeries s) {
        if (s.size() > 0) {
            double max = (double) s.getY(0);
            for (int i = 1; i < s.size(); i++) {
                double a = (double) s.getY(i);
                if (a > max) {
                    max = a;
                }
            }
            return max;
        } else
            return 0.0;
    }

    private double findGraphMin(SimpleXYSeries s) {
        if (s.size() > 0) {
            double min = (double) s.getY(0);
            for (int i = 1; i < s.size(); i++) {
                double a = (double) s.getY(i);
                if (a < min) {
                    min = a;
                }
            }
            return min;
        } else {
            return 0.0;
        }
    }

    private void executeWheelchairCommand(int command) {
        byte[] bytes = new byte[1];
        switch (command) {
            case 0:
                bytes[0] = (byte) 0x00;
                break;
            case 1:
                bytes[0] = (byte) 0x01; //Stop
                break;
            case 2:
                bytes[0] = (byte) 0xF0; //?
                break;
            case 3:
                bytes[0] = (byte) 0x0F;
                break;
            case 4:
                bytes[0] = (byte) 0xFF;
                // TODO: 6/27/2017 Disconnect instead of reverse?
                break;
            default:
                break;
        }
        if (mLedService != null) {
            mBluetoothLe.writeCharacteristic(mBluetoothGattArray[mWheelchairGattIndex], mLedService.getCharacteristic(AppConstant.CHAR_WHEELCHAIR_CONTROL), bytes);
        }
    }

    private int unsignedToSigned(int unsigned, int size) {
        if ((unsigned & (1 << size - 1)) != 0) {
            unsigned = -1 * ((1 << size - 1) - (unsigned & ((1 << size - 1) - 1)));
        }
        return unsigned;
    }

    private int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }

    private int unsignedBytesToInt(byte b0, byte b1, byte b2) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8) + (unsignedByteToInt(b2) << 16));
    }

    /**
     * Convert a signed byte to an unsigned int.
     */
    private int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    private void writeToDiskMPU(final double[] timestampsMPU, final double[] dataArray) {
        if(dataArray.length>6) {
            for (int i = 0; i < dataArray.length; i+=6) {
                try {
                    exportFileMPU(timestampsMPU[i], dataArray[i],dataArray[i+1],dataArray[i+2],dataArray[i+3],dataArray[i+4],dataArray[i+5]);
                } catch (IOException e) {
                    Log.e("IOException", e.toString());
                }
            }
        }
    }

    private void writeToDisk24(final double[] timestamp, final int[] ecgCh1, final int[] ecgCh2) {
        try {
            for (int i=0; i<ecgCh1.length; i++) {
                exportDataToFileWithTimeStamp(timestamp[i],((double)ecgCh1[i]/8388607.0)*2.25,((double)ecgCh2[i]/8388607.0)*2.25);
            }
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }
    }

    private void writeToDisk24(final double ch1) {
        try {
            exportFileWithClass(ch1);
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }
    }

    private boolean lastThreeMatches(double[] yfitarray) {
        boolean b0 = false;
        boolean b1 = false;
        if (yfitarray[4] != 0) {
            b0 = (yfitarray[4] == yfitarray[3]);
            b1 = (yfitarray[3] == yfitarray[2]);
        }
        return b0 && b1;
    }

    private void getDataRateBytes(int bytes) {
        mCurrentTime = System.currentTimeMillis();
//        Log.e(TAG, "Packet Size: "+String.valueOf(bytes));
        points += bytes;
        if (mCurrentTime > (mLastTime + 3000)) {
            dataRate = (points / 3);
            points = 0;
            mLastTime = mCurrentTime;
            Log.e(" DataRate:", String.valueOf(dataRate) + " Bytes/s");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDataRate.setText(String.valueOf(dataRate) + " Bytes/s");
                }
            });
        }
    }
        private int points2 = 0;
    private void getDataRateBytes2(int bytes) {
        mCurrentTime2 = System.currentTimeMillis();
//        Log.e(TAG, "Packet Size: "+String.valueOf(bytes));
        points2 += bytes;
        if (mCurrentTime2 > (mLastTime2 + 3000)) {
            double DR2 = (points2 / 3);
            points2 = 0;
            mLastTime2 = mCurrentTime2;
            Log.e(" DataRate 2(MPU):", String.valueOf(DR2) + " Bytes/s");
        }
    }

    // Constants:
    public static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String toHexStringBigEndian(byte[] bytes) {
        //Big Endian = As they are assigned in byte array order:
        // [0, 1, ..., n]
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        uiRssiUpdate(rssi);
        String lastRssi = String.valueOf(rssi) + "db";
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
                mConnected = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (menu != null) {
                            menu.findItem(R.id.menu_connect).setVisible(false);
                            menu.findItem(R.id.menu_disconnect).setVisible(true);
                        }
                    }
                });
                Log.i(TAG, "Connected");
                updateConnectionState(getString(R.string.connected));
                invalidateOptionsMenu();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDataRate.setTextColor(Color.BLACK);
                        mDataRate.setTypeface(null, Typeface.NORMAL);
                    }
                });
                //Start the service discovery:
                gatt.discoverServices();
                startMonitoringRssiValue();
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                mConnected = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (menu != null) {
                            menu.findItem(R.id.menu_connect).setVisible(true);
                            menu.findItem(R.id.menu_disconnect).setVisible(false);
                        }
                    }
                });
                Log.i(TAG, "Disconnected");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDataRate.setTextColor(Color.RED);
                        mDataRate.setTypeface(null, Typeface.BOLD);
                        mDataRate.setText("0 Hz");
                    }
                });
                //TODO: ATTEMPT TO RECONNECT:
                updateConnectionState(getString(R.string.disconnected));
                stopMonitoringRssiValue();
                invalidateOptionsMenu();
                break;
            default:
                break;
        }
    }

    public void startMonitoringRssiValue() {
        readPeriodicallyRssiValue(true);
    }

    public void stopMonitoringRssiValue() {
        readPeriodicallyRssiValue(false);
    }

    public void readPeriodicallyRssiValue(final boolean repeat) {
        mTimerEnabled = repeat;
        // check if we should stop checking RSSI value
        if (!mConnected || mBluetoothGattArray == null || !mTimerEnabled) {
            mTimerEnabled = false;
            return;
        }

        mTimerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBluetoothGattArray == null || !mConnected) {
                    mTimerEnabled = false;
                    return;
                }
                // request RSSI value
                mBluetoothGattArray[0].readRemoteRssi();
                // add call it once more in the future
                readPeriodicallyRssiValue(mTimerEnabled);
            }
        }, RSSI_UPDATE_TIME_INTERVAL);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
            characteristic, int status) {
        Log.i(TAG, "onCharacteristicWrite :: Status:: " + status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        Log.i(TAG, "onDescriptorRead :: Status:: " + status);
    }

    @Override
    public void onError(String errorMessage) {
        Log.e(TAG, "Error:: " + errorMessage);
    }

    private void updateConnectionState(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (status.equals(getString(R.string.connected))) {
                    Toast.makeText(getApplicationContext(), "Device Connected!", Toast.LENGTH_SHORT).show();
                } else if (status.equals(getString(R.string.disconnected))) {
                    Toast.makeText(getApplicationContext(), "Device Disconnected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateBatteryStatus(final int integerValue) {
        final String status;
        double convertedBatteryVoltage = ((double) integerValue/(4096.0))*7.20;
        double finalPercent = (convertedBatteryVoltage/4.2)*100;
        Log.e(TAG,"Battery Integer Value: "+String.valueOf(integerValue));
        Log.e(TAG,"ConvertedBatteryVoltage: "+String.valueOf(convertedBatteryVoltage));
        status = String.format(Locale.US,"%.2f",convertedBatteryVoltage)+"V : "+String.format(Locale.US,"%.2f",finalPercent)+"%";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (integerValue <= batteryWarning) {
                    mBatteryLevel.setTextColor(Color.RED);
                    mBatteryLevel.setTypeface(null, Typeface.BOLD);
                    Toast.makeText(getApplicationContext(), "Charge Battery, Battery Low " + status, Toast.LENGTH_SHORT).show();
                } else {
                    mBatteryLevel.setTextColor(Color.GREEN);
                    mBatteryLevel.setTypeface(null, Typeface.BOLD);
                }
                mBatteryLevel.setText(status);
            }
        });
    }
    private void updateBatteryStatus(final int integerValue, final String status) {
        double convertedBatteryVoltage = ((double) integerValue/(4096.0))*7.20;
        Log.e(TAG,"ConvertedBatteryVoltage: "+String.valueOf(convertedBatteryVoltage));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (integerValue <= batteryWarning) {
                    mBatteryLevel.setTextColor(Color.RED);
                    mBatteryLevel.setTypeface(null, Typeface.BOLD);
                    Toast.makeText(getApplicationContext(), "Charge Battery, Battery Low " + status, Toast.LENGTH_SHORT).show();
                } else {
                    mBatteryLevel.setTextColor(Color.GREEN);
                    mBatteryLevel.setTypeface(null, Typeface.BOLD);
                }
                mBatteryLevel.setText(status);
            }
        });
    }

    private void uiRssiUpdate(final int rssi) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MenuItem menuItem = menu.findItem(R.id.action_rssi);
                MenuItem status_action_item = menu.findItem(R.id.action_status);
                final String valueOfRSSI = String.valueOf(rssi) + " dB";
                menuItem.setTitle(valueOfRSSI);
                if (mConnected) {
                    String newStatus = "Status: " + getString(R.string.connected);
                    status_action_item.setTitle(newStatus);
                } else {
                    String newStatus = "Status: " + getString(R.string.disconnected);
                    status_action_item.setTitle(newStatus);
                }
            }
        });
    }

    /*
    * Application of JNI code:
    */
    static {
        System.loadLibrary("android-jni");
    }

    public native double jniResultantGyro(double[] a);
    public native double jniResultantAcc(double[] a);
//    public native int jmainInitialization(boolean b);
//
//    public native double[] jClassifySSVEP(double[] a, double[] b, double c);

}
