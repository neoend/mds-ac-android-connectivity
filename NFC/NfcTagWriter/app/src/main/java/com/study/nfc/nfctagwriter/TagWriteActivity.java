package com.study.nfc.nfctagwriter;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TagWriteActivity extends AppCompatActivity {
    // Foreground
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_write);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // WRITE
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private NdefMessage makeNdefMsg(String data, String type) {

        return null;
    }

    private void writeMessage(Tag detectedTag, NdefMessage msg) {

    }
}
