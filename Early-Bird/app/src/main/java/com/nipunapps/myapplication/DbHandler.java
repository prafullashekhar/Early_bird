package com.nipunapps.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nipunapps.myapplication.Models.AlarmModel;

public class DbHandler extends SQLiteOpenHelper {
    private static final String dbName="alarmDb";
    private static final int version=1;

    public DbHandler(Context context){
        super(context,dbName,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

//        Creatting the alarm table in sql
        String sql="CREATE TABLE ALARM (sno INTEGER PRIMARY KEY AUTOINCREMENT, HOUR INTEGER, MINUTE INTEGER, DAYS TEXT,ALARMSTATE INTEGER)";
        db.execSQL(sql);
    }
    public void addAlarm(AlarmModel alarm){

//        Insert the alarm data in sql database
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("HOUR",alarm.getHour());
        values.put("MINUTE",alarm.getMinute());
        values.put("DAYS",alarm.getDays());
        values.put("ALARMSTATE",alarm.getAlarmState());
        long k=database.insert("ALARM",null,values);
        Log.d("MyTag",Long.toString(k));
        database.close();
    }
    public AlarmModel getAlarm(int sno){

//        get the alarm data from database
        SQLiteDatabase database=this.getReadableDatabase();
        AlarmModel alarmModel=new AlarmModel();
        Cursor cursor=database.query("ALARM",new String[]{"sno,HOUR,MINUTE,DAYS,ALARMSTATE"},"sno=?",new String[]{String.valueOf(sno)},null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            alarmModel.setSno(cursor.getInt(0));
            alarmModel.setHour(cursor.getInt(1));
            alarmModel.setMinute(cursor.getInt(2));
            alarmModel.setDays(cursor.getString(3));
            alarmModel.setAlarmState(cursor.getInt(4));
        }
        return alarmModel;
    }

    public void updateAlarm(AlarmModel alarmModel){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("HOUR",alarmModel.getHour());
        values.put("MINUTE",alarmModel.getMinute());
        values.put("DAYS",alarmModel.getDays());
        values.put("ALARMSTATE",alarmModel.getAlarmState());
        database.update("ALARM",values,"sno=?",new String[]{String.valueOf(alarmModel.getSno())});
    }
//    public void updateAlarmState(AlarmModel alarmModel){
//        SQLiteDatabase database=this.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put("ALARMSTATE",alarmModel.getAlarmState());
//        database.update("ALARM",values,"sno=?",new String[]{String.valueOf(alarmModel.getSno())});
//    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
