package com.example.capture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class Sub2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub2activity_main);

        Button female = findViewById(R.id.Female);
        Button male = findViewById(R.id.male);

        female.setOnClickListener(new View.OnClickListener() {
            @Override //이미지 불러오기기(갤러리 접근)
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Sex",0);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override //이미지 불러오기기(갤러리 접근)
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Sex",1);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
}