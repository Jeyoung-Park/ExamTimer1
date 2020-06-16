package com.examtimer1.Record;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.examtimer1.DBHelper_tt;

import com.examtimer1.examtimer.R;
import com.examtimer1.TimerRecordItem_tt;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class Record_total_specific extends AppCompatActivity {

    private Context mContext;
    private SQLiteDatabase db;
    private DBHelper_tt mDBHelper;
    private TextView TextView_time_korean_tt, TextView_time_math_tt, TextView_time_english_tt, TextView_time_history_tt,
                    TextView_time_science1_tt, TextView_time_science2_tt, TextView_time_foreign_language_tt, TextView_title;

    private RecyclerView recyclerView;
    private ArrayList<TimerRecordItem_tt> timerRecordItems = new ArrayList<>();
    RecyclerView.Adapter recyclerviewAdater;
    RecyclerView.LayoutManager layoutManager;
    private AdView mAdView;

    private Long totalId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_total_specific);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        TextView_time_korean_tt = findViewById(R.id.TextView_record_total_Korean);
        TextView_time_math_tt = findViewById(R.id.TextView_record_total_math);
        TextView_time_english_tt = findViewById(R.id.TextView_record_total_English);
        TextView_time_history_tt = findViewById(R.id.TextView_record_total_history);
        TextView_time_science1_tt = findViewById(R.id.TextView_record_total_science1);
        TextView_time_science2_tt = findViewById(R.id.TextView_record_total_science2);
        TextView_time_foreign_language_tt = findViewById(R.id.TextView_record_total_foreignLanguage);
        TextView_title=findViewById(R.id.TextView_record_total_title);

        mDBHelper=new DBHelper_tt(Record_total_specific.this);
        db=mDBHelper.getReadableDatabase();

        Intent gintent=getIntent();
        totalId=gintent.getLongExtra("totalId", -1);

        String selectDB="SELECT time_korean, time_math, time_english, " +
                "time_history, time_science1, time_science2, time_foreign_language, title " +
                "FROM "+mDBHelper.TABLE_NAME+" WHERE id="+totalId;

        Cursor cursor=db.rawQuery(selectDB, null);
        cursor.moveToFirst();

        TextView_time_korean_tt.setText(cursor.getString(0));
        TextView_time_math_tt.setText(cursor.getString(1));
        TextView_time_english_tt.setText(cursor.getString(2));
        TextView_time_history_tt.setText(cursor.getString(3));
        TextView_time_science1_tt.setText(cursor.getString(4));
        TextView_time_science2_tt.setText(cursor.getString(5));
        TextView_time_foreign_language_tt.setText(cursor.getString(6));
        TextView_title.setText(cursor.getString(7));

        cursor.close();
    }
}
