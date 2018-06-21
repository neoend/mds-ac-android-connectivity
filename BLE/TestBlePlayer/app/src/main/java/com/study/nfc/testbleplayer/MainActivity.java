package com.study.nfc.testbleplayer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // TI BLE sensor
    String mymac = "5C:31:3E:87:AC:B1";
    UUID UUID_KEY_SERV = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    UUID UUID_KEY_DATA = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    UUID UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    BluetoothDevice bluetoothDevice;
    BluetoothGatt bluetoothGatt;
    TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "no bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }

        infoTv = findViewById(R.id.infoTv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bluetoothAdapter.stopLeScan(leScanCallback);
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
    }

    public void scanStart(View view) {
        showMessage("Start scan...");
        bluetoothAdapter.startLeScan(leScanCallback);
        // TODO deprecated!!!
        //bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        //bluetoothLeScanner.startScan(scanCallback);
    }
    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            // TODO
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    public void scanStop(View view) {
        showMessage("Stop scan...");
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

    public void connect(View view) {
        // connect & enable
        showMessage("Start connection...");
        bluetoothGatt = bluetoothDevice.connectGatt(this, false, bluetoothGattCallback);
    }

    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            // TODO scanRecord 가 p.84 PDU 부분. mac addr, data 등등..
            if (mymac.equals(device.getAddress())) {
                bluetoothDevice = device;
                bluetoothAdapter.stopLeScan(leScanCallback);
                showMessage("scan complete: " + bluetoothDevice);
            }
        }
    };

    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e("ble", "bluetoothGattCallback.onConnectionStateChange()");
            // 연결되면 discoverServices() 호출해야 한다.
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                showMessage("connected");
                Log.e("ble", "bluetoothGatt" + bluetoothGatt.toString());
                Log.e("ble", "gatt" + gatt.toString());
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                showMessage("disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            enableSensor();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            processKeyEventData(characteristic);
        }
    };

    private void enableSensor() {
        // KeyEvent
        BluetoothGattCharacteristic characteristic = bluetoothGatt.getService(UUID_KEY_SERV).getCharacteristic(UUID_KEY_DATA);
        bluetoothGatt.setCharacteristicNotification(characteristic, true);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_DESCRIPTOR);

        if (descriptor != null) {
            byte[] val = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
            descriptor.setValue(val);
            bluetoothGatt.writeDescriptor(descriptor);
        }
    }

    private void processKeyEventData(BluetoothGattCharacteristic characteristic) {
        // val 꺼내서 01 이면 play, 02 면 stop
        byte[] val = characteristic.getValue();
        System.out.println(val.toString());
    }

    private void showMessage(String msg) {
        final String data = msg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoTv.setText(infoTv.getText().toString() + "\n" + data);
            }
        });
    }
}
