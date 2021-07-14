package com.example.elevator_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.elevator_music.RankingAdapter.RankingAdapter;
import com.example.elevator_music.Retrofit.ApiClient;
import com.example.elevator_music.Retrofit.Data;
import com.example.elevator_music.Retrofit.RetrofitHappyInterface;
import com.example.elevator_music.Retrofit.RetrofitMadInterface;
import com.example.elevator_music.Retrofit.RetrofitSadInterface;
import com.example.elevator_music.Retrofit.TestItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity{

    TestItem dataList;
    ArrayList<Data> dataInfo;

    RankingAdapter adapter;

    RecyclerView rvHappy, rvSad, rvMad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        init();

    }

    private void init(){
        rvHappy = findViewById(R.id.musicHappy);
        rvMad = findViewById(R.id.musicMad);
        rvSad = findViewById(R.id.musicSad);

        dataInfo = new ArrayList<>();

        rvSad.setLayoutManager(new LinearLayoutManager(this));
        rvMad.setLayoutManager(new LinearLayoutManager(this));
        rvHappy.setLayoutManager(new LinearLayoutManager(this));

        RetrofitSadInterface sadInterface = ApiClient.getClient().create(RetrofitSadInterface.class);
        RetrofitHappyInterface happyInterface = ApiClient.getClient().create(RetrofitHappyInterface.class);
        RetrofitMadInterface madInterface = ApiClient.getClient().create(RetrofitMadInterface.class);

        Call<TestItem> sCall = sadInterface.getData();
        sCall.enqueue(new Callback<TestItem>() {
            @Override
            public void onResponse(Call<TestItem> call, Response<TestItem> response) {
                dataList = response.body();

                Log.d("RankingActivity", dataList.toString());

                dataInfo = dataList.musics;

                adapter = new RankingAdapter(dataInfo, getApplicationContext());
                rvSad.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TestItem> call, Throwable t) {
                Log.d("RankingActivity_error", t.toString());
            }
        });


        Call<TestItem> hCall = happyInterface.getData();
        hCall.enqueue(new Callback<TestItem>() {
            @Override
            public void onResponse(Call<TestItem> call, Response<TestItem> response) {
                dataList = response.body();

                Log.d("RankingActivity", dataList.toString());

                dataInfo = dataList.musics;

                adapter = new RankingAdapter(dataInfo, getApplicationContext());
                rvHappy.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TestItem> call, Throwable t) {
                Log.d("RankingActivity_error", t.toString());
            }
        });


        Call<TestItem> mCall = madInterface.getData();
        mCall.enqueue(new Callback<TestItem>() {
            @Override
            public void onResponse(Call<TestItem> call, Response<TestItem> response) {
                dataList = response.body();

                Log.d("RankingActivity", dataList.toString());

                dataInfo = dataList.musics;

                adapter = new RankingAdapter(dataInfo, getApplicationContext());
                rvMad.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TestItem> call, Throwable t) {
                Log.d("RankingActivity_error", t.toString());
            }
        });
    }
}
