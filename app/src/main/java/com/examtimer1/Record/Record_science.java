package com.examtimer1.Record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Record_science extends AppCompatActivity {

    private Button btn_record_science1, btn_record_science2, btn_record_history, btn_record_science_total;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_science);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        btn_record_history=findViewById(R.id.btn_record_history);
        btn_record_science1=findViewById(R.id.btn_record_science1);
        btn_record_science2=findViewById(R.id.btn_record_science2);
        btn_record_science_total=findViewById(R.id.btn_record_science_total);

        btn_record_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Record_science.this, Record_history.class);
                startActivity(intent1);
            }
        });

        btn_record_science1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Record_science.this, Record_science1.class);
                startActivity(intent2);
            }
        });

        btn_record_science2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(Record_science.this, Record_science2.class);
                startActivity(intent3);
            }
        });

        btn_record_science_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4=new Intent(Record_science.this, Record_science_total.class);
                startActivity(intent4);
            }
        });
    }
}
