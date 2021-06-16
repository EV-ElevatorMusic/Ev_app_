package com.example.elevator_music;

import com.google.gson.JsonObject;

public class ChatItem {
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    String text;
    String artist_name;
    String cover_img;
    String music_name;
    String preview_url;

    public ChatItem(int position, String artist_name, String cover_img, String music_name, String preview_url) {
        this.position = position;
        this.artist_name = artist_name;
        this.cover_img = cover_img;
        this.music_name = music_name;
        this.preview_url = preview_url;
    }



    public ChatItem(int position, String text) {
        this.position = position;
        this.text = text;
    }

}
