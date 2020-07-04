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

public class Quiz_inputActivity extends AppCompatActivity {

    private TextView queText;
    private TextView startText;
    private TextView endText;
    private EditText answerText;
    private Button chkButton;

    private Cursor cursor;
    private int cnt;
    private int answercnt=0;
    private int iarr[];
    private int ran;
    final private int quizcount=20;//퀴즈수
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_input);

        queText=findViewById(R.id.queText);
        startText=findViewById(R.id.startText);
        startText.setText("1");
        endText=findViewById(R.id.endText);
        endText.setText(Integer.toString(quizcount));
        answerText=findViewById(R.id.answerText);
        chkButton=findViewById(R.id.chkButton);
        cnt=0;
        mHandler=new Handler();
        findViewById(R.id.chk_ok).setVisibility(View.INVISIBLE);

        SQLiteDatabase db = DBHelper.getInstance(Quiz_inputActivity.this).getReadableDatabase();
        String sql = "SELECT * FROM BeginningLevel";
        cursor = db.rawQuery(sql, null);
        setQuizrandom();
        ran=setQuizText(cnt);//뜻인지 영어단어인지

        chkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt>=quizcount-1){
                    Intent intent=new Intent(Quiz_inputActivity.this,QuizResultActivity.class);
                    intent.putExtra("result",answercnt);
                    startActivity(intent);
                    finish();
                }else{
                    isResult(ran);
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

    public void isResult(int ran){
        cnt++;
        if(ran==0) {
            if (answerText.getText().toString().equals(cursor.getString(cursor.getColumnIndex("MEAN")))) {
                answercnt++;
                init_chk_ok(true);
                Log.e("","O");
            }else{
                init_chk_ok(false);
            }
        }else{
            if (answerText.getText().toString().equals(cursor.getString(cursor.getColumnIndex("WORD")))) {
                answercnt++;
                init_chk_ok(true);
                Log.e("","X");
            }else{
                init_chk_ok(false);
            }
        }
        Log.e("",cursor.getString(cursor.getColumnIndex("WORD")));
        Log.e("",cursor.getString(cursor.getColumnIndex("MEAN")));
    }

    public void next(){
        ran=setQuizText(cnt);
        startText.setText(Integer.toString(cnt+1));
    }
    public int setQuizText(int cnt){
        cursor.moveToPosition(iarr[cnt]);
        Random random=new Random();
        int ran=random.nextInt(2);//0~50 데이터개수로 바꾸기 51
        if(ran==0){
            queText.setText(cursor.getString(cursor.getColumnIndex("WORD")));
        }else{
            queText.setText(cursor.getString(cursor.getColumnIndex("MEAN")));
        }
        return ran;
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
}
