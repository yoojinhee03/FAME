package com.example.fame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    Context context;
    int id;
    int cnt;

    DBHelper dbHelper;
    String sql;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("","onReceive");
        int iarr[];
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e("","ACTION_BOOT_COMPLETED");
            Intent mintent;
            if(getslidecnt()>0){
                iarr=new int[getslidecnt()];
                mintent = new Intent(context, SlideService.class);
                for(int i=0;i<getslidecnt(); i++){
                    getslideId(i,iarr);
                    mintent.putExtra("id",iarr[i]);
                }
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    context.startForegroundService(mintent);
                }else context.startService(mintent);
            }else{
                mintent = new Intent(context, SlideService.class);
                context.stopService(mintent);
            }
        }
    }
    public void getslideDB(){
        dbHelper=DBHelper.getInstance(context);
        sql ="SELECT * FROM SlideCategory";
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(sql,null);
    }
    public int getslidecnt(){
        getslideDB();
        cnt=cursor.getCount();

        while(cursor.moveToNext()) {
        }
        return cnt;
    }
    public void getslideId(int i,int iarr[]){
        cursor.moveToPosition(i);
        iarr[i]=Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
        Log.e("",iarr[i]+"");
    }
}
