package com.example.fame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MissionActivity extends AppCompatActivity {

    ImageButton inputButton;
    ImageButton basicButton;
    static int result=Activity.RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        inputButton=(ImageButton) findViewById(R.id.inputButton);
        basicButton=(ImageButton) findViewById(R.id.basicButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),InputSetActivity.class);
                startActivityForResult(intent,101);
            }
        });
        basicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("category", "기본");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Intent intent = new Intent();
            int count = data.getIntExtra("inputcount", -1);
            String category = data.getStringExtra("category");
            intent.putExtra("category", category);
            intent.putExtra("inputcount", count);
            if(resultCode== Activity.RESULT_OK) {//input
                setResult(Activity.RESULT_OK, intent);
                result=Activity.RESULT_OK;
                finish();
            }else if(resultCode==Activity.RESULT_CANCELED){//기본
                result=Activity.RESULT_CANCELED;
            }else if(resultCode==Activity.RESULT_FIRST_USER){
                result=Activity.RESULT_FIRST_USER;
            }
//            전페이지에서 보낸 값을 받아오는 메서드
        }
    }
//    응답을 전달 받음
//    A에서 B로 갔다가 다시 A로 넘어올 때 사용하는, 안드로이드에서 제공하는 기본 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent=new Intent();
                if(result==Activity.RESULT_CANCELED) {
                    intent.putExtra("category", "기본");
                    setResult(Activity.RESULT_CANCELED, intent);

                }else if(result==Activity.RESULT_OK||result==Activity.RESULT_FIRST_USER){
                    intent.putExtra("category", "입력하기");
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //뒤로가기
}

