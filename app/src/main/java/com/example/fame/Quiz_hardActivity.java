package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Quiz_hardActivity extends AppCompatActivity {

    private TextView meanText;
    private TextView engText;
    private EditText answerText;
    private TextView startText;
    private TextView endText;
    private Button chkButton;

    private Cursor cursor;
    private int cnt;
    private int answercnt=0;
    private int iarr[];
    final private int quizcount=20;//퀴즈수
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_hard);

        meanText=findViewById(R.id.meanText);
        engText=findViewById(R.id.engText);
        answerText=findViewById(R.id.answerText);
        startText=findViewById(R.id.startText);
        startText.setText("1");
        endText=findViewById(R.id.endText);
        endText.setText(Integer.toString(quizcount));
        chkButton=findViewById(R.id.chkButton);
        cnt=0;
        mHandler=new Handler();
        findViewById(R.id.chk_ok).setVisibility(View.INVISIBLE);

        SQLiteDatabase db = DBHelper.getInstance(Quiz_hardActivity.this).getReadableDatabase();
        String sql = "SELECT * FROM quiz_hard";
        cursor = db.rawQuery(sql, null);
        setQuizrandom();
        setQuizText(cnt);

        chkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(cnt==quizcount-1){
                    Intent intent=new Intent(Quiz_hardActivity.this,QuizResultActivity.class);
                    intent.putExtra("result",answercnt);
                    startActivity(intent);
                    finish();
                }else{
                    if(isResult()){
                        init_chk_ok(true);
                    }else{
                        init_chk_ok(false);
                    }
                }
                answerText.setText(null);
            }
        });
    }

    public void init_chk_ok(boolean bool){
        if(bool){
            findViewById(R.id.chk_ok).setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.chk_ok).setVisibility(View.INVISIBLE);
                    next();
                }
            },1500);//2초
        }else{
            next();
        }
    }

    //랜덤으로 퀴즈 배치
    public void setQuizrandom(){
        Random random=new Random();
        iarr=new int[quizcount];//중복 제거
        for(int i=0;i<quizcount;i++){
            iarr[i]=random.nextInt(cursor.getCount());//0~50 데이터개수로 바꾸기 51
            for(int j=0;j<i;j++){//중복제거
                if(iarr[i]==iarr[j]) i--;
            }
        }
    }

    public void next(){
        setQuizText(cnt);
        startText.setText(Integer.toString(cnt+1));
    }
    public void setQuizText(int cnt){
        cursor.moveToPosition(iarr[cnt]);
        meanText.setText(cursor.getString(cursor.getColumnIndex("mean")));
        engText.setText(underline());
    }
    //답 체크
    public Boolean isResult(){
        Log.e("result", cursor.getString(cursor.getColumnIndex("result")));
        Log.e("answerText", answerText.getText().toString());
        cnt++;
        if(answerText.getText().toString().equals(cursor.getString(cursor.getColumnIndex("result")).toLowerCase())){
            answercnt++;
            return true;
        }else {
            return false;
        }
    }
    public String underline(){
        String content = cursor.getString(cursor.getColumnIndex("quiz"));
        String underline="";
        for(int i=0; i<cursor.getString(cursor.getColumnIndex("result")).length(); i++){
            underline+="_";
        }
        content=content.replace(cursor.getString(cursor.getColumnIndex("result")),underline);
        return content;
    }
}
