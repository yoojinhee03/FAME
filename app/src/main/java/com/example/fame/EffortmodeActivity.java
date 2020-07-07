package com.example.fame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

import static com.example.fame.SelModeActivity.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE;

public class EffortmodeActivity extends AppCompatActivity {

    private ImageButton addButton;
    private Button slideButton;
    private Button alarmButton;
    private ImageButton plusButton;

    private Cursor cursor;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private ListViewAdapter listViewAdapter;
    private ListView listView;

    private String table;
    private String table_name;
    private String table_id;
    private String buttontextcolor;
    private String buttonchangecolor;

    private String fileName;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effortmode);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M // M 버전(안드로이드 6.0 마시멜로우 버전) 보다 같거나 큰 API에서만 설정창 이동 가능합니다.,
                && !Settings.canDrawOverlays(this)) {
            PermissionOverlay();
        }//백그라운드 실행

        mContext = this;
        listView=findViewById(R.id.word_note);
        View footer = getLayoutInflater().inflate(R.layout.activity_effortmode_footer, null, false) ;
        addButton = (ImageButton) footer.findViewById(R.id.addButton);
        slideButton = (Button) findViewById(R.id.slideButton);
        alarmButton = (Button) findViewById(R.id.alarmButton);
        table="";
        buttontextcolor="#017BEC";
        buttonchangecolor="#FF666363";
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
        dbHelper=DBHelper.getInstance(this);
        listView.addFooterView(footer);

        if(recordalarmcount()>0){
            alarmlist();
        }else if(recordslidecount()>0){
            slidelist();
        }else{
            Intent intent=new Intent(EffortmodeActivity.this,SelModeActivity.class);
            startActivity(intent);
            finish();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), WordSetActivity.class);

                if(table.equals("slide")){
                    intent.putExtra("category","slide");
                }else if(table.equals("alarm")){
                    intent.putExtra("category","alarm");
                }
                startActivity(intent);
            }
        });

        slideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidelist();
            }
        });
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmlist();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M) //M 버전 이상 API를 타겟으로,
    public void PermissionOverlay() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }//백그라운드 실행

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                // You have permission
// 오버레이 설정창 이동 후 이벤트 처리합니다.

            }else{
                finish();
            }
        }
    }
    public void slidelist(){
        table="slide";
        table_name=SlideCategory.CategoryEntry.TABLE_NAME;
        table_id= SlideCategory.CategoryEntry.COLUMN_NAME_ID;
        slideButton.setTextColor(Color.parseColor(buttontextcolor));
        alarmButton.setTextColor(Color.parseColor(buttonchangecolor));
        list();
    }

    public void alarmlist(){
        table="alarm";
        table_name=AlarmCategory.CategoryEntry.TABLE_NAME;
        table_id= AlarmCategory.CategoryEntry.COLUMN_NAME_ID;
        alarmButton.setTextColor(Color.parseColor(buttontextcolor));
        slideButton.setTextColor(Color.parseColor(buttonchangecolor));
        list();
    }
    public void list(){
        cursor = getCursor();
        listViewAdapter=new ListViewAdapter(EffortmodeActivity.this,cursor,table);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//리스트 눌렀을때 단어장으로 이동
                Intent intent=new Intent(EffortmodeActivity.this,WordActivity.class);
                //Cursor cursor=(Cursor) listViewAdapter.getItem(position);
                intent.putExtra("id",id);
                intent.putExtra("Category",table);
                intent.putExtra("table_name",table_name);
                intent.putExtra("table_id",table_id);
                fileName=id+table+".json";
                intent.putExtra("fileName",fileName);
                view.setFocusable(false);
                startActivity(intent);
            }
        });
    }


    private Cursor getCursor(){
        return dbHelper.getReadableDatabase().query(
                table_name,
                null,null,null,null,null,table_id+" DESC");
    }

    public int recordslidecount() {
        String sql = "SELECT * FROM SlideCategory";
        return cnt(sql);
    }//끈기모드 데이터베이스 레코드 개수 반환

    public int recordalarmcount() {
        String sql = "SELECT * FROM AlarmCategory";
        return cnt(sql);
    }

    public int cnt(String sql){
        int cnt=0;
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(sql, null);
        cnt+=cursor.getCount();
        return cnt;
    }
    public void alarmsetlayout(View view){
        TextView text=view.findViewById(R.id.text);
        TextView hourtext=view.findViewById(R.id.hourtext);
        TextView minutetext=view.findViewById(R.id.minutetext);
        TextView levelText=view.findViewById(R.id.levelText);
        TextView dayText=view.findViewById(R.id.dayText);

        int hour=Integer.parseInt(cursor.getString(1));
        if(hour>=12&&hour<24){
            text.setText("오후");
        }
        if(hour>=13&&hour<=24){
            hour-=12;
        }else if(hour==0){
            hour+=12;
        }
        hourtext.setText(Integer.toString(hour));
        minutetext.setText(cursor.getString(cursor.getColumnIndexOrThrow(AlarmCategory.CategoryEntry.COLUMN_NAME_MINUTE)));
        //dayText.setText(cursor.getString(cursor.getColumnIndexOrThrow(AlarmCategory.CategoryEntry.COLUMN_NAME_INDEX)));
        levelText.setText(cursor.getString(cursor.getColumnIndexOrThrow(AlarmCategory.CategoryEntry.COLUMN_NAME_LEVEL)));
        String days=cursor.getString(cursor.getColumnIndexOrThrow(AlarmCategory.CategoryEntry.COLUMN_NAME_INDEX));

        String day[]=days.split("/");
        String result="";
        for(int i=0; i<day.length; i++){
            if(!day[i].equals("X")){
                Log.e("",day[i]);
                result+=day[i];
            }
        }
        dayText.setText(result);

    }
    public void slidesetlayout(View view){
        TextView wordcntText=view.findViewById(R.id.wordcntText);
        TextView levelText=view.findViewById(R.id.levelText);

        wordcntText.setText(cursor.getString(cursor.getColumnIndexOrThrow(SlideCategory.CategoryEntry.COLUMN_NAME_WORDCOUNT)));
        levelText.setText(cursor.getString(cursor.getColumnIndexOrThrow(SlideCategory.CategoryEntry.COLUMN_NAME_LEVEL)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(EffortmodeActivity.this,SelModeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//이전 엑티비티 다 죽이기
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//뒤로가기
}