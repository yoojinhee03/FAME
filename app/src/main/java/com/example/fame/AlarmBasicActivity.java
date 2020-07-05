package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class AlarmBasicActivity extends AppCompatActivity{

    private TextView wordText;
    private Button button1;
    private Button button2;
    private Button button3;

    private String fileName;
    private JSONObject wordArrayJSONObject;
    private JSONObject choiceArrayJSONObject;

    private String category;
    private int id;
    private String answer;
    private MediaPlayer mediaPlayer;
    private int randomquestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_basic);

        this.mediaPlayer= MediaPlayer.create(this, R.raw.alarm);
        this.mediaPlayer.start();

        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        id=intent.getIntExtra("id",-1);
        Log.e("input",id+"");

        wordText=findViewById(R.id.endText);
        button1=findViewById(R.id.selectButton1);
        button2=findViewById(R.id.selectButton2);
        button3=findViewById(R.id.selectButton3);
        getFileName();
        wordinfo();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true);
            KeyguardManager keyguardManager=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this,null);

        }else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChoice(button1.getText().toString());
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChoice(button2.getText().toString());
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChoice(button3.getText().toString());
            }
        });
    }

    public void checkChoice(String choice){
        if(answer.equals(choice)){
            if(this.mediaPlayer.isPlaying()){
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
                this.mediaPlayer=null;
            }
            finish();
        }
    }

    public void getFileName(){
        SQLiteDatabase db=DBHelper.getInstance(AlarmBasicActivity.this).getReadableDatabase();
        String sql="";
        sql = "SELECT * FROM AlarmCategory WHERE _id="+id;
        fileName=id+"alarm.json";
        Log.e("id",""+fileName);
    }
    public void wordinfo(){//퀴즈 데이터 가져오기
        //Toast.makeText(this, "inputActivity", Toast.LENGTH_SHORT).show();
        String dir = getFilesDir().getAbsolutePath();
        File file = new File(dir, fileName);
        String choice;

        if(file.exists()){
            Log.e("파일 있음","파일임");
            try {
                while(
                    button1.getText().toString().equals(button2.getText().toString())||
                    button1.getText().toString().equals(button3.getText().toString())||
                    button2.getText().toString().equals(button3.getText().toString())) {
                    int randomchoice = new Random().nextInt(3);//0~2
                    randomquestion = new Random().nextInt(2);//0~1
                    String data = getStringFromFile(file.getPath());
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray wordArray = jsonObject.getJSONArray("note");
                    wordArrayJSONObject = wordArray.getJSONObject(new Random().nextInt(wordArray.length()));

                    if (randomquestion == 1) {
                        wordText.setText(wordArrayJSONObject.getString("word"));
                        answer = wordArrayJSONObject.getString("mean");
                        choice = "mean";
                    } else {
                        wordText.setText(wordArrayJSONObject.getString("mean"));
                        answer = wordArrayJSONObject.getString("word");
                        choice = "word";
                    }
                        if(randomchoice==0) answer(button1,choice);
                        if(randomchoice==1) answer(button2,choice);
                        if(randomchoice==2) answer(button3,choice);
                    for(int i=0; i<2; i++){
                        choiceArrayJSONObject = wordArray.getJSONObject(new Random().nextInt(wordArray.length()));
                        if(randomchoice==0) notAnswer(i,button2,button3,choice);
                        if(randomchoice==1) notAnswer(i,button1,button3,choice);
                        if(randomchoice==2) notAnswer(i,button1,button2,choice);
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
//            Toast.makeText(this, "파일 X", Toast.LENGTH_SHORT).show();
        }
    }

    public void answer(Button answer,String choice){
        try {
            answer.setText(wordArrayJSONObject.getString(choice));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void notAnswer(int i,Button button1,Button button2,String choice){
        try {
                if(i==0) {
                    button1.setText(choiceArrayJSONObject.getString(choice));
                }else button2.setText(choiceArrayJSONObject.getString(choice));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
    private String getStringFromFile(String path) {//파일 경로찾기
        String json = "";

        try {
            InputStream inputStream=openFileInput(fileName);
            int fileSize = inputStream.available();

            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }//백키 막기
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //MediaPlayer release()
//        if(this.mediaPlayer !=null) {
//         this.mediaPlayer.stop();
//         this.mediaPlayer.release();
//         this.mediaPlayer=null;
//
//        }
    }
}
