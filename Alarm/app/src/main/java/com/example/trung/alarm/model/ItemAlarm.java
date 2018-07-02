package com.example.trung.alarm.model;

public class ItemAlarm {

    private int id;
    private String title;
    private String time;
    private String loop;
    private int vibrate;
    private String sound;

    public ItemAlarm(int id, String title, String time, String loop, int vibrate, String sound) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.loop = loop;
        this.vibrate = vibrate;
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLoop() {
        return loop;
    }

    public void setLoop(String loop) {
        this.loop = loop;
    }

    public int isVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

    public int getVibrate() {
        return vibrate;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
