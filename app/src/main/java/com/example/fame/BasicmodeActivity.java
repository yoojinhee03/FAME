package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasicmodeActivity extends AppCompatActivity {

    TextView levelText;
    ImageButton prevButton;
    ImageButton nextButton;

    int cnt;
    String level;
    int startnum;
    int endnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicmode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        levelText=(TextView) findViewById((R.id.levelText));
        levelText.setText("초급");
        prevButton=(ImageButton) findViewById(R.id.prevButton);
        nextButton=(ImageButton) findViewById(R.id.nextButton);

        findViewById(R.id.chapter1).setOnClickListener(onClickListener);
        findViewById(R.id.chapter2).setOnClickListener(onClickListener);
        findViewById(R.id.chapter3).setOnClickListener(onClickListener);
        findViewById(R.id.chapter4).setOnClickListener(onClickListener);

        cnt=0;
        setLevel("BeginningLevel");
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt>0) cnt--;
                changeLevel();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt<2) cnt++;
                changeLevel();
            }
        });
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.chapter1){
                startnum=1;
                endnum=50;
            }
            if(v.getId()==R.id.chapter2){
                startnum=51;
                endnum=100;
            }
            if(v.getId()==R.id.chapter3){
                startnum=101;
                endnum=150;
            }
            if(v.getId()==R.id.chapter4){
                startnum=151;
                endnum=200;
            }

            Intent intent = new Intent(getApplicationContext(), BasicWordActivity.class);
            intent.putExtra("startnum",startnum);
            intent.putExtra("endnum",endnum);
            intent.putExtra("level",getLevel());
            intent.putExtra("levelText",levelText.getText().toString());
            startActivity(intent);
        }
    };

    public void changeLevel(){
        Log.e("",cnt+"");
        switch (cnt){
            case 0:
                levelText.setText("초급");
                setLevel("BeginningLevel");
                break;
            case 1:
                levelText.setText("중급");
                setLevel("intermediateLevel");
                break;
            case 2:
                levelText.setText("고급");
                setLevel("highLevel");
                break;
        }
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(BasicmodeActivity.this, SelModeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//이전 엑티비티 다 죽이기
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//뒤로가기
}