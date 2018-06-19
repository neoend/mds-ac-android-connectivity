package com.study.nfc.nfctagwriter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void writeTextData(View view) {
        editText = findViewById(R.id.textEt);
        Intent intent = new Intent(this, TagWriteActivity.class);
        intent.putExtra("type", "T");
        intent.putExtra("data", editText.getText().toString());
        startActivity(intent);
    }

    public void writeLinkData(View view) {
        editText = findViewById(R.id.uriEt);
        Intent intent = new Intent(this, TagWriteActivity.class);
        intent.putExtra("type", "U");
        intent.putExtra("data", editText.getText().toString());
        startActivity(intent);
    }
}
