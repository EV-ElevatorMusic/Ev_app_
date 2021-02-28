package com.example.elevator_music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    TextView nameTv, emailTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        nameTv = findViewById(R.id.setting_name);
        emailTv = findViewById(R.id.setting_user_email);
        Intent intent = getIntent();
        nameTv.setText(intent.getStringExtra("name"));
        emailTv.setText(intent.getStringExtra("email"));

    }
}