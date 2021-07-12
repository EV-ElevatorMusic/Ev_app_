package com.example.elevator_music.Retrofit;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("preview_url")
    private String preview_url;

    @SerializedName("cover_img")
    private String cover_img;
    @SerializedName("artist_name")
    private String artist_name;
    @SerializedName("music_name")
    private String music_name;
    @SerializedName("view")
    private int view;

    public String getPreview_url() {
        return preview_url;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
