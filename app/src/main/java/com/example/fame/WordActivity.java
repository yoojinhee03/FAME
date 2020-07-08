package com.example.fame;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class WordActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton leftButton;
    ImageButton rightButton;
    Button deleteButton;
    WordListViewAdapter wordListViewAdapter;
    ListView listView;
    TextView startText;
    TextView endText;
    int page;
    String category;
    Long id;
    private ArrayList<Word> arrayList=new ArrayList<>();
    String fileName;
    private TextToSpeech tts;
    SQLiteDatabase db;
    String table_name;
    String table_id;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        mContext = this;
        Intent intent=getIntent();
        category=intent.getStringExtra("Category");
        id=intent.getLongExtra("id",-1);
        fileName=intent.getStringExtra("fileName");
        table_name=intent.getStringExtra("table_name");
        table_id=intent.getStringExtra("table_id");
        wordinfo();
        leftButton=(ImageButton)findViewById(R.id.prevButton);
        rightButton=(ImageButton)findViewById(R.id.rightButton);
        deleteButton=(Button)findViewById(R.id.deleteButton);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        listView=findViewById(R.id.listview);
        startText=findViewById(R.id.startText);
        page=Integer.parseInt(startText.getText().toString());
        endText=findViewById(R.id.endText);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.ENGLISH);
                    Log.e("wordtts", tts +"");
                    tts();
                }
            }
        });

        wordListViewAdapter=new WordListViewAdapter(WordActivity.this,arrayList,1);
        listView.setAdapter(wordListViewAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        Log.e("아이디", String.valueOf(id));
        Log.e("Category", category);
        endText.setText(Integer.toString(wordcount()/5));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.prevButton:
                if(page>1){
                    page--;
                    startText.setText(Integer.toString(page));
                    wordListViewAdapter=new WordListViewAdapter(WordActivity.this,arrayList,page);
                    listView.setAdapter(wordListViewAdapter);
                }
                break;
            case R.id.rightButton:
                if(page<wordcount()/5){
                    page++;
                    startText.setText(Integer.toString(page));
                    wordListViewAdapter=new WordListViewAdapter(WordActivity.this,arrayList,page);
                    listView.setAdapter(wordListViewAdapter);
                }
                break;
            case R.id.deleteButton:
                Log.e("deleteButton","deleteButton");
                AlertDialog.Builder builder=new AlertDialog.Builder(WordActivity.this,R.style.MyAlertDialogStyle);
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db=DBHelper.getInstance(WordActivity.this).getWritableDatabase();
                        int deletedcount=db.delete(table_name,
                                table_id+"="+id,null);
                        if(deletedcount==0){
                            Toast.makeText(WordActivity.this, "삭제하는데 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(table_name.equals("SlideCategory")){
                                Intent intent = new Intent(getApplicationContext(), SlideService.class);
                                getApplicationContext().stopService(intent);
                               // fileName="slide.json";
                                if(deleteFile(fileName)){
                                    Intent intent1=new Intent(WordActivity.this,EffortmodeActivity.class);
                                    startActivity(intent1);
                                    finish();
                                }
                                serviceChk();
                            }
                            else if (table_name.equals("AlarmCategory")){
                                cancelAlarmManger(id);
                              //  fileName="alarm.json";
                                if(deleteFile(fileName)){
                                    Intent intent1=new Intent(WordActivity.this,EffortmodeActivity.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                            //Toast.makeText(EffortmodeActivity.this, "삭제함", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
        }
    }
    public void serviceChk(){
        SQLiteDatabase db=DBHelper.getInstance(WordActivity.this).getReadableDatabase();
        String sql = "SELECT * FROM SlideCategory";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()>0){
            cursor.moveToLast();
            Intent intent = new Intent(this, SlideService.class);
            SlideService slideService=new SlideService();
            slideService.setCategory(category);
            intent.putExtra("id",Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
            intent.putExtra("category",cursor.getString(cursor.getColumnIndex("category")));
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                startForegroundService(intent);
            }else startService(intent);
        }
    }

    public boolean deleteFile(String fileName){
        String dir = getFilesDir().getAbsolutePath();
        File f= new File(dir,fileName);
        if(f.exists()) {
            //Toast.makeText(mContext, "파일 삭제함", Toast.LENGTH_SHORT).show();
            f.delete();
            return true;
        }else return false;
    }
//    PendingIntent pendingIntent;
//    AlarmManager alarmManager;
    public void cancelAlarmManger(long alarmId) {//알람 삭제

        if (((AlarmCompleteActivity)AlarmCompleteActivity.mContext).pendingIntent!=null) {
            Log.e("아이디", String.valueOf(alarmId));
            ((AlarmCompleteActivity)AlarmCompleteActivity.mContext).alarmManager = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(mContext.getApplicationContext(), AlarmReceiver.class);
            ((AlarmCompleteActivity)AlarmCompleteActivity.mContext).pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), (int)alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            ((AlarmCompleteActivity)AlarmCompleteActivity.mContext).alarmManager.cancel(((AlarmCompleteActivity)AlarmCompleteActivity.mContext).pendingIntent);
            ((AlarmCompleteActivity)AlarmCompleteActivity.mContext).pendingIntent.cancel();
            ((AlarmCompleteActivity)AlarmCompleteActivity.mContext).alarmManager = null;
            ((AlarmCompleteActivity)AlarmCompleteActivity.mContext).pendingIntent = null;
        }
    }
    public void tts(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tts.setPitch(0.5f);
                tts.speak(arrayList.get(5*(page-1)+position).getWord(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
    public int wordcount(){//외우는 단어 개수
        SQLiteDatabase db=DBHelper.getInstance(WordActivity.this).getReadableDatabase();
        String sql="";
        if(category.equals("slide")){
            sql = "SELECT * FROM SlideCategory WHERE _id="+id;
        }else if(category.equals("alarm")){
            sql = "SELECT * FROM AlarmCategory WHERE _id="+id;
        }
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex("wordcount")));
    }
    public void wordinfo(){
        String dir = getFilesDir().getAbsolutePath();
        //File f= new File(dir,slidefileName);
        File file = new File(dir, fileName);
        if(file.exists()){
            try {
                String data = getStringFromFile(file.getPath());
                JSONObject jsonObject = new JSONObject(data);

                JSONArray wordArray = jsonObject.getJSONArray("note");
                for(int i=0; i<wordArray.length(); i++)
                {
                    JSONObject wordArrayJSONObject = wordArray.getJSONObject(i);

                    Word word = new Word();

                    word.setWord(wordArrayJSONObject.getString("word"));
                    word.setMean(wordArrayJSONObject.getString("mean"));
                    //Toast.makeText(this, word.getWord(), Toast.LENGTH_SHORT).show();
                    arrayList.add(word);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringFromFile(String path) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                if(tts != null){
                    tts.stop();
                    tts.shutdown();
                    tts = null;
                }
                Intent intent = new Intent(WordActivity.this,EffortmodeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//이전 엑티비티 다 죽이기
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//뒤로가기

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}