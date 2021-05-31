package com.nipunapps.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nipunapps.myapplication.Adapters.AlarmAdapter;
import com.nipunapps.myapplication.Models.AlarmModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    Variable declaration from activity main file
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<AlarmModel> list=new ArrayList<>();
    Toolbar toolbar;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Variable initialisation
        imageView = findViewById(R.id.addMoreAlarm);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolBarDash);
        recyclerView=findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        AlarmAdapter adapter=new AlarmAdapter(this,list);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AlarmModel model=new AlarmModel();
        DbHandler handler=new DbHandler(this);
        list.clear();
        int i=0;
        while (true){
            model=handler.getAlarm(i+1);
            if(model.getSno()==0){
                break;
            }
            else {
                list.add(model);
                i++;
            }
        }
        adapter.notifyDataSetChanged();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmModel alarmModel=new AlarmModel(6,30,"EVERYDAY",1);
                DbHandler handler=new DbHandler(MainActivity.this);
                handler.addAlarm(alarmModel);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}