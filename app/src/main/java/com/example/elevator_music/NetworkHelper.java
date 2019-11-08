package com.example.elevator_music;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class NetworkHelper {
    private static Retrofit retrofit = null;

    public static InputInterface getInstence(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl("https://api.dialogflow.com/v1/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(InputInterface.class);
    }

}
