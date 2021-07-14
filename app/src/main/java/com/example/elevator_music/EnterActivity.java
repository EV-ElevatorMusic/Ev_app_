package com.example.elevator_music;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EnterActivity extends AppCompatActivity {
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        enterButton = findViewById(R.id.enterButton);


        enterButton.setOnClickListener(v -> {
            Intent intent = new Intent(EnterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
