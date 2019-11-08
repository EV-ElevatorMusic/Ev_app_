package com.example.elevator_music;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface InputInterface {
    @Headers({"Authorization:Bearer 957ea642f45647e98a071eaacd6b73bf","Content-Type:application/json; charset=utf-8"})
    @GET("query")
    Call<ChatItem> get_Weather_retrofit(@Query("v") String v, @Query("query") String query,
                                        @Query("lang") String lang, @Query("sessionId") String sessionId);
}
