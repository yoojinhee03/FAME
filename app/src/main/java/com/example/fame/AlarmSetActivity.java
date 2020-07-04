package com.example.fame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmSetActivity extends AppCompatActivity{

    TimePicker TimePicker;
    String hour;
    String minute;
    String category;//미션
    int inputcount;//몇번 입력하는지
    int []index;//요일
    String dayindex;
    int wordcount;
    Button missionButton;
    Button repeatButton;
    ImageButton nextButton;
    boolean Alarmrepeatresult;//알람 반복 설정을 하였는지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        Intent intent=getIntent();
        wordcount=intent.getIntExtra("wordcount",-1);

        TimePicker = (TimePicker) findViewById(R.id.time_picker);
        missionButton=(Button) findViewById(R.id.missionButton);
        repeatButton=(Button) findViewById(R.id.repeatButton);
        nextButton=(ImageButton) findViewById(R.id.nextButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        ((InputSetActivity)InputSetActivity.mContext).result="기본";
        category="기본";
        inputcount=-1;

        missionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MissionActivity.class);
                startActivityForResult(intent,100);
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RepeatSetActivity.class);
                startActivityForResult(intent,200);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeChanged();
            Intent intent=new Intent(getApplicationContext(),AlarmLevelSetActivity.class);
            //((InputSetActivity)InputSetActivity.mContext).clearMyPrefs();
            if (Alarmrepeatresult == false) {
                Toast.makeText(AlarmSetActivity.this, "반복 설정을 해주세요", Toast.LENGTH_SHORT).show();
            }else{
                intent.putExtra("hour",hour);
                intent.putExtra("minute",minute);
                intent.putExtra("category",category);
                intent.putExtra("dayindex",dayindex);
                intent.putExtra("inputcount",inputcount);
                intent.putExtra("wordcount",wordcount);

                startActivityForResult(intent,300);
            }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {//미션
            category = data.getStringExtra("category");
            inputcount = data.getIntExtra("inputcount", -1);
            missionButton.setText(category);
//            전페이지에서 보낸 값을 받아오는 메서드
            if(missionButton.getText().toString().equals("기본")) {
                ((InputSetActivity)InputSetActivity.mContext).result="기본";
                category="기본";
            }
            //Toast.makeText(getApplicationContext(), "메뉴화면으로부터 응답 : " + category + "," + inputcount, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 200) {//반복
            if(resultCode== Activity.RESULT_OK) {
//                int cnt=0;
                index = data.getIntArrayExtra("dayindex");
                String day[] = {"일", "월", "화", "수", "목", "금", "토"};
                indextoString(index,day);
                //Toast.makeText(getApplicationContext(), "메뉴화면으로부터 응답 : " + index[0]+""+index[1]+""+index[2]+""+index[3]+""+index[4]+""+index[5]+""+index[6], Toast.LENGTH_SHORT).show();
                DayButtonSet(index, day);
            }
            if(repeatButton.getText().toString()=="안함"){
                Alarmrepeatresult=false;
            }else{
                Alarmrepeatresult=true;
            }

//            전페이지에서 보낸 값을 받아오는 메서드
        }
    }

    public void indextoString(int index[],String day[]){
        dayindex="";
        for(int i=0;i<index.length;i++){
            if(index[i]==1)
                dayindex+=day[i]+"/";
            else{
                dayindex+="X/";
            }
        }
    }

    public void TimeChanged(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = TimePicker.getHour() + "";
            minute = TimePicker.getMinute() + "";
        } else {
            hour = TimePicker.getCurrentHour() + "";
            minute = TimePicker.getCurrentMinute() + "";
        }
    }//설정한 시간을 값에 넣어줌

    public void DayButtonSet(int[] index,String day[]){
        String text="";
        int count=0;

        for (int i = 0; i < index.length; i++) {
            if (index[i] == 1) {
                count++;
                text += day[i]+" ";
            }
        }
        if(index[0]==1&&index[6]==1&&count==2){
            text="주말";
        }
        if(index[1]==1&&index[2]==1&&index[3]==1&&index[4]==1&&index[5]==1&&count==5) {
            text = "평일";
        }
        if (count == 7) {
            text = "매일";
        }else if(count==0){
            text="안함";
        }
        repeatButton.setText(text);
    }//repeatset에서 가져온 값을 버튼 텍스트에 넣어줌

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent=new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //뒤로가기
}
