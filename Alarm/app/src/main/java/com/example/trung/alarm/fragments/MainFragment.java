package com.example.trung.alarm.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trung.alarm.MainActivity;
import com.example.trung.alarm.R;
import com.example.trung.alarm.adapter.ListViewAlarmAdapter;
import com.example.trung.alarm.model.ItemAlarm;

import java.util.ArrayList;

public class MainFragment extends Fragment{

    private ListView lvAlarm;
    private ListViewAlarmAdapter adapter;
    private ArrayList<ItemAlarm> arrAlarm;
    private MainActivity mainActivity;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findID();
        initViews();
        loadData();


    }

    private void findID() {
        toolbar = getActivity().findViewById(R.id.toolBarMain);
        lvAlarm = getActivity().findViewById(R.id.lvAlarm);

    }

    @SuppressLint("RestrictedApi")
    private void initViews() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mainActivity = (MainActivity) getActivity();

        lvAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(mainActivity, "check", Toast.LENGTH_SHORT).show();
                mainActivity.setCurrentAlarm(arrAlarm.get(i));
                mainActivity.addFrawment(mainActivity.getNewSettingFragment());
            }
        });
    }

    private void loadData() {
        arrAlarm = mainActivity.getDatabase().getData();
        adapter = new ListViewAlarmAdapter(getContext(),R.layout.item_alarm,arrAlarm);
        lvAlarm.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
