package com.example.elevator_music.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitSadInterface {
    @GET("/music/music_list?emotion=sad")
    Call<TestItem> getData();
}