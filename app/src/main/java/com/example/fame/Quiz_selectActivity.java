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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Quiz_selectActivity extends AppCompatActivity {

    private TextView meanText;
    private TextView startText;
    private TextView endText;
    private Button selectButton1;
    private Button selectButton2;
    private Button selectButton3;

    private Cursor cursor;
    private int cnt;
    private int answercnt=0;
    private int iarr[];
    private int buttonarr[];

    final private int quizcount=10;//퀴즈수 + 1
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_select);

        meanText=findViewById(R.id.meanText);
        startText=findViewById(R.id.startText);
        startText.setText("1");
        endText=findViewById(R.id.endText);
        endText.setText(Integer.toString(quizcount));
        selectButton1=findViewById(R.id.selectButton1);
        selectButton2=findViewById(R.id.selectButton2);
        selectButton3=findViewById(R.id.selectButton3);
        cnt=0;
        mHandler=new Handler();
        findViewById(R.id.chk_ok).setVisibility(View.INVISIBLE);

        SQLiteDatabase db = DBHelper.getInstance(Quiz_selectActivity.this).getReadableDatabase();
        String sql = "SELECT * FROM quiz_select";
        cursor = db.rawQuery(sql, null);

        setQuizrandom();
        setQuizText(cnt);

        selectButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton(selectButton1.getText().toString());
            }
        });
        selectButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton(selectButton2.getText().toString());
            }
        });
        selectButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton(selectButton3.getText().toString());
            }
        });
    }

    public void onClickButton(String str){
        if(cnt>=quizcount-1){
            Intent intent=new Intent(Quiz_selectActivity.this,QuizResultActivity.class);
            intent.putExtra("result",answercnt);
            startActivity(intent);
            finish();
        }else{
            isResult(str);
        }
    }
    public void next(){
        setQuizText(cnt);
        startText.setText(Integer.toString(cnt+1));
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
            },1000);//2초
        }else{
            next();
        }
    }
    public void isResult(String choice){
        cnt++;
        if(choice.equals(cursor.getString(cursor.getColumnIndex("result")))){
            init_chk_ok(true);
            answercnt++;
            Log.e("result","O");
        }else{
            init_chk_ok(false);
            Log.e("result","X");
        }
    }

    //랜덤으로 퀴즈 배치
    public void setQuizrandom(){
        iarr=random(iarr,quizcount,cursor.getCount());
    }

    public void setQuizText(int cnt){
        buttonarr=random(buttonarr,3,3);

        cursor.moveToPosition(iarr[cnt]);
        meanText.setText(cursor.getString(cursor.getColumnIndex("mean")));
        selectButton1.setText(cursor.getString(buttonarr[0]+2));
        selectButton2.setText(cursor.getString(buttonarr[1]+2));
        selectButton3.setText(cursor.getString(buttonarr[2]+2));
    }

    public int[] random(int arr[],int cnt,int datacnt){
        Random random=new Random();
        arr=new int[cnt];//중복 제거
        for(int i=0;i<cnt;i++){
            arr[i]=random.nextInt(datacnt);//0~50 데이터개수로 바꾸기 51
            for(int j=0;j<i;j++){//중복제거
                if(arr[i]==arr[j]) i--;
            }
        }
        return arr;
    }
}
