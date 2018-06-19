package com.study.nfc.nfcforegroundreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
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
        IntentFilter ndefF = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefF.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }

        filters = new IntentFilter[]{ndefF};
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }
}
