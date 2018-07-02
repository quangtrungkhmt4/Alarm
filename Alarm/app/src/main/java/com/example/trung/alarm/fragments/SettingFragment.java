package com.example.trung.alarm.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.trung.alarm.MainActivity;
import com.example.trung.alarm.R;
import com.example.trung.alarm.model.ItemAlarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Toolbar toolbar;
    private ImageView btnBackSetting;

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

    private boolean checkVibrate;

    private MainActivity mainActivity;

    private Dialog dialogSong;

    private boolean checkCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flagment_setting,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findId();
        initViews();

        initDialogSong();

        if (mainActivity.getCurrentAlarm() != null){
            tvSelectTime.setText(mainActivity.getCurrentAlarm().getTime());
            edtTitle.setText(mainActivity.getCurrentAlarm().getTitle());
            if (!mainActivity.getCurrentAlarm().getLoop().isEmpty()){
                cbLoop.setChecked(true);
                String lp = mainActivity.getCurrentAlarm().getLoop();
                String[] arrLp = lp.split(",");

                for (int i=0; i<arrLp.length; i++){
                    for (int j=0; j<arrTv.size(); j++){
                        if (arrTv.get(j).getText().toString().equals(arrLp[i])){
                            arrTv.get(j).setBackgroundColor(Color.parseColor("#A6A5A1"));
                            arrTv.get(j).setTextColor(Color.parseColor("#485058"));
                        }
                    }
                }
            }
        }
    }

    private void findId() {
        toolbar = getActivity().findViewById(R.id.toolBarSetting);
        btnBackSetting = getActivity().findViewById(R.id.btnBackSetting);
        tvSelectTime = getActivity().findViewById(R.id.tvSelectTime);
        edtTitle = getActivity().findViewById(R.id.edtTitle);
        cbLoop = getActivity().findViewById(R.id.checkBoxLoop);
        cbVibrate = getActivity().findViewById(R.id.checkVibrate);
        tvSelectSound = getActivity().findViewById(R.id.tvSetSongAlarm);
        btnDone = getActivity().findViewById(R.id.btnDone);
        lnLoop = getActivity().findViewById(R.id.lnChooseDateLoop);

        tvMo = getActivity().findViewById(R.id.tvMo);
        tvTu = getActivity().findViewById(R.id.tvTu);
        tvWe = getActivity().findViewById(R.id.tvWe);
        tvTh = getActivity().findViewById(R.id.tvTh);
        tvFr = getActivity().findViewById(R.id.tvFr);
        tvSa = getActivity().findViewById(R.id.tvSa);
        tvSu = getActivity().findViewById(R.id.tvSu);
    }

    private void initViews() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
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

        btnBackSetting.setOnClickListener(this);

        mainActivity = (MainActivity) getActivity();

        cbLoop.setOnCheckedChangeListener(this);
        cbVibrate.setOnCheckedChangeListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBackSetting:
                mainActivity.removeFragment(this);
                break;
            case R.id.tvSelectTime:
                showDialogTime();
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
                if (checkMo){
                    loop = loop + "Mo,";
                }
                if (checkTu){
                    loop = loop + "Tu,";
                }
                if (checkWe){
                    loop = loop + "We,";
                }
                if (checkTh){
                    loop = loop + "Th,";
                }
                if (checkFr){
                    loop = loop + "Fr,";
                }
                if (checkSa){
                    loop = loop + "Sa,";
                }
                if (checkSu){
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
                    soundPath = "android.resource://com.example.trung.alarm/raw/spirit";
                }


                ItemAlarm itemAlarm = new ItemAlarm(id,edtTitle.getText().toString(),tvSelectTime.getText().toString()
                    ,strLoop,vib,soundPath);

                mainActivity.getDatabase().insert(itemAlarm);

                mainActivity.removeFragment(this);
                mainActivity.removeFragment(mainActivity.getMainFragment());
                mainActivity.addFrawment(mainActivity.getNewMainFragment());
                mainActivity.showBtnAdd();

                break;
            case R.id.tvSetSongAlarm:
                dialogSong.show();
                break;
            case R.id.tvMo:
                if (!checkMo){
                    tvMo.setBackgroundColor(Color.parseColor("#A6A5A1"));
                    tvMo.setTextColor(Color.parseColor("#485058"));
                    checkMo = true;
                }else {
                    tvMo.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tvMo.setTextColor(Color.parseColor("#A6A5A1"));
                    checkMo = false;
                }
                break;
            case R.id.tvTu:
                if (!checkTu){
                    tvTu.setBackgroundColor(Color.parseColor("#A6A5A1"));
                    tvTu.setTextColor(Color.parseColor("#485058"));
                    checkTu = true;
                }else {
                    tvTu.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tvTu.setTextColor(Color.parseColor("#A6A5A1"));
                    checkTu = false;
                }
                break;
            case R.id.tvWe:
                if (!checkWe){
                    tvWe.setBackgroundColor(Color.parseColor("#A6A5A1"));
                    tvWe.setTextColor(Color.parseColor("#485058"));
                    checkWe = true;
                }else {
                    tvWe.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tvWe.setTextColor(Color.parseColor("#A6A5A1"));
                    checkWe = false;
                }
                break;
            case R.id.tvTh:
                if (!checkTh){
                    tvTh.setBackgroundColor(Color.parseColor("#A6A5A1"));
                    tvTh.setTextColor(Color.parseColor("#485058"));
                    checkTh = true;
                }else {
                    tvTh.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tvTh.setTextColor(Color.parseColor("#A6A5A1"));
                    checkTh = false;
                }
                break;
            case R.id.tvFr:
                if (!checkFr){
                    tvFr.setBackgroundColor(Color.parseColor("#A6A5A1"));
                    tvFr.setTextColor(Color.parseColor("#485058"));
                    checkFr = true;
                }else {
                    tvFr.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tvFr.setTextColor(Color.parseColor("#A6A5A1"));
                    checkFr = false;
                }
                break;
            case R.id.tvSa:
                if (!checkSa){
                    tvSa.setBackgroundColor(Color.parseColor("#A6A5A1"));
                    tvSa.setTextColor(Color.parseColor("#485058"));
                    checkSa = true;
                }else {
                    tvSa.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tvSa.setTextColor(Color.parseColor("#A6A5A1"));
                    checkSa = false;
                }
                break;
            case R.id.tvSu:
                if (!checkSu){
                    tvSu.setBackgroundColor(Color.parseColor("#A6A5A1"));
                    tvSu.setTextColor(Color.parseColor("#485058"));
                    checkSu = true;
                }else {
                    tvSu.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tvSu.setTextColor(Color.parseColor("#A6A5A1"));
                    checkSu = false;
                }
                break;
        }
    }

    public void showDialogTime(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
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
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void initDialogSong() {
        dialogSong = new Dialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialogSong.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSong.setCancelable(false);
        dialogSong.setContentView(R.layout.dialog_choose_alarm_song);

        ImageView imBack = dialogSong.findViewById(R.id.imDimissDialog);
        TextView tvAnotherSong = dialogSong.findViewById(R.id.tvChooseAnotherSong);
        TextView tvSystemSong = dialogSong.findViewById(R.id.tvSystemSong);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSong.dismiss();
            }
        });

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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.checkBoxLoop:
                if (b){
                    lnLoop.setVisibility(View.VISIBLE);
                    checkMo = false;
                    checkTu = false;
                    checkWe = false;
                    checkTh = false;
                    checkFr = false;
                    checkSa = false;
                    checkSu = false;
                    resetChooseLoop();
                }else {
                    lnLoop.setVisibility(View.GONE);
                    checkMo = false;
                    checkTu = false;
                    checkWe = false;
                    checkTh = false;
                    checkFr = false;
                    checkSa = false;
                    checkSu = false;
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
        tvMo.setBackgroundColor(Color.parseColor("#00ffffff"));
        tvMo.setTextColor(Color.parseColor("#A6A5A1"));
        tvTu.setBackgroundColor(Color.parseColor("#00ffffff"));
        tvTu.setTextColor(Color.parseColor("#A6A5A1"));
        tvWe.setBackgroundColor(Color.parseColor("#00ffffff"));
        tvWe.setTextColor(Color.parseColor("#A6A5A1"));
        tvTh.setBackgroundColor(Color.parseColor("#00ffffff"));
        tvTh.setTextColor(Color.parseColor("#A6A5A1"));
        tvFr.setBackgroundColor(Color.parseColor("#00ffffff"));
        tvFr.setTextColor(Color.parseColor("#A6A5A1"));
        tvSa.setBackgroundColor(Color.parseColor("#00ffffff"));
        tvSa.setTextColor(Color.parseColor("#A6A5A1"));
        tvSu.setBackgroundColor(Color.parseColor("#00ffffff"));
        tvSu.setTextColor(Color.parseColor("#A6A5A1"));
    }
}
