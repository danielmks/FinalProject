package com.example.capture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent resultIntent = getIntent();
        String measure = resultIntent.getStringExtra("measurement");
        @SuppressLint("WrongViewCast") TextView tf_output = findViewById(R.id.test_text);
        tf_output.setText(measure);
    }
}
