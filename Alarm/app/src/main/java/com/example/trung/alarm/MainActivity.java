package com.example.trung.alarm;

import android.support.design.widget.FloatingActionButton;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.example.trung.alarm.fragments.MainFragment;
import com.example.trung.alarm.fragments.SettingFragment;
import com.example.trung.alarm.model.ItemAlarm;
import com.example.trung.alarm.utils.DatabaseUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MainFragment mainFragment = new MainFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private FloatingActionButton btnAdd;

    private DatabaseUtils databaseUtils;

    private ItemAlarm currentAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseUtils = new DatabaseUtils(this);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        addFrawment(mainFragment);
    }

    public void addFrawment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.RIGHT));
        fragment.setExitTransition(new Slide(Gravity.LEFT));
        fragmentTransaction.add(R.id.viewGroup,fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        //removeFragment(mainFragment);
        addFrawment(new SettingFragment());
        btnAdd.setVisibility(View.INVISIBLE);
    }

    public DatabaseUtils getDatabase(){
        return this.databaseUtils;
    }

    public void removeFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
    }

    public void showBtnAdd(){
        btnAdd.setVisibility(View.VISIBLE);
    }

    public MainFragment getMainFragment() {
        return mainFragment;
    }

    public MainFragment getNewMainFragment() {
        return new MainFragment();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.e("Tag",count+"");
        if (count == 1) {
            finish();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    public ItemAlarm getCurrentAlarm() {
        return currentAlarm;
    }

    public SettingFragment getNewSettingFragment() {
        return new SettingFragment();
    }

    public void setCurrentAlarm(ItemAlarm currentAlarm) {
        this.currentAlarm = currentAlarm;
    }


}
