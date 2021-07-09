package com.example.elevator_music.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TestItem {
    @SerializedName("musics")
    public ArrayList<Data> musics;

    @Override
    public String toString(){
        return "TestItem{"+"musics"+musics+"}";
    }
}
