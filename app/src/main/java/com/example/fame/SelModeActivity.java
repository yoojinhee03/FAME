package com.example.fame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import javax.security.auth.Destroyable;

public class SelModeActivity extends AppCompatActivity {

    private ImageButton effortButton;
    private ImageButton basicButton;
    private ImageButton quizButton;
    private ImageButton gameButton;

    private SQLiteDatabase db;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_mode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M // M 버전(안드로이드 6.0 마시멜로우 버전) 보다 같거나 큰 API에서만 설정창 이동 가능합니다.,
                && !Settings.canDrawOverlays(this)) {
            PermissionOverlay();
        }//백그라운드 실행
        initLoadDB();

        effortButton = (ImageButton) findViewById(R.id.effortButton);
        basicButton = (ImageButton) findViewById(R.id.basicButton);
        quizButton=(ImageButton) findViewById(R.id.quizButton);
        gameButton=(ImageButton) findViewById(R.id.gameButton);

        Toast.makeText(SelModeActivity.this, ""+count(),Toast.LENGTH_SHORT).show();

        effortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count()<=0){
                    Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), EffortmodeActivity.class);
                    intent.putExtra("mode","null");
                    startActivity(intent);
                }
            }
        });

        basicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BasicmodeActivity.class);
                startActivity(intent);
            }
        });
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuizmodeActivity.class);
                //Intent intent = new Intent(getApplicationContext(), Quiz_selectActivity.class);
                startActivity(intent);
            }
        });
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GamemodeActivity.class);
                //Intent intent = new Intent(getApplicationContext(), Quiz_selectActivity.class);
                startActivity(intent);
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

            }
        }
    }
    public int count() {
        int cnt = 0;
        Cursor cursor;
        db=DBHelper.getInstance(this).getWritableDatabase();
        String slidesql = "SELECT * FROM SlideCategory";
        cursor = db.rawQuery(slidesql, null);
        cnt+=cursor.getCount();
        String alarmsql = "SELECT * FROM AlarmCategory";
        cursor = db.rawQuery(alarmsql, null);
        cnt+=cursor.getCount();
        return cnt;
    }//끈기모드 데이터베이스 레코드 개수 반환

    public int slidecount(){
        int cnt = 0;
        Cursor cursor;
        db=DBHelper.getInstance(this).getWritableDatabase();
        String slidesql = "SELECT * FROM SlideCategory";
        cursor = db.rawQuery(slidesql, null);
        cnt=cursor.getCount();
        return cnt;
    }

    private void initLoadDB() {
        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        // db에 있는 값들을 model을 적용해서 넣는다.
        //List = mDbHelper.getTableData();
        // db 닫기
        mDbHelper.close();
    }
}
