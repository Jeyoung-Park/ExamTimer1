package com.examtimer1.SubjectTimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.examtimer1.CustomAnalogClock_science1;
import com.examtimer1.MainActivity;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SubjectTimer_science1_analog extends AppCompatActivity {

    private TextView TextView_analogClock;
    private Button btn_start_analogClock;
    private ImageButton btn_toDigitalMode, btn_settings;
    private CustomAnalogClock_science1 analogClock;
    private Thread thread;
    private int choice;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock_science1);

        this.getSupportActionBar().hide(); // 상단 바 숨기기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 꺼짐 방지


        TextView_analogClock=findViewById(R.id.TextView_analogClock_science1);
        btn_start_analogClock=findViewById(R.id.btn_start_analogClock_science1);
        analogClock=findViewById(R.id.analogClock_science1);
        btn_toDigitalMode=findViewById(R.id.btn_toDigitalMode_science1);
        btn_settings=findViewById(R.id.btn_science1_settings);

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

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] science_subjects={"탐구 영역 전체", "한국사", "탐구 1", "탐구 2"};
                choice=2;

                AlertDialog.Builder builder=new AlertDialog.Builder(SubjectTimer_science1_analog.this);
                builder.setTitle("과목 설정")
                        .setSingleChoiceItems(science_subjects, 2, new DialogInterface.OnClickListener() {
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
                                if(choice==1) intent=new Intent(SubjectTimer_science1_analog.this, SubjectTimer_history_analog.class);
                                else if(choice==0) intent=new Intent(SubjectTimer_science1_analog.this, SubjectTimer_science_analog.class);
                                else intent=new Intent(SubjectTimer_science1_analog.this, SubjectTimer_science2_analog.class);
                                if(choice!=2)   startActivity(intent);
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        btn_toDigitalMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SubjectTimer_science1_analog.this);
                builder.setMessage("타이머가 진행 중인 상황에서 디지털 시계 모드로 변환할 시 현재 타이머 기록이 초기화됩니다.\n디지털 시계 모드로 전환하시겠습니까?");
                builder.setPositiveButton("디지털 시계 모드로 전환", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(SubjectTimer_science1_analog.this, SubjectTimer_science.class);
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
                                    Toast.makeText(SubjectTimer_science1_analog.this, "모든 시험이 종료되었습니다.\n뒤로 가기 버튼을 눌러 시험을 종료하세요", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent tempIntent=new Intent(SubjectTimer_science1_analog.this, MainActivity.class);
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
