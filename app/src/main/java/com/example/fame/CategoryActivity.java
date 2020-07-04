package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class CategoryActivity extends AppCompatActivity {

    ImageButton alarmButton;
    ImageButton slideButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        alarmButton=(ImageButton)findViewById(R.id.alarmButton);
        slideButton=(ImageButton)findViewById(R.id.slideButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),WordSetActivity.class);
                intent.putExtra("category","alarm");
                startActivity(intent);
            }
        });
        slideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),WordSetActivity.class);
                intent.putExtra("category","slide");
                startActivity(intent);
            }
        });
    }
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
