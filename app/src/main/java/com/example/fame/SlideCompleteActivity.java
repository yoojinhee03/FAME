package com.example.fame;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.util.Random;

public class SlideCompleteActivity extends AppCompatActivity {

    private int wordcount;
    private int repeatcount;
    private String category;
    private int inputcount;

    private String level;
    protected static final String TABLE_NAME = "SlideCategory";
    private ContentValues values;
    private Button finishButton;
    private SQLiteDatabase db;
    private Cursor cursor;
    private long newRowId;
    private TextView text;
    private FileOutputStream outputStream;
    private String fileName;
    private Word word = new Word();
    private WordJson wordJson=new WordJson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        finishButton=(Button)findViewById(R.id.finishButton);

        Intent intent = getIntent();
        wordcount = intent.getIntExtra("wordcount", -1);
        repeatcount = intent.getIntExtra("repeatcount", -1);
        level=intent.getStringExtra("level");
        category=intent.getStringExtra("category");
        inputcount = intent.getIntExtra("inputcount", -1);

        text=findViewById(R.id.text);
      //  Toast.makeText(getApplicationContext(), "슬라이드 : " + category + "," + wordcount+ "," + repeatcount+ "," + inputcount+"," + level, Toast.LENGTH_LONG).show();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((InputSetActivity)InputSetActivity.mContext).clearMyPrefs();//설정할때의 값 reset
                values = new ContentValues();
                values.put(SlideCategory.CategoryEntry.COLUMN_NAME_WORDCOUNT, wordcount);
                values.put(SlideCategory.CategoryEntry.COLUMN_NAME_REPEATCOUNT, repeatcount);
                values.put(SlideCategory.CategoryEntry.COLUMN_NAME_LEVEL, level);
                values.put(SlideCategory.CategoryEntry.COLUMN_NAME_CATEGORY, category);
                values.put(SlideCategory.CategoryEntry.COLUMN_NAME_INPUTCOUNT, inputcount);
                getDB();
                Intent intent=new Intent(SlideCompleteActivity.this,EffortmodeActivity.class);
                //intent.putExtra("slidefileName",slidefileName);
                startActivity(intent);
            }
        });
    }
    public void getDB(){
        db= DBHelper.getInstance(this).getWritableDatabase();
        newRowId=db.insert(SlideCategory.CategoryEntry.TABLE_NAME,
                null,
                values);
        if(newRowId==-1){
           // Toast.makeText(getApplicationContext(),"저장에 문제가 발생하였습니다",Toast.LENGTH_SHORT).show();
        }else{
            db= DBHelper.getInstance(this).getReadableDatabase();
            String sql="SELECT _id from SlideCategory";
            Cursor cursor=db.rawQuery(sql,null);
            cursor.moveToLast();
            Intent intent = new Intent(this, SlideService.class);
            SlideService slideService=new SlideService();
            slideService.setCategory(category);
            intent.putExtra("id",Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
            intent.putExtra("category",category);
            fileName=Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")))+"slide.json";
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                startForegroundService(intent);
            }else
                startService(intent);
            writedata();
          //  Toast.makeText(getApplicationContext(),"저장되었습니다",Toast.LENGTH_SHORT).show();
        }
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
//        Toast.makeText(getApplicationContext(), "슬라이드 : " + category + "," + wordcount+ "," + repeatcount+"," + level, Toast.LENGTH_LONG).show();

//        TextView text=findViewById(R.id.textView25);
//        WordDBHelper wordbHelper=WordDBHelper.getInstance(this);
//
//        String sql ="SELECT * FROM BeginningLevel";
//
//
//        db = wordbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery(sql,null);
//
//        while(cursor.moveToNext()) {
//            text.setText(cursor.getString(2));
//    }