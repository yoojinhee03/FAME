package com.example.fame;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.RequiresApi;

public class SlideService extends Service {
    private SlideReceiver mReceiver=null;
    int id;
    String category;
    private String ANDROID_CHANNER_ID="com.example.fame.slide";
    Handler handler = new Handler();
    int cnt=0;
    public SlideService() {
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("","onStartCommandsd");

        if (intent != null) {
            if (intent.getAction() == null) {
                if (mReceiver == null) {
                    Log.e("","onStartCommand");
                    id=intent.getIntExtra("id",-1);
                    category=intent.getStringExtra("category");
                    //.makeText(this, "StartService"+id, Toast.LENGTH_SHORT).show();
                    mReceiver = new SlideReceiver();
                    mReceiver.setId(id);
                    mReceiver.setCategory(category);
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(mReceiver, filter);
                }
            }
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(ANDROID_CHANNER_ID,"MyService", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            new Notification.Builder(this,ANDROID_CHANNER_ID);
            Notification.Builder builder=new Notification.Builder(this,ANDROID_CHANNER_ID)
                    .setContentTitle("타이틀")
                    .setContentText("text");
            Notification notification=builder.build();
            startForeground(1,notification);
        }else startForeground(1,new Notification());
       // registerRestartAlarm(true);
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "stopservice", Toast.LENGTH_SHORT).show();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
           // registerRestartAlarm(false);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public void registerRestartAlarm(boolean isOn){
//        //Toast.makeText(this, ""+isOn, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(SlideService.this, RestartReceiver.class);
//        intent.putExtra("id",id);
//        intent.setAction(RestartReceiver.ACTION_RESTART_SERVICE);
//        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//
//        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//        if(isOn){
//            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 10000, sender);
//        }else{
//            am.cancel(sender);
//        }
//    }
}
