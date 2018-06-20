package com.study.nfc.testble;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText macEt;
    TextView statusTv;
    BluetoothAdapter bluetoothAdapter;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO 1. 화면이 뜰때 BT 활성화
        if (!bluetoothAdapter.isEnabled()) {
            //bluetoothAdapter.enable(); //비추: 그냥 켜짐ㅋㅋ
            // 사용자에게 물어 보고 켬
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }
    }

    public void registerDevice(View view) {
        devMacAdd = macEt.getText().toString().trim();
        statusTv.setText(statusTv.getText().toString() + "\n" +
                "등록 장치: "+ devMacAdd);
    }

    public void scanStart(View view) {
    }

    public void scanStop(View view) {
    }

    public void connectSensor(View view) {
    }
}
