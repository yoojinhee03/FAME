package com.example.fame;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class AlarmService extends Service {

    private String ANDROID_CHANNER_ID="com.example.fame.alarm";

    @Nullable
    public IBinder onBind(Intent intent){
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startid){
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            String channelId=createNotificationChannel();
//
//            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, channelId);
//            Notification notification=builder.setOngoing(true)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .build();
//
//            startForeground(1,notification);
//        }

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
        Log.e("AlarmService",intent.getIntExtra("id",-1)+"");

        //알림창 호출
        if(intent.getStringExtra("category").equals("입력하기")){
            Intent intent1=new Intent(this, AlarmInputAcitivity.class);
            intent1.putExtra("id",intent.getIntExtra("id",-1));
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);

        } else if(intent.getStringExtra("category").equals("기본")){
            Intent intent1=new Intent(this, AlarmBasicActivity.class);
            intent1.putExtra("id",intent.getIntExtra("id",-1));
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        }

        Log.d("AlarmService","Alarm");

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            stopForeground(true);
        }
        stopSelf();

        return START_REDELIVER_INTENT;
    }

    public void intentActivity(Intent intent){
        intent.putExtra("id",intent.getIntExtra("id",-1));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(){
        String channelId="Alarm";
        String channelName=getString(R.string.app_name);
        NotificationChannel channel=new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setSound(null,null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }
}
