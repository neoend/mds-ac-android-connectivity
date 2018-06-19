package com.study.nfc.nfcforegroundreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView infoTv;
    NfcAdapter nfcAdapter;
    IntentFilter[] filters;
    PendingIntent pIntent;
    // p.48
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = findViewById(R.id.infoTv);

        // 1. NFCAdapter 얻어 오기
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC 지원 안함", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 2. Tag 인식한 후 처리할 Intent 생성
        Intent i = new Intent(this, this.getClass()); // 내가 직접 처리하겠다.
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pIntent = PendingIntent.getActivity(this, 0, i, 0);
        // 태그될때 어떤 태그를 처리할 것인가
        IntentFilter filterText = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter filterUri = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter filterUri2 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            filterText.addDataType("text/plain");
            filterUri.addDataScheme("http");
            filterUri2.addDataScheme("https");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        filters = new IntentFilter[]{filterText, filterUri, filterUri2};
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pIntent, filters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("INFO", "onNewIntent(); call");
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        String action = intent.getAction();
        infoTv.setText("action : " + action);

        if ("android.nfc.action.NDEF_DISCOVERED".equals(intent.getAction())) {
            Parcelable[] rawData = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            // 1st Message ; 일반적으로 메시지는 하나만 사용
            NdefMessage ndefMsg = (NdefMessage) rawData[0];
            // 2. Ndef records
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
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                    break;
                default:
                    // unknown type
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }
}
