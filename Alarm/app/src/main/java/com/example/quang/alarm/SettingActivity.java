package com.example.quang.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.quang.alarm.model.ItemAlarm;
import com.example.quang.alarm.utils.DatabaseUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import static java.lang.Boolean.TRUE;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView tvSelectTime;
    private EditText edtTitle;
    private CheckBox cbLoop;
    private CheckBox cbVibrate;
    private TextView tvSelectSound;
    private LinearLayout lnLoop;
    private FloatingActionButton btnDone;

    private TextView tvMo;
    private TextView tvTu;
    private TextView tvWe;
    private TextView tvTh;
    private TextView tvFr;
    private TextView tvSa;
    private TextView tvSu;
    private ArrayList<TextView> arrTv;

    private boolean checkMo;
    private boolean checkTu;
    private boolean checkWe;
    private boolean checkTh;
    private boolean checkFr;
    private boolean checkSa;
    private boolean checkSu;
    private ArrayList<Boolean> arrCheckDateLoop;

    private boolean checkVibrate;

    private Dialog dialogSong;

    private DatabaseUtil databaseUtil;

    private boolean checkExists;
    private ItemAlarm intentAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findID();
        initViews();
        loadData();
        initDialogSong();
    }

    private void findID() {
        tvSelectTime = findViewById(R.id.tvSelectTime);
        edtTitle = findViewById(R.id.edtTitle);
        cbLoop = findViewById(R.id.checkBoxLoop);
        cbVibrate = findViewById(R.id.checkVibrate);
        tvSelectSound = findViewById(R.id.tvSetSongAlarm);
        btnDone = findViewById(R.id.btnDone);
        lnLoop = findViewById(R.id.lnChooseDateLoop);

        tvMo = findViewById(R.id.tvMo);
        tvTu = findViewById(R.id.tvTu);
        tvWe = findViewById(R.id.tvWe);
        tvTh = findViewById(R.id.tvTh);
        tvFr = findViewById(R.id.tvFr);
        tvSa = findViewById(R.id.tvSa);
        tvSu = findViewById(R.id.tvSu);
    }


    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        btnDone.setOnClickListener(this);
        tvSelectTime.setOnClickListener(this);
        tvSelectSound.setOnClickListener(this);

        tvMo.setOnClickListener(this);
        tvTu.setOnClickListener(this);
        tvWe.setOnClickListener(this);
        tvTh.setOnClickListener(this);
        tvFr.setOnClickListener(this);
        tvSa.setOnClickListener(this);
        tvSu.setOnClickListener(this);

        arrTv = new ArrayList<>();
        arrTv.add(tvMo);
        arrTv.add(tvTu);
        arrTv.add(tvWe);
        arrTv.add(tvTh);
        arrTv.add(tvFr);
        arrTv.add(tvSa);
        arrTv.add(tvSu);

        arrCheckDateLoop = new ArrayList<>();
        arrCheckDateLoop.add(checkMo);
        arrCheckDateLoop.add(checkTu);
        arrCheckDateLoop.add(checkWe);
        arrCheckDateLoop.add(checkTh);
        arrCheckDateLoop.add(checkFr);
        arrCheckDateLoop.add(checkSa);
        arrCheckDateLoop.add(checkSu);

        cbLoop.setOnCheckedChangeListener(this);
        cbVibrate.setOnCheckedChangeListener(this);
        checkExists = false;
    }

    private void loadData() {
        Intent intent = getIntent();
        if (!intent.getStringExtra("setting").equals("null")){
            checkExists = true;
            ItemAlarm itemAlarm = (ItemAlarm) intent.getSerializableExtra("alarm");
            intentAlarm = itemAlarm;
            tvSelectTime.setText(itemAlarm.getTime());
            edtTitle.setText(itemAlarm.getTitle());
            if (!itemAlarm.getLoop().isEmpty()){
                cbLoop.setChecked(true);

                String[] arrLp = itemAlarm.getLoop().split(",");

                for (int i=0; i<arrLp.length; i++){
                    for (int j=0; j<arrTv.size(); j++){
                        if (arrTv.get(j).getText().toString().equals(arrLp[i])){
                            arrTv.get(j).setBackgroundColor(Color.parseColor("#099191"));
                            arrTv.get(j).setTextColor(Color.parseColor("#333333"));
                            arrCheckDateLoop.set(j,true);
                        }
                    }
                }
            }

            if (!itemAlarm.getSound().contains("android.resource:")){
                tvSelectSound.setText(itemAlarm.getSound());
            }

            if (itemAlarm.getVibrate() == 1){
                cbVibrate.setChecked(true);
            }else {
                cbVibrate.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSelectTime:
                showDialogTime();
                break;
            case R.id.tvSetSongAlarm:
                dialogSong.show();
                break;
            case R.id.tvMo:
                if (!arrCheckDateLoop.get(0)){
                    arrTv.get(0).setBackgroundColor(Color.parseColor("#099191"));
                    arrTv.get(0).setTextColor(Color.parseColor("#333333"));
                    arrCheckDateLoop.set(0,true);
                }else {
                    arrTv.get(0).setBackgroundColor(Color.parseColor("#00ffffff"));
                    arrTv.get(0).setTextColor(Color.parseColor("#099191"));
                    arrCheckDateLoop.set(0,false);
                }
                break;
            case R.id.tvTu:
                if (!arrCheckDateLoop.get(1)){
                    arrTv.get(1).setBackgroundColor(Color.parseColor("#099191"));
                    arrTv.get(1).setTextColor(Color.parseColor("#333333"));
                    arrCheckDateLoop.set(1,true);
                }else {
                    arrTv.get(1).setBackgroundColor(Color.parseColor("#00ffffff"));
                    arrTv.get(1).setTextColor(Color.parseColor("#099191"));
                    arrCheckDateLoop.set(1,false);
                }
                break;
            case R.id.tvWe:
                if (!arrCheckDateLoop.get(2)){
                    arrTv.get(2).setBackgroundColor(Color.parseColor("#099191"));
                    arrTv.get(2).setTextColor(Color.parseColor("#333333"));
                    arrCheckDateLoop.set(2,true);
                }else {
                    arrTv.get(2).setBackgroundColor(Color.parseColor("#00ffffff"));
                    arrTv.get(2).setTextColor(Color.parseColor("#099191"));
                    arrCheckDateLoop.set(2,false);
                }
                break;
            case R.id.tvTh:
                if (!arrCheckDateLoop.get(3)){
                    arrTv.get(3).setBackgroundColor(Color.parseColor("#099191"));
                    arrTv.get(3).setTextColor(Color.parseColor("#333333"));
                    arrCheckDateLoop.set(3,true);
                }else {
                    arrTv.get(3).setBackgroundColor(Color.parseColor("#00ffffff"));
                    arrTv.get(3).setTextColor(Color.parseColor("#099191"));
                    arrCheckDateLoop.set(3,false);
                }
                break;
            case R.id.tvFr:
                if (!arrCheckDateLoop.get(4)){
                    arrTv.get(4).setBackgroundColor(Color.parseColor("#099191"));
                    arrTv.get(4).setTextColor(Color.parseColor("#333333"));
                    arrCheckDateLoop.set(4,true);
                }else {
                    arrTv.get(4).setBackgroundColor(Color.parseColor("#00ffffff"));
                    arrTv.get(4).setTextColor(Color.parseColor("#099191"));
                    arrCheckDateLoop.set(4,false);
                }
                break;
            case R.id.tvSa:
                if (!arrCheckDateLoop.get(5)){
                    arrTv.get(5).setBackgroundColor(Color.parseColor("#099191"));
                    arrTv.get(5).setTextColor(Color.parseColor("#333333"));
                    arrCheckDateLoop.set(5,true);
                }else {
                    arrTv.get(5).setBackgroundColor(Color.parseColor("#00ffffff"));
                    arrTv.get(5).setTextColor(Color.parseColor("#099191"));
                    arrCheckDateLoop.set(5,false);
                }
                break;
            case R.id.tvSu:
                if (!arrCheckDateLoop.get(6)){
                    arrTv.get(6).setBackgroundColor(Color.parseColor("#099191"));
                    arrTv.get(6).setTextColor(Color.parseColor("#333333"));
                    arrCheckDateLoop.set(6,true);
                }else {
                    arrTv.get(6).setBackgroundColor(Color.parseColor("#00ffffff"));
                    arrTv.get(6).setTextColor(Color.parseColor("#099191"));
                    arrCheckDateLoop.set(6,false);
                }
                break;
            case R.id.btnDone:
                Date currentTime = Calendar.getInstance().getTime();
                int date = currentTime.getDate();
                int year = currentTime.getYear();
                int hour = currentTime.getHours();
                int sec = currentTime.getSeconds();

                String str = date+""+year+""+hour+""+sec+"";

                int id = Integer.parseInt(str);

                String loop = "";
                if (arrCheckDateLoop.get(0)){
                    loop = loop + "Mo,";
                }
                if (arrCheckDateLoop.get(1)){
                    loop = loop + "Tu,";
                }
                if (arrCheckDateLoop.get(2)){
                    loop = loop + "We,";
                }
                if (arrCheckDateLoop.get(3)){
                    loop = loop + "Th,";
                }
                if (arrCheckDateLoop.get(4)){
                    loop = loop + "Fr,";
                }
                if (arrCheckDateLoop.get(5)){
                    loop = loop + "Sa,";
                }
                if (arrCheckDateLoop.get(6)){
                    loop = loop + "Su,";
                }

                String strLoop = "";
                if (!loop.isEmpty()){
                    char[] arrChar = loop.toCharArray();
                    for (int i=0; i<arrChar.length - 1; i++) {
                        strLoop += arrChar[i];
                    }
                }

                int vib = 0;
                if (checkVibrate){
                    vib = 1;
                }else {
                    vib = 0;
                }

                String soundPath = tvSelectSound.getText().toString();
                if (soundPath.contains("Default(Spirit)")){
                    soundPath = "android.resource://com.example.quang.alarm/raw/spirit";
                }

                ItemAlarm itemAlarm = new ItemAlarm(id,edtTitle.getText().toString(),tvSelectTime.getText().toString()
                        ,strLoop,vib,soundPath,0);

                databaseUtil = new DatabaseUtil(this);

                if (!checkExists){
                    databaseUtil.insert(itemAlarm);
                }else {
                    ItemAlarm itemAlarm1 = new ItemAlarm(intentAlarm.getId(),edtTitle.getText().toString(),tvSelectTime.getText().toString()
                            ,strLoop,vib,soundPath,0);
                    databaseUtil.update(itemAlarm1);
                }
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.checkBoxLoop:
                if (b){
                    lnLoop.setVisibility(View.VISIBLE);
                    Collections.fill(arrCheckDateLoop, Boolean.FALSE);
                    resetChooseLoop();
                }else {
                    lnLoop.setVisibility(View.GONE);
                    Collections.fill(arrCheckDateLoop, Boolean.FALSE);
                }
                break;
            case R.id.checkVibrate:
                if (b){
                    checkVibrate = true;
                }else {
                    checkVibrate = false;
                }
                break;
        }
    }

    private void resetChooseLoop(){
        for (TextView tv : arrTv){
            tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            tv.setTextColor(Color.parseColor("#099191"));
        }
    }

    public void showDialogTime(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        String hour = hourOfDay + "";
                        String min = minute + "";
                        if (hourOfDay - 10 < 0){
                            hour = "0"+hour;
                        }
                        if (minute - 10 < 0){
                            min = "0"+minute;
                        }

                        tvSelectTime.setText(hour + ":" + min);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void initDialogSong() {
        dialogSong = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialogSong.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSong.setContentView(R.layout.dialog_choose_alarm_song);

        TextView tvAnotherSong = dialogSong.findViewById(R.id.tvChooseAnotherSong);
        TextView tvSystemSong = dialogSong.findViewById(R.id.tvSystemSong);


        tvAnotherSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("audio/mp3");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                        intent, "Open Audio (mp3) file"), 111);
            }
        });

        tvSystemSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSong.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 111){
                tvSelectSound.setText(data.getData().getPath());
                dialogSong.dismiss();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        else if (id == R.id.action_delete){
            DatabaseUtil database = new DatabaseUtil(this);
            database.delete(intentAlarm.getId());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
