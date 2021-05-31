package com.nipunapps.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.nipunapps.myapplication.DbHandler;
import com.nipunapps.myapplication.Models.AlarmModel;
import com.nipunapps.myapplication.R;
import com.nipunapps.myapplication.SetAlarmActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.viewHolder> {
    Context context;
    ArrayList<AlarmModel> list;

    public AlarmAdapter(Context context, ArrayList<AlarmModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_show_active_alarm,parent,false);
        return new viewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

//        getting individual alarm from the database
        String strHour,strMinute;
        Boolean alarmon=false;
        AlarmModel model=list.get(position);
        if(model.getAlarmState()==1){
            alarmon=true;
        }
        else {
            alarmon=false;
        }
        if(alarmon){
            showActiveAlarm(holder);
        }
        else {
            showNonActiveAlarm(holder);
        }
        String[] days=model.getDays().split(" ");
        if(days.length==7 || model.getDays().equals("EVERYDAY")){
            holder.daysCount.setText(R.string.everyday);
        }
        else if(days.length==1){
            holder.daysCount.setText(days[0].toUpperCase());
        }
        else {
            String showDay=days[0].toUpperCase()+" to "+days[days.length-1].toUpperCase();
            holder.daysCount.setText(showDay);
        }
        int hour=model.getHour();
        if(hour<12){
            hour=hour;
            holder.timeAmPm.setText("AM");
        }
        else if(hour==12) {
            hour=hour;
            holder.timeAmPm.setText("PM");
        }
        else {
            hour=hour-12;
            holder.timeAmPm.setText("PM");
        }
        if(hour<10){
            strHour="0"+String.valueOf(hour);
        }
        else {
            strHour=String.valueOf(hour);
        }

        if(model.getMinute()<10){
            strMinute="0"+String.valueOf(model.getMinute());
        }
        else{
            strMinute=String.valueOf(model.getMinute());
        }
        holder.showTime.setText(strHour+":"+strMinute);

        holder.editAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SetAlarmActivity.class);
                intent.putExtra("sno",model.getSno());
                context.startActivity(intent);
            }
        });

        holder.alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmModel updateModelOff=new AlarmModel(model.getHour(),model.getMinute(),model.getDays(),0);
                updateModelOff.setSno(model.getSno());
                DbHandler handler=new DbHandler(context);
                handler.updateAlarm(updateModelOff);
                showNonActiveAlarm(holder);
            }
        });
        holder.alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmModel updateModelOn=new AlarmModel(model.getHour(),model.getMinute(),model.getDays(),1);
                updateModelOn.setSno(model.getSno());
                DbHandler handler=new DbHandler(context);
                handler.updateAlarm(updateModelOn);
                showActiveAlarm(holder);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showActiveAlarm(viewHolder holder){
        holder.editAlarm.setBackground(context.getDrawable(R.drawable.active_alarm_bg));
        holder.alarmOn.setVisibility(View.VISIBLE);
        holder.alarmOn.setClickable(true);
        holder.alarmOff.setVisibility(View.INVISIBLE);
        holder.alarmOff.setClickable(false);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showNonActiveAlarm(viewHolder holder){
        holder.editAlarm.setBackground(context.getDrawable(R.drawable.non_active_alarm_bg));
        holder.alarmOff.setVisibility(View.VISIBLE);
        holder.alarmOff.setClickable(true);
        holder.alarmOn.setVisibility(View.INVISIBLE);
        holder.alarmOn.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        FrameLayout editAlarm;
        TextView daysCount,timeAmPm,showTime;
        Button alarmOn,alarmOff;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            editAlarm=itemView.findViewById(R.id.editAlarm);
            daysCount=itemView.findViewById(R.id.daysCount);
            timeAmPm=itemView.findViewById(R.id.timeAmPm);
            showTime=itemView.findViewById(R.id.showTime);
            alarmOn=itemView.findViewById(R.id.alarmOn);
            alarmOff=itemView.findViewById(R.id.alarmOff);
        }
    }
}
