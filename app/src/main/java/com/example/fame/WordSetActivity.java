package com.example.fame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordSetActivity extends AppCompatActivity {

    TextView cnt;
    Button nextButton;
    ImageButton upButton;
    ImageButton downButton;
    int count=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_set);

        Intent getIntent=getIntent();
        final String category=getIntent.getStringExtra("category");

        nextButton=findViewById(R.id.nextButton);
        upButton=(ImageButton)findViewById(R.id.upButton);
        downButton=(ImageButton)findViewById(R.id.downButton);
        cnt=(TextView)findViewById(R.id.cnt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.equals("slide")) {
                    Intent intent = new Intent(getApplicationContext(), SlideSetActivity.class);
                    intent.putExtra("wordcount", count);
                    startActivity(intent);
                }else if(category.equals("alarm")) {
                    Intent intent = new Intent(getApplicationContext(), AlarmSetActivity.class);
                    intent.putExtra("wordcount", count);
                    startActivity(intent);
                }
            }
        });
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<100) {
                    count+=5;
                    cnt.setText("" + count);
                }
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>5) {
                    count-=5;
                    cnt.setText("" + count);
                }
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
}
