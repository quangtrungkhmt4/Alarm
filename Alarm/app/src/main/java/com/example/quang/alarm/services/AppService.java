package com.example.quang.alarm.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.quang.alarm.model.ItemAlarm;
import com.example.quang.alarm.utils.DatabaseUtil;

import java.util.ArrayList;
import java.util.Calendar;
public class AppService extends Service {

    private Thread thread;
    private boolean isRunning;
    private DatabaseUtil databaseUtil;

    private boolean checkStartIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseUtil = new DatabaseUtil(this);
        isRunning = true;
        checkStartIntent = false;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    ArrayList<ItemAlarm> arrAlarm = databaseUtil.getData();
                    String currentTime = Calendar.getInstance().getTime().toString();

                    for (ItemAlarm alarm : arrAlarm){
                        if (alarm.getEnable() == 1){
                            String time = alarm.getTime();
                            String[] hourMin = time.split(":");
                            int hour = Integer.parseInt(hourMin[0]);
                            int min = Integer.parseInt(hourMin[1]);

                            char[] arrCurrentTime = currentTime.toCharArray();
                            String str = String.valueOf(arrCurrentTime[0]) + String.valueOf(arrCurrentTime[1]);

                            if (alarm.getLoop().contains(str)){
                                if (hour == Calendar.getInstance().getTime().getHours()){
                                    if(min == Calendar.getInstance().getTime().getMinutes()){
                                        Log.e("-------------------",isMyServiceRunning(AlarmService.class)+"");
                                        if (!isMyServiceRunning(AlarmService.class)){
                                            Intent i = new Intent(AppService.this,AlarmService.class);
                                            i.putExtra("title",alarm.getTitle());
                                            i.putExtra("time",alarm.getTime());
                                            i.putExtra("id",alarm.getId());
                                            i.putExtra("song",alarm.getSound());
                                            i.putExtra("vibrate",alarm.getVibrate());
                                            checkStartIntent = true;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                startForegroundService(i);
                                            }
                                            else {
                                                startService(i);
                                            }
                                        }
                                    }else if (min < Calendar.getInstance().getTime().getMinutes()){
                                        checkStartIntent = false;
                                    }
                                }
                            }


                        }
                    }

                    Log.e("-----",currentTime);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread.start();

//        Intent i = new Intent(AppService.this,AlarmService.class);
//        startService(i);
        return START_STICKY;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
