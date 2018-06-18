package com.example.hjh.nfcreader;

import android.content.Intent;
import android.net.Uri;
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
        if ("android.nfc.action.NDEF_DISCOVERED".equals(i.getAction())) {
            System.out.println("android.nfc.action.NDEF_DISCOVERED");
        }
        // 관례적으로 NDEF Message는 1개.
        Parcelable[] rawData = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawData == null) {
            return;
        }
        // 1st Message
        NdefMessage ndefMsg = (NdefMessage) rawData[0];
        // 2. Ndef Records
        NdefRecord[] recArr = ndefMsg.getRecords();
        NdefRecord record = recArr[0];

        byte[] realData = record.getPayload();
        String type = new String(record.getType());
        // U, T, S
        String strData = null;
        System.out.println(realData);
        System.out.println("type: " + type);
        // p.41 참조
        switch (type.charAt(0)) {
            case 'T':
                strData = new String(realData, 3, realData.length -3);
                break;
            case 'U':
                //strData = new String(realData);
                Uri uri = record.toUri();
                strData = uri.toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            default:
                // unknown type
                break;
        }

        infoTv.setText("tag data: " + strData);
    }
}
