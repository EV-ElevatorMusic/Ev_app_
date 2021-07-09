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

import com.example.elevator_music.Retrofit.ApiClient;
import com.example.elevator_music.Retrofit.Data;
import com.example.elevator_music.Retrofit.RetrofitInterface;
import com.example.elevator_music.Retrofit.TestItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TestItem dataList;
    ArrayList<Data> dataInfo;

    RankingSadAdapter sadAdapter;

    RecyclerView.Adapter adapterHappy;
//    RecyclerView.Adapter adapterSad;
    RecyclerView.Adapter adapterMad;
    ArrayList<String> rankingHappyUrl = new ArrayList<>();
    ArrayList<String> rankingSadUrl = new ArrayList<>();
    ArrayList<String> rankingMadUrl = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        RecyclerView rvHappy, rvSad, rvMad;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvHappy = findViewById(R.id.musicHappy);
        rvMad = findViewById(R.id.musicMad);

        rankingHappyUrl.add("https://youtu.be/6vqT0LoiCKM");
        rankingHappyUrl.add("https://youtu.be/BizBCm3W4Ok");
        adapterHappy = new RankingHappyAdapter(rankingHappyUrl, getApplicationContext());
        rvHappy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHappy.setAdapter(adapterHappy);
        adapterMad = new RankingMadAdapter(rankingMadUrl, getApplicationContext());
        rvMad.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMad.setAdapter(adapterMad);


        dataInfo = new ArrayList<>();
        rvSad = findViewById(R.id.musicSad);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvSad.setLayoutManager(layoutManager);

        RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);

        Call<TestItem> call = retrofitInterface.getData();

        call.enqueue(new Callback<TestItem>() {
            @Override
            public void onResponse(Call<TestItem> call, Response<TestItem> response) {
                dataList = response.body();

                Log.d("RankingActivity", dataList.toString());

                dataInfo = dataList.musics;

                sadAdapter = new RankingSadAdapter(dataInfo, getApplicationContext());
                rvSad.setAdapter(sadAdapter);
            }

            @Override
            public void onFailure(Call<TestItem> call, Throwable t) {
                Log.d("RankingActivity", t.toString());
            }
        });


        //NavigationView navigationView = findViewById(R.id.nav_view2);

        //navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        drawer.openDrawer(GravityCompat.END);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;

        if (id == R.id.setting) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.mRanking) {
            intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
