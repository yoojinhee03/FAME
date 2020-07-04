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

import java.util.Random;

public class CardGameActivity extends AppCompatActivity  implements View.OnClickListener {

    Button[] cardButton = new Button [12];//버튼
    Button reButton;
    Button finishButton;

    private int[] queran =new int[cardButton.length/2];//문제
    private int[] cardran = new int[cardButton.length];//카드랜덤
    private Cursor cursor;
    private int start=0;
    private int prev=-1;
    private int prevIndex=-1;
    private Handler mHandler;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_game);

        Intent intent=getIntent();
        level=intent.getStringExtra("level");

        SQLiteDatabase db = DBHelper.getInstance(CardGameActivity.this).getReadableDatabase();
        String sql = "SELECT * FROM "+level;
        cursor = db.rawQuery(sql, null);
        mHandler=new Handler();

        //for(int i=0; i<)
        for(int i=0; i<cardButton.length; i++){//12
            cardButton[i]=(Button)findViewById(R.id.button24 +i);
            cardButton[i].setOnClickListener(this);
        }
        reButton=findViewById(R.id.reButton);
        finishButton=findViewById(R.id.finishButton);
        reButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        QueRandom();

        //3.5초간 카드뒷면을 보여줌
        for(int i=0; i<cardButton.length; i++){
            setCard(i);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<cardButton.length; i++) {
                    resetCard(i);
                }
            }
        },3500);//3.5초
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==reButton.getId()){
            Intent intent=new Intent(CardGameActivity.this,CardGameActivity.class);
            intent.putExtra("level",level);
            startActivity(intent);
            finish();
        }
        if(v.getId()==finishButton.getId()){
            Intent intent=new Intent(CardGameActivity.this,GamemodeActivity.class);
            startActivity(intent);
            finish();
        }

        for(int i=0; i<cardButton.length; i++){//12
            if(cardButton[i].getText().toString().equals("")){// 로고이미지로 변경
                if(v.getId()==cardButton[i].getId()){
                    choiceChk(i,i);
                }
            }
        }
    }

    public void choiceChk(final int i, int index){
        if(start==0){//처음
            start=1;
            prev=cardran[index];
            prevIndex=i;
            setCard(i);
        }
        else if (cardButton.length-1 == prev+cardran[index]) {//정답
            setCard(i);
            start=0;
            Log.e("","정답");
        } else if (cardButton.length-1 != prev+cardran[index]) {//정답X
            setCard(i);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpCard(i);
                    start=0;

                }
            },500);//2초
            Log.e("","정답X");
        }
    }

    //3.5초간 뒷면을 보여주가 다시 앞면을 보여줌
    public void resetCard(int i){
        cardButton[i].setText("");
    }

    //카드클릭시
    public void setCard(int i) {
        if(cardButton[i].getText().toString().equals("")){// 로고이미지로 변경
            if(cardran[i]<cardran.length/2){
                Log.e("",cardran[i]+"");
                cursor.moveToPosition(queran[cardran[i]]);
                cardButton[i].setText(cursor.getString(cursor.getColumnIndex("WORD")));
            }else{
                Log.e("",cardButton.length-1-cardran[i]+"");
                cursor.moveToPosition(queran[cardButton.length - 1 - cardran[i]]);
                cardButton[i].setText(cursor.getString(cursor.getColumnIndex("MEAN")));
            }
        }
    }

    //두장의 카드가 맞지 않을 때
    public void setUpCard(int i){
        if (i < queran.length) {
            cursor.moveToPosition(queran[i]);
            cardButton[prevIndex].setText("");//// 로고이미지로 변경
            cardButton[i].setText("");// 로고이미지로 변경
        } else {
            cursor.moveToPosition(queran[cardButton.length - 1 - i]);
            cardButton[prevIndex].setText("");// 로고이미지로 변경
            cardButton[i].setText("");// 로고이미지로 변경
        }
    }

    //랜덤으로 문제 배치
    public void QueRandom(){
        Random random=new Random();
        Log.e("",cursor.getCount()+"");
        for(int i=0;i<queran.length;i++){
            queran[i]=random.nextInt(cursor.getCount());//0~50 데이터개수로 바꾸기 51
            for(int j=0;j<i;j++){//중복제거
                if(queran[i]==queran[j]) i--;
            }
        }
        CardRandom();
    }

    //카드 랜덤
    public void CardRandom(){
        Random random=new Random();

        for(int i=0;i<cardButton.length;i++){
            cardran[i]=random.nextInt(cardButton.length);//0~50 데이터개수로 바꾸기 51
            for(int j=0;j<i;j++){//중복제거
                if(cardran[i]==cardran[j]) i--;
            }
        }
    }
}
