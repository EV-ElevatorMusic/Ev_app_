package com.example.elevator_music.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitHappyInterface {
    @GET("/music/music_list?emotion=happy")
    Call<TestItem> getData();
}
