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
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.SharedPreferences.*;

public class RepeatSetActivity extends AppCompatActivity {

    Button finishButton;
    CheckBox[] day;
    final int[] index=new int[7];
    static String result = "";
    public static Context mContext;

//    private CompoundButton.OnCheckedChangeListener basic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmrepeat_set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
        mContext=this;

        Toast.makeText(RepeatSetActivity.this, result, Toast.LENGTH_SHORT).show();

        finishButton = findViewById(R.id.nextButton);
        day = new CheckBox[]
                {
                        (CheckBox)findViewById(R.id.SunCheck),
                        (CheckBox)findViewById(R.id.monCheck),
                        (CheckBox)findViewById(R.id.TuesCheck),
                        (CheckBox)findViewById(R.id.WedsCheck),
                        (CheckBox)findViewById(R.id.ThursCheck),
                        (CheckBox)findViewById(R.id.FriCheck),
                        (CheckBox)findViewById(R.id.SaturCheck),
                };
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayChecked(day,index);
                Intent intent = new Intent();
                intent.putExtra("dayindex",index);
                setResult(Activity.RESULT_OK, intent);
                result="완료";
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //뒤로가기

    public void dayChecked(CheckBox[] day, final int[] index) {

        for(int i=0;i<day.length;i++){
            if(day[i].isChecked()==true){
                index[i]=1;
            }else{
                index[i]=0;
            }
        }
    }//요일 체크한거 배열로 넘기기

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(result.equals("완료")){

            result="완료";
            restoreState();
        }
    }
    protected void saveState(){
        SharedPreferences pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);
        ArrayList<Integer> list = new ArrayList<>();
        Editor editor=pref.edit();
//        editor.putString("data",cnt.getText().toString());

        editor.putBoolean("Sun",day[0].isChecked());
        editor.putBoolean("Mon",day[1].isChecked());
        editor.putBoolean("Tue",day[2].isChecked());
        editor.putBoolean("Wed",day[3].isChecked());
        editor.putBoolean("Thu",day[4].isChecked());
        editor.putBoolean("Fri",day[5].isChecked());
        editor.putBoolean("Sat",day[6].isChecked());

        editor.commit();

    }//상태 저장

    private void restoreState() {
        SharedPreferences pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);
        if((pref!=null)&&(pref.contains("Sun"))&&(pref.contains("Mon"))&&(pref.contains("Tue"))&&(pref.contains("Wed"))&&(pref.contains("Thu"))&&(pref.contains("Fri"))&&(pref.contains("Sat"))){
//            String data=pref.getString("data","");
            Boolean[][] data ={
                    {pref.getBoolean("Sun",false)},
                    {pref.getBoolean("Mon",false)},
                    {pref.getBoolean("Tue",false)},
                    {pref.getBoolean("Wed",false)},
                    {pref.getBoolean("Thu",false)},
                    {pref.getBoolean("Fri",false)},
                    {pref.getBoolean("Sat",false)},
            };
//

            for(int i=0;i<index.length;i++){
                if(data[i][0]==true){
                    day[i].setChecked(true);
                }
            }
        }
    }//재개될 때 데이터를 다시 불러오는 메서드

    public void clearMyPrefs(){
        SharedPreferences pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);
        Editor editor=pref.edit();
        editor.clear();
        editor.commit();
    }
}
