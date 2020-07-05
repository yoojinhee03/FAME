package com.example.fame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class AlarmCompleteActivity extends AppCompatActivity {

    private String hour;
    private String minute;
    private String dayindex;
    private String category;
    private int inputcount;
    private String level;
    private int wordcount;
    protected static final String TABLE_NAME = "AlarmCategory";
    private ContentValues values;
    private Button finishButton;

    private SQLiteDatabase db;
    private Cursor cursor;
    private long newRowId;
    private FileOutputStream outputStream;
    private String fileName;
    private Word word = new Word();
    private WordJson wordJson=new WordJson();
    public static Context mContext;

    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        finishButton=(Button)findViewById(R.id.finishButton);

        final SlideCategory basiccategory=new SlideCategory();
        mContext=this;
        Intent intent = getIntent();
        hour = intent.getStringExtra("hour");
        minute = intent.getStringExtra("minute");
        dayindex = intent.getStringExtra("dayindex");
        category = intent.getStringExtra("category");
        level=intent.getStringExtra("level");
        inputcount = intent.getIntExtra("inputcount", -1);
        wordcount = intent.getIntExtra("wordcount", -1);

//        Toast.makeText(getApplicationContext(), "슬라이드 : " + category + "," + hour+ "," + minute+ "," + inputcount+"," + level+","+dayindex, Toast.LENGTH_LONG).show();

        this.calendar = Calendar.getInstance();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                values = new ContentValues();
                ((RepeatSetActivity) RepeatSetActivity.mContext).clearMyPrefs();
                values.put(AlarmCategory.CategoryEntry.COLUMN_NAME_HOUR, hour);
                values.put(AlarmCategory.CategoryEntry.COLUMN_NAME_MINUTE, minute);
                values.put(AlarmCategory.CategoryEntry.COLUMN_NAME_LEVEL, level);
                values.put(AlarmCategory.CategoryEntry.COLUMN_NAME_CATEGORY, category);
                values.put(AlarmCategory.CategoryEntry.COLUMN_NAME_INPUTCOUNT, inputcount);
                values.put(AlarmCategory.CategoryEntry.COLUMN_NAME_WORDCOUNT, wordcount);
                values.put(AlarmCategory.CategoryEntry.COLUMN_NAME_INDEX, dayindex);
                getDB();
                setAlarm();
                Intent intent=new Intent(AlarmCompleteActivity.this , EffortmodeActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getDB(){
        db= DBHelper.getInstance(this).getWritableDatabase();
        newRowId=db.insert(AlarmCategory.CategoryEntry.TABLE_NAME,
                null,
                values);

        if(newRowId==-1){
            Toast.makeText(getApplicationContext(),"저장에 문제가 발생하였습니다",Toast.LENGTH_SHORT).show();
        }else{
            Cursor cursor;
            DBHelper dbHelper;
            dbHelper=DBHelper.getInstance(this);
            cursor = dbHelper.getReadableDatabase().query(
                    AlarmCategory.CategoryEntry.TABLE_NAME,
                    null,null,null,null,null,null);

            cursor.moveToLast();
            int id= Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
            fileName=id+"alarm.json";
            writedata();
//            Toast.makeText(getApplicationContext(),"저장되었습니다",Toast.LENGTH_SHORT).show();
        }
    }
    public static AlarmManager alarmManager = null;
    public static PendingIntent pendingIntent = null;

    @RequiresApi(api= Build.VERSION_CODES.M)
    private void setAlarm(){//알람 설정

        Cursor cursor;
        DBHelper dbHelper;
        dbHelper=DBHelper.getInstance(this);
        cursor = dbHelper.getReadableDatabase().query(
                AlarmCategory.CategoryEntry.TABLE_NAME,
                null,null,null,null,null,null);

        cursor.moveToLast();
        String hour=cursor.getString(cursor.getColumnIndex("hour"));
        String minute=cursor.getString(cursor.getColumnIndex("minute"));
        int id= Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
        Log.e("",hour);
        Log.e("",minute);
        Log.e("",id+"");
        this.calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour));
        this.calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        this.calendar.set(Calendar.SECOND, 0);//10초뒤
        long aTime = System.currentTimeMillis();
        long bTime = calendar.getTimeInMillis();
        long interval = 1000 * 60 * 60  * 24;
        while(aTime>bTime){
            bTime += interval;
        }
        Log.e("아이디", String.valueOf(Integer.parseInt(cursor.getString(0))));

        //알람 설정
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(this,AlarmReceiver.class);
//        intent.setAction();
        intent.putExtra("category",category);
        intent.putExtra("id",id);
        intent.putExtra("dayindex",cursor.getString(cursor.getColumnIndex("dayindex")));

        pendingIntent = (PendingIntent) PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,bTime,pendingIntent);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, bTime, AlarmManager.INTERVAL_DAY, pendingIntent);
//        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,bTime,pendingIntent);
//        }else{
//            alarmManager.set(AlarmManager.RTC_WAKEUP,bTime, pendingIntent);
//        }// this.calendar.getTimeInMillis()

        //Toast 보여주기(알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
//        Toast.makeText(this, "Alarm: " + format.format(calendar.getTime()), Toast.LENGTH_LONG).show();
    }
    public String getLevel(){
        String sql = null;
        if(level.equals("초급")){
            sql ="SELECT * FROM BeginningLevel";
        }else if(level.equals("중급")) {
            sql = "SELECT * FROM intermediateLevel";
        }else if(level.equals("고급")) {
            sql = "SELECT * FROM highLevel";
        }
        return sql;
    }
    public void writedata(){
        Random random=new Random();
        int iarr[]=new int[wordcount];//중복 제거
        String startJson = "{\"note\":[";
        String endJson = "]}";
        byte[] startbyte = startJson.getBytes();
        byte[] endbyte = endJson.getBytes();

        cursor = db.rawQuery(getLevel(),null);
        try {
            outputStream = openFileOutput(fileName, Context.MODE_APPEND);
            outputStream.write(startbyte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<wordcount;i++){
            iarr[i]=random.nextInt(cursor.getCount());//0~50 데이터개수로 바꾸기 51
            for(int j=0;j<i;j++){//중복제거
                if(iarr[i]==iarr[j]) i--;
            }
        }
        for(int k=0; k<wordcount; k++) {
            cursor.moveToPosition(iarr[k]);
            word.setId(k);
            word.setWord( cursor.getString(1));
            word.setMean( cursor.getString(2));
            saveFile(k);
        }
        try {
            outputStream.write(endbyte);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveFile(int i){
        String rest = ",";
        byte[] restbyte = rest.getBytes();
        try {
            outputStream.write(wordJson.toJSon(word).getBytes());
            if(i<wordcount-1)outputStream.write(restbyte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

