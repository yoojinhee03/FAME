package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SliderepeatSetActivity extends AppCompatActivity {

    TextView cnt;
    Button finishButton;
    ImageButton upButton;
    ImageButton downButton;
    int count=5;
    static String result = "";
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliderepeat_set);

        finishButton=findViewById(R.id.finishButton);
        upButton=(ImageButton)findViewById(R.id.upButton);
        downButton=(ImageButton)findViewById(R.id.downButton);
        cnt=(TextView)findViewById(R.id.cnt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<360) {
                    count+=5;
                    cnt.setText("" + count);
                }
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>0) {
                    count-=5;
                    cnt.setText("" + count);
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("슬라이드반복",count);
                setResult(Activity.RESULT_OK, intent);
                result="반복설정완료";
                finish();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작

                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //뒤로가기
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(result.equals("반복설정완료")){
            result="반복설정완료";
            restoreState();
        }
    }
    protected void saveState(){
        SharedPreferences pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("data",cnt.getText().toString());
        editor.commit();
    }//상태 저장

    private void restoreState() {
        SharedPreferences pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);
        if((pref!=null)&&(pref.contains("data"))){
            String data=pref.getString("data","");
            cnt.setText(data);
            count=Integer.parseInt(cnt.getText().toString());
        }
    }//재개될 때 데이터를 다시 불러오는 메서드

    public void clearMyPrefs(){
        SharedPreferences pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.clear();
        editor.commit();
    }
}
