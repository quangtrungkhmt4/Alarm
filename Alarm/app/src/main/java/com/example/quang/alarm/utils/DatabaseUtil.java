package com.example.quang.alarm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.quang.alarm.model.ItemAlarm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DatabaseUtil {
    public static final String PATH = Environment.getDataDirectory().getPath()
            + "/data/com.example.quang.alarm/databases/";

    public static final String DB_NAME = "alarm.sqlite";
    public static final String TABLE_NAME = "TBLAlarm";
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String TIME = "TIME";
    public static final String LOOP = "LOOP";
    public static final String VIBRATE = "VIBRATE";
    public static final String SOUND = "SOUND";
    public static final String ENABLE = "ENABLE";

    private Context context;
    private SQLiteDatabase database;

    public DatabaseUtil(Context context) {
        this.context = context;
        copyFileToDevice();
    }

    private void copyFileToDevice() {
        File file = new File(PATH + DB_NAME);
        if (!file.exists()) {
            File parent = file.getParentFile();
            parent.mkdirs();
            try {
                InputStream inputStream = context.getAssets().open(DB_NAME);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int count = inputStream.read(b);
                while (count != -1) {
                    outputStream.write(b, 0, count);
                    count = inputStream.read(b);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    private void closeDatabase() {
        database.close();
    }

    public ArrayList<ItemAlarm> getData() {
        ArrayList<ItemAlarm> arr = new ArrayList<>();
        openDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        int indexId = cursor.getColumnIndex(ID);
        int indexTITLE = cursor.getColumnIndex(TITLE);
        int indexTIME = cursor.getColumnIndex(TIME);
        int indexLOOP = cursor.getColumnIndex(LOOP);
        int indexVIBRATE = cursor.getColumnIndex(VIBRATE);
        int indexSOUND = cursor.getColumnIndex(SOUND);
        int indexENABLE = cursor.getColumnIndex(ENABLE);

        while (cursor.isAfterLast() == false) {
            int id = cursor.getInt(indexId);
            String title = cursor.getString(indexTITLE);
            String time = cursor.getString(indexTIME);
            String loop = cursor.getString(indexLOOP);
            int vibrate = cursor.getInt(indexVIBRATE);
            String sound = cursor.getString(indexSOUND);
            int enable = cursor.getInt(indexENABLE);

            ItemAlarm alarm = new ItemAlarm(id,title,time,loop,vibrate,sound,enable);
            arr.add(alarm);
            cursor.moveToNext();
        }
        closeDatabase();
        return arr;
    }

    public long insert(ItemAlarm item) {
        ContentValues values = new ContentValues();
        values.put(ID, item.getId());
        values.put(TITLE, item.getTitle());
        values.put(TIME, item.getTime());
        values.put(LOOP, item.getLoop());
        values.put(VIBRATE, item.getVibrate());
        values.put(SOUND, item.getSound());
        values.put(ENABLE, item.getEnable());
        openDatabase();
        long id = database.insert(TABLE_NAME, null, values);
        closeDatabase();
        return id;
    }

    public int update(ItemAlarm item) {
        ContentValues values = new ContentValues();
        values.put(TITLE, item.getTitle());
        values.put(TIME, item.getTime());
        values.put(LOOP, item.getLoop());
        values.put(VIBRATE, item.getVibrate());
        values.put(SOUND, item.getSound());
        values.put(ENABLE, item.getEnable());
        openDatabase();
        String where = ID + " = ?";
        String[] whereAgrs = {item.getId() + ""};
        int rows = database.update(TABLE_NAME, values, where, whereAgrs);
        closeDatabase();
        return rows;
    }

    public int delete(int id){
        openDatabase();
        String where = ID + " = ?";
        String[] whereAgrs = {id + ""};
        int rows = database.delete(TABLE_NAME,where,whereAgrs);
        closeDatabase();
        return rows;
    }
}
