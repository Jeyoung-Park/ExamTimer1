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

import com.examtimer1.CustomAnalogClock_science2;
import com.examtimer1.MainActivity;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SubjectTimer_science2_analog extends AppCompatActivity {

    private TextView TextView_analogClock;
    private Button btn_start_analogClock;
    private ImageButton btn_toDigitalMode, btn_settings;
    private CustomAnalogClock_science2 analogClock;
    private Thread thread;
    private int choice;
    private InterstitialAd mInterstitialAd;
    private boolean isThread=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock_science2);

        this.getSupportActionBar().hide(); // 상단 바 숨기기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 꺼짐 방지

        TextView_analogClock=findViewById(R.id.TextView_analogClock_science2);
        btn_start_analogClock=findViewById(R.id.btn_start_analogClock_science2);
        analogClock=findViewById(R.id.analogClock_science2);
        btn_toDigitalMode=findViewById(R.id.btn_toDigitalMode_science2);
        btn_settings=findViewById(R.id.btn_science2_settings);

        TextView_analogClock.bringToFront(); //레이아웃을 맨 앞으로

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.front_ad_unit));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

        btn_start_analogClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(analogClock.isStart()) btn_start_analogClock.setText("계속");
                else btn_start_analogClock.setText("일시정지");
                analogClock.setStart(!analogClock.isStart());
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] science_subjects={"탐구 영역 전체", "한국사", "탐구 1", "탐구 2"};
                choice=3;

                AlertDialog.Builder builder=new AlertDialog.Builder(SubjectTimer_science2_analog.this);
                builder.setTitle("과목 설정")
                        .setSingleChoiceItems(science_subjects, 3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                choice=which;
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent;
                                if(choice==1) intent=new Intent(SubjectTimer_science2_analog.this, SubjectTimer_history_analog.class);
                                else if(choice==2) intent=new Intent(SubjectTimer_science2_analog.this, SubjectTimer_science1_analog.class);
                                else intent=new Intent(SubjectTimer_science2_analog.this, SubjectTimer_science_analog.class);
                                if(choice!=3)   startActivity(intent);
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        btn_toDigitalMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SubjectTimer_science2_analog.this);
                builder.setMessage(getString(R.string.explain_analongToDigital));
                builder.setPositiveButton("디지털 시계 모드로 전환", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(SubjectTimer_science2_analog.this, SubjectTimer_science.class);
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
                while (isThread)
                    try
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run()
                            {
                                if(analogClock.isExamEnd()) {
                                    Toast.makeText(SubjectTimer_science2_analog.this, "모든 시험이 종료되었습니다.\n뒤로 가기 버튼을 눌러 시험을 종료하세요", Toast.LENGTH_SHORT).show();
                                    isThread=false;
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

            ConstraintLayout.LayoutParams layoutParams_btn_setting=new ConstraintLayout.LayoutParams(
                    250, 250
            );
            layoutParams_btn_setting.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_btn_setting.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_btn_setting.topMargin=20;
            layoutParams_btn_setting.leftMargin=20;
            btn_settings.setId(1005);
            btn_settings.setLayoutParams(layoutParams_btn_setting);
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
            layoutParams_button_toDigital.leftToRight=1005;
            layoutParams_button_toDigital.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button_toDigital.bottomToTop=1003;
            layoutParams_button_toDigital.topToBottom=1002;
            btn_toDigitalMode.setId(1004);
            btn_toDigitalMode.setLayoutParams(layoutParams_button_toDigital);

            ConstraintLayout.LayoutParams layoutParams_btn_setting=new ConstraintLayout.LayoutParams(
                    250, 250
            );
            layoutParams_btn_setting.leftToRight=1001;
            layoutParams_btn_setting.rightToLeft=1004;
            layoutParams_btn_setting.topToBottom=1002;
            layoutParams_btn_setting.bottomToTop=1003;
            btn_settings.setId(1005);
            btn_settings.setLayoutParams(layoutParams_btn_setting);
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
                isThread=false;
                Intent tempIntent=new Intent(SubjectTimer_science2_analog.this, MainActivity.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isThread=false;
    }
}
