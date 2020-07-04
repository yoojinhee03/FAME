package com.example.fame;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

public class InputSetActivity extends AppCompatActivity {

    TextView cnt;
    ImageButton upButton;
    ImageButton downButton;
    Button finishButton;
    int count=3;
    static String result = "";
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_set_actvity);
        Toast.makeText(this, "oncreate", Toast.LENGTH_SHORT).show();
        cnt=(TextView)findViewById(R.id.cnt);
        upButton=findViewById(R.id.upButton);
        downButton=findViewById(R.id.downButton);
        finishButton=findViewById(R.id.nextButton);
        mContext = this;
        count=Integer.parseInt(cnt.getText().toString());

        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<10) {
                    count++;
                    cnt.setText("" + count);
                }
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1) {
                    count--;
                    cnt.setText("" + count);
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                result="입력하기";
                intent.putExtra("category", "입력하기");
                intent.putExtra("inputcount",count);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작

                if(result.equals("입력하기")) {
                    Intent intent=new Intent();
                    intent.putExtra("category", result);
                    setResult(Activity.RESULT_FIRST_USER, intent);
                }else{
                    Intent intent=new Intent();
                    intent.putExtra("category", "기본");
                    setResult(Activity.RESULT_CANCELED, intent);
                }
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //뒤로가기

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String data=savedInstanceState.getString("data");
        cnt.setText(data);
    }//화면을 돌려도 값을 변하지 X

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Toast.makeText(this, "onSaveInstanceState", Toast.LENGTH_SHORT).show();
        String data = cnt.getText().toString();
        outState.putString("data",data);

    }//화면을 돌려도 값을 변하지 X

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(result.equals("입력하기")){
            result="입력하기";
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
