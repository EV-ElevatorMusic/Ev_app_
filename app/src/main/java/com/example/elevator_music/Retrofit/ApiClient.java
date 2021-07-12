package com.example.elevator_music.Retrofit;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class ApiClient {
    public static String BASE_URL = "http://34.64.94.120/";

    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
