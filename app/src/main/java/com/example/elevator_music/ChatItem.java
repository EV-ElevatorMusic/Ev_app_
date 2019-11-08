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

    String id,lang,sessionId,timestamp, text;

    public ChatItem(int position, String text) {
        this.position = position;
        this.text = text;
    }

    JsonObject result,status;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public JsonObject getResult() {
        return result;
    }

    public void setResult(JsonObject result) {
        this.result = result;
    }

    public JsonObject getStatus() {
        return status;
    }

    public void setStatus(JsonObject status) {
        this.status = status;
    }
}
