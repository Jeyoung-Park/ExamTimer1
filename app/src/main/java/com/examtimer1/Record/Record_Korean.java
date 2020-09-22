package com.examtimer1.Record;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.examtimer1.DBHelper;
import com.examtimer1.MyAdapter;
import com.examtimer1.examtimer.R;
import com.examtimer1.TimerRecordItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class Record_Korean extends AppCompatActivity {
    private Context context;
    private SQLiteDatabase db;
    private DBHelper mDBHelper;

    private RecyclerView recyclerView;
    private ArrayList<TimerRecordItem> timerRecordItems = new ArrayList<>();
    RecyclerView.Adapter recyclerviewAdater;
    RecyclerView.LayoutManager layoutManager;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_korean);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        context = this.getBaseContext();
        recyclerView = findViewById(R.id.rv_record_Korean);
        recyclerView.setHasFixedSize(true);

        timerRecordItems.clear();
        mDBHelper = new DBHelper(Record_Korean.this, "TABLE_SUBJECT_KOREAN");
        db = mDBHelper.getReadableDatabase();

        Cursor cursor = mDBHelper.LoadSQLiteDBCursor();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                addItem(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));

        recyclerviewAdater = new MyAdapter(timerRecordItems, Record_Korean.this, "TABLE_SUBJECT_KOREAN");
        recyclerView.setAdapter(recyclerviewAdater);
    }

    public void addItem(Long id, String title, String time, String date) {
        TimerRecordItem item = new TimerRecordItem();
        item.setTitle(title);
        item.setTime(time);
        item.setDate(date);
        item.setId(id);
        timerRecordItems.add(item);
    }
}



