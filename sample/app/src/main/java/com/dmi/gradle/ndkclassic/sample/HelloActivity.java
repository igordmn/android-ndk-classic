package com.dmi.gradle.ndkclassic.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HelloActivity extends Activity {
    static {
        System.loadLibrary("hello");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(getMessage());
        Toast.makeText(getApplicationContext(), HelloUniverse.getMessage(), Toast.LENGTH_LONG).show();
    }

    private native String getMessage();
}
