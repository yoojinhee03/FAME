package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class QuizmodeActivity extends AppCompatActivity {

    private Button quiz0Button;
    private Button quiz1Button;
    private Button quiz2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        quiz0Button=findViewById(R.id.quiz0Button);
        quiz1Button=findViewById(R.id.quiz1Button);
        quiz2Button=findViewById(R.id.quiz2Button);

        quiz0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuizmodeActivity.this,Quiz_inputActivity.class);
                startActivity(intent);
            }
        });
        quiz1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuizmodeActivity.this,Quiz_selectActivity.class);
                startActivity(intent);
            }
        });
        quiz2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuizmodeActivity.this,Quiz_hardActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //뒤로가기
}
