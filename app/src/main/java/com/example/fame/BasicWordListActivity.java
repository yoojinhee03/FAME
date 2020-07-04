package com.example.fame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class BasicWordListActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton leftButton;
    ImageButton rightButton;
    WordListViewAdapter wordListViewAdapter;
    ListView listView;
    TextView startText;
    TextView endText;

    String level;
    String leveltext;
    int startnum,endnum;
    int page=1;
    int prepage;

    private ArrayList<Word> arrayList=new ArrayList<>();
    private TextToSpeech tts;
    SQLiteDatabase db;
    Cursor cursor;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_word_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기

        Intent intent=getIntent();
        level=intent.getStringExtra("level");
        leveltext=intent.getStringExtra("levelText");

        startnum=intent.getIntExtra("startnum",-1);
        endnum=intent.getIntExtra("endnum",-1);
        prepage=intent.getIntExtra("page",-1);

        leftButton=(ImageButton)findViewById(R.id.prevButton);
        rightButton=(ImageButton)findViewById(R.id.rightButton);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        listView=findViewById(R.id.listview);

        startText=findViewById(R.id.startText);
        endText=findViewById(R.id.endText);
        endText.setText("10");
        SQLiteDatabase db = DBHelper.getInstance(BasicWordListActivity.this).getReadableDatabase();
        String sql = "SELECT * FROM "+level;
        cursor = db.rawQuery(sql, null);
        cursor.moveToPosition(startnum-1);

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

        wordinfo();
        wordListViewAdapter=new WordListViewAdapter(BasicWordListActivity.this,arrayList,1);
        listView.setAdapter(wordListViewAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prevButton:
                if (page > 1) {
                    page--;
                    startText.setText(Integer.toString(page));
                    wordListViewAdapter = new WordListViewAdapter(BasicWordListActivity.this, arrayList, page);
                    listView.setAdapter(wordListViewAdapter);
                }
                break;
            case R.id.rightButton:
                if (page < 10) {
                    page++;
                    startText.setText(Integer.toString(page));
                    wordListViewAdapter = new WordListViewAdapter(BasicWordListActivity.this, arrayList, page);
                    listView.setAdapter(wordListViewAdapter);
                }
                break;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                if(tts != null){
                    tts.stop();
                    tts.shutdown();
                    tts = null;
                }
                Intent intent = new Intent(BasicWordListActivity.this, BasicWordActivity.class);
                intent.putExtra("startnum",startnum);
                intent.putExtra("endnum",endnum);
                intent.putExtra("level",level);
                intent.putExtra("page",prepage);
                intent.putExtra("levelText",leveltext);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//이전 엑티비티 다 죽이기
                finish();
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//뒤로가기

    public void wordinfo(){
        for(int i=0; i<50; i++) {
            Word word = new Word();
            word.setWord(cursor.getString(cursor.getColumnIndex("WORD")));
            word.setMean(cursor.getString(cursor.getColumnIndex("MEAN")));
            cursor.moveToNext();
            arrayList.add(word);
        }
    }
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
