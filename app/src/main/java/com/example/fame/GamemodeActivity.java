package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GamemodeActivity extends AppCompatActivity implements View.OnClickListener{


    Button[] button = new Button [3];
    String[] level=new String[]{"BeginningLevel","intermediateLevel","highLevel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        for(int i=0; i<button.length; i++){
            button[i]=(Button)findViewById(R.id.button13+i);
            button[i].setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        for(int i=0; i<button.length; i++){
            if(v.getId()==button[i].getId()){
              //  Toast.makeText(this, "button0", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,CardGameActivity.class);
                intent.putExtra("level",level[i]);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(GamemodeActivity.this, SelModeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//이전 엑티비티 다 죽이기
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//뒤로가기
}
