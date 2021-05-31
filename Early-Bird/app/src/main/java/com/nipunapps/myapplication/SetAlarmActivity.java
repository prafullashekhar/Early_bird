package com.nipunapps.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nipunapps.myapplication.Models.AlarmModel;
import com.nipunapps.myapplication.Models.TimeModel;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import java.util.ArrayList;

public class SetAlarmActivity extends AppCompatActivity {

    Boolean sun = false, mon = false, tue = false, wed = false, thu = false, fri = false, sat = false;
    ArrayList<String> days = new ArrayList<>();
    Button saveAlarm;
    Boolean isHour=true,isMinute=false;
    int sno;
    Croller croller;
    TimeModel timeModel=new TimeModel();
    ProgressBar progressBar;
    TextView hour,minute,meredian;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        getSupportActionBar().hide();
        initView();
        initialiseCroller();
        sno = getIntent().getIntExtra("sno", 0);
        DbHandler handler = new DbHandler(this);
        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                if (isHour) {
                    Double d = (progress) * 4.17;
                    progressBar.setProgress(d.intValue());
                    if ((progress - 1) < 12) {
                        meredian.setText(R.string.am);
                    } else {
                        meredian.setText(R.string.pm);
                    }
                    sethour(progress - 1);
                }
                else if(isMinute){
                    Double d=(progress)*1.67;
                    progressBar.setProgress(d.intValue());
                    setMinute(progress-1);
                }
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStopTrackingTouch(Croller croller) {
                if(isHour){
                    int hourProgress=(croller.getProgress()-1);
                    isHour=false;
                    isMinute=true;
                    timeModel.setHour(hourProgress);
                    if(hourProgress<12){
                        timeModel.setMeredian(String.valueOf(R.string.am));
                    }
                    else {
                        timeModel.setMeredian(String.valueOf(R.string.pm));
                    }
                    initialiseCroller();
                }
                else if(isMinute){
                    timeModel.setMinute((croller.getProgress()-1));
                }

            }
        });
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                AlarmModel model = new AlarmModel();
                model.setSno(sno);
                model.setHour(timeModel.getHour());
                model.setMinute(timeModel.getMinute());
                String day = getDays();
                if(day.equals("")){
                    Toast.makeText(SetAlarmActivity.this, "please select at least on day", Toast.LENGTH_SHORT).show();
                }
                else {
                    model.setDays(day);
                    handler.updateAlarm(model);
                    startActivity(new Intent(SetAlarmActivity.this, MainActivity.class));
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialiseCroller(){
        if(isHour) {
            hour.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.day_pick_off));
            minute.setBackgroundResource(0);
            if(timeModel.getHour()!=0){
                croller.setProgress((timeModel.getHour()+1));
            }
            else {
                croller.setProgress(1);
            }
            croller.setMax(24);
            croller.setEnabled(true);
        }
        else if(isMinute){
            minute.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.day_pick_off));
            hour.setBackgroundResource(0);
            if(timeModel.getMinute()!=0){
                croller.setProgress((timeModel.getMinute()+1));
            }
            else {
                croller.setProgress(1);
            }
            croller.setMax(60);
            croller.setEnabled(true);
        }
    }
    private void sethour(int _hour){
        if(_hour>12){
            _hour=_hour-12;
        }
        if(_hour==0){
            hour.setText("12");
        }
        else if(_hour>0 &&_hour<10){
            hour.setText("0"+_hour);
        }
        else {
            hour.setText(""+_hour);
        }
    }
    private void setMinute(int __minute){
        if(__minute<10){
            minute.setText("0"+__minute);
        }
        else {
            minute.setText(""+__minute);
        }
    }
    private void initView(){
        saveAlarm = findViewById(R.id.saveAlarm);
        hour=findViewById(R.id.alarmEditHour);
        minute=findViewById(R.id.alarmEditMinute);
        progressBar=findViewById(R.id.progressBar);
        croller=findViewById(R.id.croller);
        meredian=findViewById(R.id.alarmEditMeredian);
    }

    private String getDays() {
        String day = "";
        if (sun) {
            days.add("sun");
        }
        if (mon) {
            days.add("mon");
        }
        if (tue) {
            days.add("tue");
        }
        if (wed) {
            days.add("wed");
        }
        if (thu) {
            days.add("thu");
        }
        if (fri) {
            days.add("fri");
        }
        if (sat) {
            days.add("sat");
        }
        for (int i = 0; i < days.size(); i++) {
            day = day + days.get(i) + " ";
        }
        return day;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void selectHour(View view){
        isHour=true;
        hour.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.day_pick_off));
        isMinute=false;
        initialiseCroller();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void selectMinute(View view){
        isMinute=true;
        minute.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.day_pick_off));
        isHour=false;
        initialiseCroller();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pickDay(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sunday:
                if (sun) {
                    sun = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                } else {
                    sun = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                }
                break;
            case R.id.monday:
                if (mon) {
                    mon = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                } else {
                    mon = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                }
                break;
            case R.id.tuesday:
                if (tue) {
                    tue = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                } else {
                    tue = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                }
                break;
            case R.id.wednesday:
                if (wed) {
                    wed = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                } else {
                    wed = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                }
                break;
            case R.id.thursday:
                if (thu) {
                    thu = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                } else {
                    thu = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                }
                break;
            case R.id.friday:
                if (fri) {
                    fri = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                } else {
                    fri = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                }
                break;
            case R.id.saturday:
                if (sat) {
                    sat = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                } else {
                    sat = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                }
                break;
        }
    }
}
