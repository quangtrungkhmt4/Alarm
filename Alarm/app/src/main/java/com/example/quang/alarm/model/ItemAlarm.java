package com.example.quang.alarm.model;

import java.io.Serializable;

public class ItemAlarm implements Serializable{
    private int id;
    private String title;
    private String time;
    private String loop;
    private int vibrate;
    private String sound;

    private int isEnable; // 0: disable    1: enable

    public ItemAlarm(int id, String title, String time, String loop, int vibrate, String sound, int isEnable) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.loop = loop;
        this.vibrate = vibrate;
        this.sound = sound;
        this.isEnable = isEnable;
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

    public int getEnable() {
        return isEnable;
    }

    public void setEnable(int enable) {
        isEnable = enable;
    }
}
