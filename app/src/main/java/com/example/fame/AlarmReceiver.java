package com.example.fame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    SQLiteDatabase db;
    Cursor cursor;
    DBHelper dbHelper;
    String dayindex;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        final int id = intent.getIntExtra("id",-1);
        final String dayindex = intent.getStringExtra("dayindex");
        Log.e("AlarmReceiver",id+"");
        //WritingInfo wi = new WritingInfo();

        final String[] week = {"일", "월", "화", "수", "목", "금", "토"};
        Calendar calendar = Calendar.getInstance();
        String todayDayOfWeek = week[calendar.get(Calendar.DAY_OF_WEEK)-1];
        String[] alarmDayOfWeek = dayindex.split("/");
        //int useOrNot = alarmInfo.get(0).getUseOrNot();

        boolean weekCheck =false;

        for(int i =0; i<alarmDayOfWeek.length;i++){
            Log.e("boolean",alarmDayOfWeek[i]+"");
            if(alarmDayOfWeek[i].equals(todayDayOfWeek)){
                weekCheck=true;
                break;
            }
        }
        Log.e("boolean",weekCheck+"");
        Log.e("boolean",weekCheck+"");

        //위에서 오늘의 요일과 저장해놓은 요일과 비교를 한 다음 오늘이 알람이 울려야 할 요일이면 Activity를 호출한다.
        if(!weekCheck) {
            return;
        }else if(weekCheck){
            Intent sIntent=new Intent(context,AlarmService.class);
            sIntent.putExtra("category",intent.getStringExtra("category"));
            sIntent.putExtra("id",id);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                context.startForegroundService(sIntent);
            }
            else{
                context.startService(sIntent);
            }
        }
    }
}
