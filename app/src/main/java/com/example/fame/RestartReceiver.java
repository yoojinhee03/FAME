package com.example.fame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RestartReceiver extends BroadcastReceiver {

    static public final String ACTION_RESTART_SERVICE = "RestartReceiver.restart";    // 값은 맘대로

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_RESTART_SERVICE)){
//            Toast.makeText(context, "restart", Toast.LENGTH_SHORT).show();
            int id=intent.getIntExtra("id",-1);
            Intent i = new Intent(context, SlideService.class);
            i.putExtra("id",id);
            context.startService(i);
        }
    }
}
