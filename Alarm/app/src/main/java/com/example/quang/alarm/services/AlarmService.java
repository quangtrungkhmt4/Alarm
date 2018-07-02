package com.example.quang.alarm.services;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.quang.alarm.R;
import com.example.quang.alarm.model.ItemAlarm;
import com.example.quang.alarm.utils.DatabaseUtil;

import java.util.ArrayList;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;

    private NotificationCompat.Builder notBuilder;

    private int id;
    private String time;
    private String song;
    private String title;
    private int vibrate;

    private boolean isRunning;

    private Dialog dialogAlarm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        id = intent.getIntExtra("id",-1);
        time = intent.getStringExtra("time");
        title = intent.getStringExtra("title");
        song = intent.getStringExtra("song");
        vibrate = intent.getIntExtra("vibrate",1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = title;
            String description = time;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_alarm);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(time);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(111, mBuilder.build());
        }


        isRunning = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){
                    if (vibrate == 1){
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            long[] mVibratePattern = new long[]{0, 400, 200, 400};
                            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                            //v.vibrate(mVibratePattern,-1);
                        }else{
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();

        mediaPlayer = MediaPlayer.create(this, Uri.parse(song));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        showDialog(title,time);

        return START_STICKY;
    }


    private void showDialog(String title, String textTime){
        dialogAlarm = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialogAlarm.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialogAlarm.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialogAlarm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAlarm.setContentView(R.layout.window_manager_alarm);
        dialogAlarm.setCancelable(false);


        TextView tvDimiss = dialogAlarm.findViewById(R.id.tvDimissWM);
        TextView tvSnooze = dialogAlarm.findViewById(R.id.tvSnoozeWM);
        TextView tvTitle = dialogAlarm.findViewById(R.id.tvTitleWM);
        TextView tvTime = dialogAlarm.findViewById(R.id.tvTimeWM);

        tvTitle.setText(title);
        tvTime.setText(textTime);
        tvDimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseUtil databaseUtil = new DatabaseUtil(getApplicationContext());
                ArrayList<ItemAlarm> arrLarm = databaseUtil.getData();
                ItemAlarm itemAlarm = null;
                for (ItemAlarm item : arrLarm){
                    if (id == item.getId()){
                        itemAlarm = item;
                        break;
                    }
                }
                itemAlarm.setEnable(0);

                SharedPreferences pre=getSharedPreferences ("data_alarm",MODE_PRIVATE);
                String oldTime = pre.getString(id+"","null");
                if (!oldTime.equals("null")){
                    itemAlarm.setTime(oldTime);
                }

                databaseUtil.update(itemAlarm);
                SharedPreferences.Editor edit=pre.edit();
                edit.putString(id+"","null");
                edit.commit();
                dialogAlarm.dismiss();
                mediaPlayer.stop();
                isRunning = false;
                stopSelf();
            }
        });

        tvSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pre=getSharedPreferences ("data_alarm",MODE_PRIVATE);

                String oldTime = pre.getString(id+"","null");
                if (oldTime.equals("null")){
                    SharedPreferences.Editor edit=pre.edit();
                    edit.putString(id+"",time);
                    edit.commit();
                }

                String[] arr = time.split(":");
                String newTime;
                if (Integer.parseInt(arr[1]) == 59){
                    int h = Integer.parseInt(arr[0]) + 1;
                    int m = 0;

                    newTime = h+":"+m+"";
                }else {
                    int newMin = Integer.parseInt(arr[1]) + 1;
                    newTime = arr[0] + ":" + newMin + "";
                }
                DatabaseUtil databaseUtil = new DatabaseUtil(getApplicationContext());
                ArrayList<ItemAlarm> arrLarm = databaseUtil.getData();
                ItemAlarm itemAlarm = null;
                for (ItemAlarm item : arrLarm){
                    if (id == item.getId()){
                        itemAlarm = item;
                        break;
                    }
                }
                itemAlarm.setTime(newTime);
                databaseUtil.update(itemAlarm);
                dialogAlarm.dismiss();
                mediaPlayer.stop();
                isRunning = false;
                stopSelf();
            }
        });
        dialogAlarm.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialogAlarm != null){
            dialogAlarm.dismiss();
            mediaPlayer.stop();
            isRunning = false;
            stopSelf();
        }
    }
}
