package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class SlideBasicActivity extends AppCompatActivity {

    private int id;
    SeekBar seekBar;
    TextView leftText;
    TextView rightText;
    TextView wordText;

    String fileName;
    JSONObject wordArrayJSONObject;
    JSONObject choiceArrayJSONObject;

    String answer;
    int randomquestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O_MR1){
            Log.d("","oreo기본");
            setShowWhenLocked(true);

            KeyguardManager keyguardManager=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this,null);
        }else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_slide_basic);

        Intent intent=getIntent();
        id=intent.getIntExtra("id",-1);

        seekBar=findViewById(R.id.seekBar);
        leftText=findViewById(R.id.startText);
        rightText=findViewById(R.id.rightText);
        wordText=findViewById(R.id.endText);
        getFileName();
        wordinfo();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //이미지 변경
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekBar.getProgress();
                Log.e("",answer+rightText.getText().toString());
                if(progress==100){//오른쪽
                    checkChoice(rightText.getText().toString());
                }else if(progress==0){
                    checkChoice(leftText.getText().toString());
                }else{
                    seekBar.setProgress(50);
                }
            }
        });
    }

    public static void setSeekberThumb(final SeekBar seekBar, final Resources res) {
        seekBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                if (seekBar.getHeight() > 0) {
                    Drawable thumb = res.getDrawable(R.drawable.slidethumb);
                    int h = seekBar.getMeasuredHeight();
                    int w = h;
                    Bitmap bmpOrg = ((BitmapDrawable) thumb).getBitmap();
                    Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                    Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                    newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                    seekBar.setThumb(newThumb);
                    seekBar.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    public void checkChoice(String choice){

        if(answer.equals(choice)){
           // ActivityCompat.finishAffinity(SlideBasicActivity.this);//모든 엑티비티 종료
            finish();
        }else{
            seekBar.setProgress(50);
        }
    }

    public void getFileName(){
        SQLiteDatabase db=DBHelper.getInstance(SlideBasicActivity.this).getReadableDatabase();
        String sql="";
        sql = "SELECT * FROM SlideCategory WHERE _id="+id;
        fileName=id+"slide.json";
        Log.e("id",""+id);
    }

    public void wordinfo(){//퀴즈 데이터 가져오기
        //Toast.makeText(this, "inputActivity", Toast.LENGTH_SHORT).show();
        String dir = getFilesDir().getAbsolutePath();
        File file = new File(dir, fileName);
        String choice;
        if(file.exists()){
            try {
                while(leftText.getText().toString().equals(rightText.getText().toString())) {
                    int randomchoice = new Random().nextInt(2);//0~1
                    randomquestion = new Random().nextInt(2);//0~1
                    String data = getStringFromFile(file.getPath());
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray wordArray = jsonObject.getJSONArray("note");
                    wordArrayJSONObject = wordArray.getJSONObject(new Random().nextInt(wordArray.length()));
                    choiceArrayJSONObject = wordArray.getJSONObject(new Random().nextInt(wordArray.length()));
                    //Toast.makeText(this, wordArrayJSONObject.getString("word"), Toast.LENGTH_SHORT).show();
                    if (randomquestion == 1) {
                        wordText.setText(wordArrayJSONObject.getString("word"));
                        answer = wordArrayJSONObject.getString("mean");
                        choice = "mean";
                    } else {
                        wordText.setText(wordArrayJSONObject.getString("mean"));
                        answer = wordArrayJSONObject.getString("word");
                        choice = "word";
                    }
                    if (randomchoice == 1) {
                        leftText.setText(choiceArrayJSONObject.getString(choice));
                        rightText.setText(wordArrayJSONObject.getString(choice));
                    } else {
                        rightText.setText(choiceArrayJSONObject.getString(choice));
                        leftText.setText(wordArrayJSONObject.getString(choice));
                    }
                }
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
