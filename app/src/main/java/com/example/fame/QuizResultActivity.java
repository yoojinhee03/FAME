package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizResultActivity extends AppCompatActivity {

    private TextView levelText;
    private TextView countText;
    private Button nextButton;

    private int answercnt;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        Intent getIntent=getIntent();
        answercnt=getIntent.getIntExtra("result",-1);

        levelText=findViewById(R.id.levelText);
        countText=findViewById(R.id.countText);
        nextButton=findViewById(R.id.nextButton);

        if(answercnt<3){
            level="초급";
        }else if(answercnt<12){
            level="중급";
        }else{
            level="고급";
        }
        levelText.setText(level);
        countText.setText(Integer.toString(answercnt));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuizResultActivity.this,SelModeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
