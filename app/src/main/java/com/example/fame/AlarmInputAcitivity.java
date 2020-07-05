package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class AlarmInputAcitivity extends AppCompatActivity {

    Button CheckButton;
    TextView wordText;
    TextView meanText;
    TextView count;
    EditText editText;

    String fileName;
    JSONObject wordArrayJSONObject;
    String category;
    private int id;
    private MediaPlayer mediaPlayer;

    int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        id=intent.getIntExtra("id",-1);

        this.mediaPlayer= MediaPlayer.create(this, R.raw.alarm);
        this.mediaPlayer.start();

        CheckButton=findViewById(R.id.CheckButton);
        wordText=findViewById(R.id.endText);
        meanText=findViewById(R.id.startText);
        count=findViewById(R.id.count);
        count.setText(Integer.toString(inputcount()));
        cnt=inputcount();
        editText=findViewById(R.id.editText);
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

        CheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wordText.getText().toString().equals(editText.getText().toString())){
                    if(cnt>1) {
                        cnt--;
                        count.setText(Integer.toString(cnt));
                        editText.setText(null);
                    }else{
                        stopPlayer();
                    }
                }
            }
        });
    }

    public void stopPlayer(){
        if(this.mediaPlayer.isPlaying()){
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer=null;
        }
        finish();
    }
    public int inputcount(){

        SQLiteDatabase db=DBHelper.getInstance(AlarmInputAcitivity.this).getReadableDatabase();
        String sql = "SELECT * FROM AlarmCategory WHERE _id="+id;
        fileName=id+"alarm.json";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex("inputcount")));
    }
    public void wordinfo(){//퀴즈 데이터 가져오기
        //Toast.makeText(this, "inputActivity", Toast.LENGTH_SHORT).show();
        String dir = getFilesDir().getAbsolutePath();
        File file = new File(dir, fileName);
        if(file.exists()){
            try {
                String data = getStringFromFile(file.getPath());
                JSONObject jsonObject = new JSONObject(data);
                JSONArray wordArray = jsonObject.getJSONArray("note");
                wordArrayJSONObject = wordArray.getJSONObject(new Random().nextInt(wordArray.length()));
//                Toast.makeText(this, wordArrayJSONObject.getString("word"), Toast.LENGTH_SHORT).show();

                wordText.setText(wordArrayJSONObject.getString("word"));
                meanText.setText(wordArrayJSONObject.getString("mean"));

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "파일 X", Toast.LENGTH_SHORT).show();
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
}
