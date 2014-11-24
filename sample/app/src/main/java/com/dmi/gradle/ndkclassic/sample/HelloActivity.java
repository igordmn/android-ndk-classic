package com.dmi.gradle.ndkclassic.sample;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

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
    }

    private native String getMessage();
}
