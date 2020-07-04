package com.example.fame;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
//데이터베이스 레코드 수 반환하여 서비스 중지시킬지 아닐지 코드 넣기
public class SlideReceiver extends BroadcastReceiver {

    private TelephonyManager telephonyManager = null;
    private boolean isPhoneIdle = true;
    private KeyguardManager km = null;

    int id;
    String category;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (km == null)
                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            if (telephonyManager == null) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
            if (isPhoneIdle) {
                Log.d("","화면 꺼짐");
                if(category.equals("기본")){
                    intent = new Intent(context, SlideBasicActivity.class);
                }else if(category.equals("입력하기")){
                    intent = new Intent(context, SlideInputActivity.class);
                }
                intent.putExtra("id",id);
                //intent.putExtra("category",category);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }
    }

    public void setId(int id){
        this.id=id;
    }
    public void setCategory(String category){
        this.category=category;
    }
    private PhoneStateListener phoneListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber){
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE :
                    isPhoneIdle = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING :
                    isPhoneIdle = false;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK :
                    isPhoneIdle = false;
                    break;
            }
        }//전화상태 체크
    };
}
