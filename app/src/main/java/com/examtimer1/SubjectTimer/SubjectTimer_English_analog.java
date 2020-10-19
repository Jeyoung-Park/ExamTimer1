package com.examtimer1.SubjectTimer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.examtimer1.CustomAnalogClock_English;
import com.examtimer1.MainActivity;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SubjectTimer_English_analog extends AppCompatActivity {

    private TextView TextView_analogClock;
    private Button btn_start_analogClock;
    private ImageButton btn_toDigitalMode;
    private CustomAnalogClock_English analogClock;
    private Thread thread;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock_english);

        this.getSupportActionBar().hide(); // 상단 바 숨기기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 꺼짐 방지

        TextView_analogClock=findViewById(R.id.TextView_analogClock_English);
        btn_start_analogClock=findViewById(R.id.btn_start_analogClock_English);
        analogClock=findViewById(R.id.analogClock_English);
        btn_toDigitalMode=findViewById(R.id.btn_toDigitalMode_English);

        TextView_analogClock.bringToFront(); //레이아웃을 맨 앞으로

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3081286779348377/7794370244");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                Log.d("Tag_Ad", "광고 로드 실패 / 에러코드:"+i);
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                Log.d("Tag_Ad", "광고 로드 완료");
                super.onAdLoaded();
            }
        });

        btn_start_analogClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(analogClock.isStart()) btn_start_analogClock.setText("계속");
                else btn_start_analogClock.setText("일시정지");
                analogClock.setStart(!analogClock.isStart());
            }
        });

        btn_toDigitalMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SubjectTimer_English_analog.this);
                builder.setMessage("타이머가 진행 중인 상황에서 디지털 시계 모드로 변환할 시 현재 타이머 기록이 초기화됩니다.\n디지털 시계 모드로 전환하시겠습니까?");
                builder.setPositiveButton("디지털 시계 모드로 전환", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(SubjectTimer_English_analog.this, SubjectTimer_English.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        thread=new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run()
                            {
                                if(analogClock.isExamEnd()) {
                                    Toast.makeText(SubjectTimer_English_analog.this, "모든 시험이 종료되었습니다.\n뒤로 가기 버튼을 눌러 시험을 종료하세요", Toast.LENGTH_SHORT).show();
                                    analogClock.setStart(false);
                                    btn_start_analogClock.setEnabled(false);
                                }
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
            }
        });
        thread.start();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){

            ConstraintLayout.LayoutParams layoutParams_analogClock=new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT, 0
            );
            layoutParams_analogClock.dimensionRatio="1:1";
            layoutParams_analogClock.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_analogClock.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_analogClock.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_analogClock.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            analogClock.setId(1001);
            analogClock.setLayoutParams(layoutParams_analogClock);

            ConstraintLayout.LayoutParams layoutParams_title=new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams_title.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_title.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_title.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_title.bottomToTop=1001;
            TextView_analogClock.setId(1002);
            TextView_analogClock.setLayoutParams(layoutParams_title);

            ConstraintLayout.LayoutParams layoutParams_button_start=new ConstraintLayout.LayoutParams(
                    0, ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams_button_start.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_start.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_start.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_start.topToBottom=1001;
            layoutParams_button_start.leftMargin=100;
            layoutParams_button_start.rightMargin=100;
            btn_start_analogClock.setId(1003);
            btn_start_analogClock.setLayoutParams(layoutParams_button_start);

            ConstraintLayout.LayoutParams layoutParams_button_toDigital=new ConstraintLayout.LayoutParams(
                    250, 250
            );
            layoutParams_button_toDigital.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_toDigital.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_toDigital.topMargin=20;
            layoutParams_button_toDigital.rightMargin=20;
            btn_toDigitalMode.setId(1004);
            btn_toDigitalMode.setLayoutParams(layoutParams_button_toDigital);
        }
        else if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            ConstraintLayout.LayoutParams layoutParams_analogClock=new ConstraintLayout.LayoutParams(
                    height, height
            );
            layoutParams_analogClock.dimensionRatio="1:1";
            layoutParams_analogClock.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_analogClock.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_analogClock.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;
            analogClock.setId(1001);
            analogClock.setLayoutParams(layoutParams_analogClock);

            ConstraintLayout.LayoutParams layoutParams_title=new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams_title.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_title.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_title.leftToRight=1001;
            layoutParams_title.topMargin=100;
            TextView_analogClock.setId(1002);
            TextView_analogClock.setLayoutParams(layoutParams_title);

            ConstraintLayout.LayoutParams layoutParams_button_start=new ConstraintLayout.LayoutParams(
                    0, ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams_button_start.leftToRight=1001;
            layoutParams_button_start.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_start.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_start.rightMargin=100;
            layoutParams_button_start.leftMargin=100;
            layoutParams_button_start.bottomMargin=100;
            btn_start_analogClock.setId(1003);
            btn_start_analogClock.setLayoutParams(layoutParams_button_start);

            ConstraintLayout.LayoutParams layoutParams_button_toDigital=new ConstraintLayout.LayoutParams(
                    250, 250
            );
            layoutParams_button_toDigital.dimensionRatio="1:1";
            layoutParams_button_toDigital.leftToRight=1001;
            layoutParams_button_toDigital.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_toDigital.bottomToTop=1003;
            layoutParams_button_toDigital.topToBottom=1002;
            btn_toDigitalMode.setId(1004);
            btn_toDigitalMode.setLayoutParams(layoutParams_button_toDigital);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent tempIntent=new Intent(SubjectTimer_English_analog.this, MainActivity.class);
                tempIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tempIntent);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
