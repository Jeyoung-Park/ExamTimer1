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

public class Record_English extends AppCompatActivity {

    private Button btn_record_English_only_reading, btn_record_English_total;
    private AdView mAdView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_english);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        btn_record_English_only_reading=findViewById(R.id.btn_record_English_only_reading);
        btn_record_English_total=findViewById(R.id.btn_record_English_total);

        btn_record_English_only_reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Record_English.this, Record_English_only_reading.class);
                startActivity(intent1);
            }
        });

        btn_record_English_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Record_English.this, Record_English_total.class);
                startActivity(intent2);
            }
        });
    }
}
