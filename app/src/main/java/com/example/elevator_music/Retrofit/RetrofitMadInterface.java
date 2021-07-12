package com.example.elevator_music.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitMadInterface {
    @GET("/music/music_list?emotion=mad")
    Call<TestItem> getData();
}
