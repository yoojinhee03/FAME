package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SlideLevelSetActivity extends AppCompatActivity {

    ImageButton nextbutton;
    Spinner levelSpinner;
    ArrayAdapter levelAdapter;
    int wordcount;
    int repeatcount;
    String category;
    int inputcount;
    String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_set);

        nextbutton=findViewById(R.id.nextButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        Intent intent=getIntent();
        wordcount = intent.getIntExtra("wordcount", -1);
        repeatcount = intent.getIntExtra("repeatcount", -1);
        category = intent.getStringExtra("category");
        inputcount=-1;
        //if(category.equals("입력하기")) {
            inputcount = intent.getIntExtra("inputcount", -1);
//            Toast.makeText(getApplicationContext(), "슬라이드 : " + category + "," + wordcount+ "," + repeatcount+ "," + inputcount, Toast.LENGTH_LONG).show();
     //   }
//        else Toast.makeText(getApplicationContext(), "슬라이드 : " + category + "," + wordcount+ "," + repeatcount , Toast.LENGTH_LONG).show();

        levelSpinner=(Spinner)findViewById(R.id.levelSpinner);
        levelAdapter = ArrayAdapter.createFromResource(this, R.array.level, R.layout.spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(levelAdapter);//arrays.xml과 Spinner 연결


        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level=levelSpinner.getSelectedItem().toString();
                Intent intent=new Intent(getApplicationContext(),SlideCompleteActivity.class);
                intent.putExtra("wordcount",wordcount);
                intent.putExtra("repeatcount",repeatcount);
                intent.putExtra("category",category);
                intent.putExtra("level", level);
                intent.putExtra("inputcount",inputcount);

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
