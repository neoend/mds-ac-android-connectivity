package com.study.nfc.nfctagwriter;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class TagWriteActivity extends AppCompatActivity {
    // Foreground

    TextView desc;
    NfcAdapter nfcAdapter;
    IntentFilter[] filters;
    PendingIntent pendingIntent;

    String type;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_write);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        data = intent.getStringExtra("data");

        desc = findViewById(R.id.descTv);

        // 1. NfcAdapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC 지원 안함", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 2. Tag 인식한 후 처리할 Intent 생성
        Intent i = new Intent(this, this.getClass());
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        IntentFilter ndefTag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters = new IntentFilter[]{ndefTag};
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        desc.setText("Tag detected~~!");
        // intent 에서 Tag 객체 구해서 Write
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            NdefMessage ndefMsg = makeNdefMsg(data, type);
            writeMessage(tag, ndefMsg);
            desc.setText("write~~! data: " + data);
        } else {
            desc.setText("fail~~~");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    private NdefMessage makeNdefMsg(String data, String type) {
        // 1. NdefRecord 생성
        NdefRecord rec = null;
        if ("T".equals(type)) {
            rec = NdefRecord.createTextRecord("en", data);
        } else if ("U".equals(type)) {
            rec = NdefRecord.createUri(data);
        }

        NdefRecord[] recArr = new NdefRecord[]{rec};
        //NdefRecord aarRec = NdefRecord.createApplicationRecord(pkg);
        //NdefRecord[] recArr = new NdefRecord[]{uriRec, aarRec};
        // 2. NdefMessage 생성
        NdefMessage ndefMsg = new NdefMessage(recArr);
        return ndefMsg;
    }

    private void writeMessage(Tag detectedTag, NdefMessage msg) {
        // Write 하기위해서는 Ndef 객체가 필요함.
        Ndef ndef = Ndef.get(detectedTag);
        try {
            ndef.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ndef.isWritable()) {
            try {
                ndef.writeNdefMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }
    }
}
