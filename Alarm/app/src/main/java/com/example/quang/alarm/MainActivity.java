package com.example.quang.alarm;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.example.quang.alarm.adapter.MyRecyclerViewAdapter;
import com.example.quang.alarm.model.ItemAlarm;
import com.example.quang.alarm.services.AppService;
import com.example.quang.alarm.utils.DatabaseUtil;
import com.example.quang.alarm.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyRecyclerViewAdapter.ItemClickListener {

    private ListView lvAlarm;
   // private ListViewAlarmAdapter adapter;
    private ArrayList<ItemAlarm> arrAlarm;
    private DatabaseUtil databaseUtil;
    private FloatingActionButton btnAddAlarm;


    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerAlarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Utils.checkPermission(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(this,PermissionActivity.class));
            finish();
            return;
        }


        if (isMyServiceRunning(AppService.class)){

        }
        else {
            Intent intent = new Intent(this, AppService.class);
            startService(intent);
        }


        findID();
        loadData();
        initViews();
    }

    private void findID() {
        //lvAlarm = findViewById(R.id.lvAlarm);
        btnAddAlarm = findViewById(R.id.btnAddAlarm);

        recyclerAlarm = findViewById(R.id.lvAlarm);
    }

    private void initViews() {
        btnAddAlarm.setOnClickListener(this);
        //lvAlarm.setOnItemClickListener(this);


    }

    private void loadData() {
        databaseUtil = new DatabaseUtil(this);
        arrAlarm = databaseUtil.getData();
//        adapter = new ListViewAlarmAdapter(this,R.layout.item_alarm,arrAlarm);
//        lvAlarm.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerAlarm.getContext(),
//                LinearLayoutManager.VERTICAL);
//        recyclerAlarm.addItemDecoration(dividerItemDecoration);
        recyclerAlarm.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new MyRecyclerViewAdapter(this, arrAlarm);
        adapter.setClickListener(this);
        recyclerAlarm.setAdapter(adapter);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddAlarm:
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                intent.putExtra("setting","null");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onItemClick(View view, int position) {
        ItemAlarm itemAlarm = arrAlarm.get(position);

        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
        intent.putExtra("setting","item");
        intent.putExtra("alarm",itemAlarm);
        startActivity(intent);
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
