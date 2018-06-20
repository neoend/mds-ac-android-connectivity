package com.study.nfc.testble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText macEt;
    TextView statusTv;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothGatt bluetoothGatt;
    String devMacAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "bluetooth XXX", Toast.LENGTH_SHORT).show();
            finish();
        }

        macEt = findViewById(R.id.macEt);
        statusTv = findViewById(R.id.statusTv);

        registerDevice(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO 1. 화면이 뜰때 BT 활성화
        if (!bluetoothAdapter.isEnabled()) {
            //bluetoothAdapter.enable(); //비추: 그냥 켜짐ㅋㅋ
            // 사용자에게 물어 보고 켬
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
            // requestCode가 0보다 작으면 결과 값을 돌려받지 않겠다는 의미이며,
            // startActivity()와 동일하게 동작
        }

        // ??
        //requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ble", "request code: " + requestCode);
        Log.i("ble", "result code: " + resultCode);
        // TODO 1. -1: in case user turns on BT, 0: in case user doesn't turn on BT
        // Activity.RESULT_OK : -1, Activity.RESULT_CANCELED : 0
    }

    public void registerDevice(View view) {
        devMacAdd = macEt.getText().toString().trim();
        showMessage("등록 장치: " + devMacAdd);
    }

    private void showMessage(String msg) {
        final String data = msg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusTv.setText(statusTv.getText().toString() + "\n" + data);
            }
        });
    }

    public void scanStart(View view) {
        // TODO 2. BLE Scan
        showMessage("Scan Start.");
        bluetoothAdapter.startLeScan(scanCallback);
    }

    public void scanStop(View view) {
        // TODO 3. Stop scan
        bluetoothAdapter.stopLeScan(scanCallback);
        showMessage("Stop Scan");
    }

    public void connectSensor(View view) {
        showMessage("Start connection...");
        // TODO 4. connect after scan
        bluetoothGatt = bluetoothDevice.connectGatt(this, false, bluetoothGattCallback);
    }

    BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        // rssi는 수신 신호 세기
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("ble", "LeScanCallback: " + device.getAddress());

            if (devMacAdd.equals(device.getAddress())) {
                bluetoothDevice = device;
                bluetoothAdapter.stopLeScan(scanCallback);
                showMessage("scan complete: " + bluetoothDevice);
            }
        }
    };

    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.i("ble", "onConnectionStateChange()");
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("ble", "conection complete with sensor");
                showMessage("sensor 와 연결 완료");
                // TODO 5. 연결되면 discover services 하면 아래 onServicesDiscovered() 가 콜백된다.
                // TODO 이건 꼭 호출해야 함. 호출 하지 않으면 이후 과정이 동작하지 않음.
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("ble", "Disconection complete with sensor");
                showMessage("sensor 와 연결 종료");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i("ble", "onServicesDiscovered()");
            // TODO 6.
            List<BluetoothGattService> gattServiceList = gatt.getServices();
            for (BluetoothGattService blueSev : gattServiceList) {
                System.out.println("BluetoothGattService:: " + blueSev.getUuid());
                List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = blueSev.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : bluetoothGattCharacteristics) {
                    System.out.println("BluetoothGattCharacteristic:: " + characteristic.getUuid());
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i("ble", "onCharacteristicRead()");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i("ble", "onCharacteristicWrite()");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i("ble", "onCharacteristicChanged()");
        }
    };
}
