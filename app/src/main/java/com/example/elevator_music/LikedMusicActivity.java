package com.example.elevator_music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.elevator_music.RankingAdapter.RankingAdapter;

public class LikedMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_music);
        RecyclerView recyclerView = findViewById(R.id.liked_music_rv);

        RankingAdapter adapter = new RankingAdapter(MainActivity.likedList, getApplicationContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}