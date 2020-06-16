package com.examtimer1.Record;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.examtimer1.DBHelper_tt;
import com.examtimer1.MyAdapter_tt;
import com.examtimer1.examtimer.R;

import com.examtimer1.TimerRecordItem_tt;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class Record_total extends AppCompatActivity {

    private Context mContext;
    private SQLiteDatabase db;
    private DBHelper_tt mDBHelper;

    private RecyclerView recyclerView;
    private ArrayList<TimerRecordItem_tt> timerRecordItems = new ArrayList<>();
    MyAdapter_tt recyclerviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_total);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        mContext = this.getBaseContext();
        recyclerView = findViewById(R.id.rv_record_total);
        recyclerView.setHasFixedSize(true);

        timerRecordItems.clear();
        mDBHelper = new DBHelper_tt(Record_total.this);
        db = mDBHelper.getReadableDatabase();

        Cursor cursor = mDBHelper.LoadSQLiteDBCursor();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                addItem(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));

        recyclerviewAdapter = new MyAdapter_tt(timerRecordItems, Record_total.this);

        recyclerviewAdapter.setOnClickListener(
                new MyAdapter_tt.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent=new Intent(Record_total.this, Record_total_specific.class);
                        intent.putExtra("totalId", timerRecordItems.get(pos).getId());
                        startActivity(intent);
                    }
                }
        );

        recyclerView.setAdapter(recyclerviewAdapter);
    }

    public void addItem(Long id, String title, String date) {
        TimerRecordItem_tt item = new TimerRecordItem_tt();
        item.setTitle(title);
//        item.setTime(time);
        item.setDate(date);
        item.setId(id);
        timerRecordItems.add(item);

    }
}
