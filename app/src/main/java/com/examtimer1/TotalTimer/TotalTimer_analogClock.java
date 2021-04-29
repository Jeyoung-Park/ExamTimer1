package com.examtimer1.TotalTimer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.examtimer1.CustomAnalogClock;
import com.examtimer1.SubjectTimer.SubjectTimer_English;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.xmlpull.v1.XmlPullParser;

public class TotalTimer_analogClock extends AppCompatActivity {

    private TextView TextView_analogClock_subject_name;
    private Button btn_start_analogClock, btn_next_analogClock;
//    private int current_subject;
    private CustomAnalogClock analogClock;
    private int current_subject;
    private Thread thread;
    private boolean isThread=true;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock);

        this.getSupportActionBar().hide(); // 상단 바 숨기기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 꺼짐 방지

        btn_start_analogClock=findViewById(R.id.btn_start_analogClock);
        TextView_analogClock_subject_name=findViewById(R.id.TextView_analogClock_subject_name);
        analogClock=findViewById(R.id.analogClock);

        TextView_analogClock_subject_name.bringToFront();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(String.valueOf(R.string.front_ad_unit));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });

        btn_start_analogClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(analogClock.isStart()) btn_start_analogClock.setText("계속");
                else btn_start_analogClock.setText("일시정지");
                analogClock.setStart(!analogClock.isStart());
                analogClock.setCnt_btn_pressed(analogClock.getCnt_btn_pressed()+1);
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
                                current_subject=analogClock.getCurrent_subject();

                                if(current_subject==2) {
                                    TextView_analogClock_subject_name.setText("쉬는 시간 (20분)\n10:00~10:20\n(10:30에 시험 시작)");
                                }
                                else if(current_subject==3) TextView_analogClock_subject_name.setText("수학 (100분)\n10:30~12:10");
                                else if(current_subject==4) TextView_analogClock_subject_name.setText("점심 시간 (50분)\n12:10~13:00\n(13:10에 시험 시작)");
                                else if(current_subject==5) TextView_analogClock_subject_name.setText("영어(70분)\n13:10~14:20");
                                else if(current_subject==6) TextView_analogClock_subject_name.setText("쉬는 시간 (20분)\n14:20~14:40\n(14:50에 시험 시작)");
                                else if(current_subject==7) TextView_analogClock_subject_name.setText("한국사(30분)\n14:50~15:20");
                                else if(current_subject==8) TextView_analogClock_subject_name.setText("시험지 교체 시간 (15분)\n15:20~15:35");
                                else if(current_subject==9) TextView_analogClock_subject_name.setText("탐구 영역 1(30분)\n15:35~16:05");
                                else if(current_subject==10) TextView_analogClock_subject_name.setText("시험지 교체 시간 (2분)\n16:05~16:07");
                                else if(current_subject==11) TextView_analogClock_subject_name.setText("탐구 영역 2(30분)\n16:07~16:37");
                                else if(current_subject==12) TextView_analogClock_subject_name.setText("쉬는 시간 (18분)\n16:37~16:55\n(17:05에 시험 시작)");
                                else if(current_subject==13) TextView_analogClock_subject_name.setText("제2외국어/한문(40분)\n17:05~17:45");

                                if(analogClock.isTestEnd()) {
                                    Toast.makeText(TotalTimer_analogClock.this, "시험이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                                if(analogClock.isBreakTimeEnd()) {
                                    Toast.makeText(TotalTimer_analogClock.this, "쉬는 시간이 종료되었습니다.\n시험을 시작하세요.", Toast.LENGTH_SHORT).show();

                                }
                                if(analogClock.isExamEnd()) {
                                    Toast.makeText(TotalTimer_analogClock.this, "모든 시험이 종료되었습니다.\n뒤로 가기 버튼을 눌러 시험을 종료하세요", Toast.LENGTH_SHORT).show();
                                    isThread=true;
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
            TextView_analogClock_subject_name.setId(1002);
            TextView_analogClock_subject_name.setLayoutParams(layoutParams_title);

            ConstraintLayout.LayoutParams layoutParams_button=new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams_button.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button.topToBottom=1001;
            btn_start_analogClock.setId(1003);
            btn_start_analogClock.setLayoutParams(layoutParams_button);
        }
        else if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            ConstraintLayout.LayoutParams layoutParams_analogClock=new ConstraintLayout.LayoutParams(
                    height, height
            );
//            layoutParams_analogClock.matchConstraintPercentHeight=1f;
//            layoutParams_analogClock.dimensionRatio="1:1";
            layoutParams_analogClock.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_analogClock.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_analogClock.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;
            analogClock.setId(1001);
            analogClock.setLayoutParams(layoutParams_analogClock);

            ConstraintLayout.LayoutParams layoutParams_title=new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams_title.leftToRight=1001;
            layoutParams_title.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_title.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_title.topMargin=20;
            TextView_analogClock_subject_name.setId(1002);
            TextView_analogClock_subject_name.setLayoutParams(layoutParams_title);

            ConstraintLayout.LayoutParams layoutParams_button=new ConstraintLayout.LayoutParams(
                    0, ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams_button.leftToRight=1001;
            layoutParams_button.rightToRight=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams_button.bottomMargin=20;
            layoutParams_button.leftMargin=20;
            layoutParams_button.rightMargin=20;
            btn_start_analogClock.setId(1003);
            btn_start_analogClock.setLayoutParams(layoutParams_button);
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
                TotalTimer_analogClock.super.onBackPressed();//뒤로가기
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
