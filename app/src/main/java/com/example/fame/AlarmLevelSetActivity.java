package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AlarmLevelSetActivity extends AppCompatActivity {

    ImageButton nextbutton;
    Spinner levelSpinner;
    ArrayAdapter levelAdapter;
    String hour;
    String minute;
    String  dayindex;
    String category;
    int inputcount;
    int wordcount;
    String level;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_set);

        nextbutton=findViewById(R.id.nextButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        Intent intent=getIntent();

        hour = intent.getStringExtra("hour");
        minute = intent.getStringExtra("minute");
        dayindex = intent.getStringExtra("dayindex");
        category = intent.getStringExtra("category");
        wordcount=intent.getIntExtra("wordcount",-1);
        inputcount = intent.getIntExtra("inputcount", -1);
            Toast.makeText(getApplicationContext(), "알람 : " + hour + "," + minute+ "," +dayindex+ "," + inputcount +","+ category, Toast.LENGTH_LONG).show();
        //}
       // else Toast.makeText(getApplicationContext(), "알람 : " + hour + "," + minute+ "," + index[0] +","+ category, Toast.LENGTH_LONG).show();

        levelSpinner=(Spinner)findViewById(R.id.levelSpinner);//android.R.layout.simple_spinner_item
        levelAdapter = ArrayAdapter.createFromResource(this, R.array.level, R.layout.spinner_item);
        //R.layout.support_simple_spinner_dropdown_item
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(levelAdapter);//arrays.xml과 Spinner 연결
        levelSpinner.getAdapter();

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level=levelSpinner.getSelectedItem().toString();
                Intent intent=new Intent(getApplicationContext(),AlarmCompleteActivity.class);
                intent.putExtra("hour",hour);
                intent.putExtra("minute",minute);
                intent.putExtra("category",category);
                intent.putExtra("level", level);
                intent.putExtra("inputcount", inputcount);
                intent.putExtra("wordcount", wordcount);
                intent.putExtra("dayindex", dayindex);

                startActivity(intent);
            }
        });
    }
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
