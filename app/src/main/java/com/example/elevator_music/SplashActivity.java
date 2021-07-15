package com.example.elevator_music;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends Activity {
    boolean isFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isFirst = sharedPreferences.getBoolean("isFirst", true);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2000);
    }

    private class splashhandler implements Runnable{
        public void run(){
            if (isFirst) startActivity(new Intent(getApplication(), ViewpagerActivity.class));
            else startActivity(new Intent(getApplication(), EnterActivity.class));
            SplashActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
