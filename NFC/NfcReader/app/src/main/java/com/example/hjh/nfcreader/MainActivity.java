package com.example.hjh.nfcreader;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = findViewById(R.id.infoTv);

        Intent recI = getIntent();
        processIntent(recI);
//        String action = recI.getAction();
//        infoTv.setText("Received action: " + action);

    }

    private void processIntent(Intent i) {
        // 관례적으로 NDEF Message는 1개.
        Parcelable[] rawData = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawData == null) {
            return;
        }
        // 1st Message
        NdefMessage ndefMsg = (NdefMessage) rawData[0];
        // Ndef Records
        NdefRecord[] recArr = ndefMsg.getRecords();

        byte[] realData = recArr[0].getPayload();
        // p.41 참조
        String strData = new String(realData, 3, realData.length -3);
        String fullData = new String(realData);
        infoTv.setText("tag data: " + fullData);
    }
}
