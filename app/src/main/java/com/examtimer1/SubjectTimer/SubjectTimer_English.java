package com.examtimer1.SubjectTimer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.examtimer1.CustomAnalogClock;
import com.examtimer1.DBHelper;
import com.examtimer1.MainActivity;
import com.examtimer1.TotalTimer.TotalTimer_breaktime_after_history;
import com.examtimer1.TotalTimer.TotalTimer_foreignLanguage;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SubjectTimer_English extends AppCompatActivity {
    private long START_TIME_IN_MILLIS=70*60*1000;

    private android.widget.TextView TextView_time_English;
    private Button btn_start_pause, btn_reset, btn_stop_save, btn_save_end, btn_onlyReading, btn_wholeEnglish;
    private CountDownTimer mCountDownTimer;
    private boolean isTimerRunning, isOnlyReading;
    private long timeLeftInMillis=START_TIME_IN_MILLIS;
    private ImageButton btn_toClockMode;

    private SQLiteDatabase db;
    private DBHelper mDBHelper;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english);

        this.getSupportActionBar().hide(); // 상단 바 숨기기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 꺼짐 방지

        TextView_time_English=findViewById(R.id.TextView_time_English);
        btn_start_pause=findViewById(R.id.btn_start_pause_English);
        btn_stop_save=findViewById(R.id.btn_stop_save_English);
        btn_reset=findViewById(R.id.btn_reset_English);
        btn_save_end=findViewById(R.id.btn_save_end_English);
        btn_onlyReading=findViewById(R.id.btn_onlyReading);
        btn_wholeEnglish=findViewById(R.id.btn_wholeEnglish);
        btn_toClockMode=findViewById(R.id.btn_English_toClockMode);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.front_ad_unit));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

//        btn_stop_save.setEnabled(false);

        btn_toClockMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SubjectTimer_English.this);
                builder.setMessage(getString(R.string.explain_digitalToAnalog));
                builder.setPositiveButton("아날로그 시계모드로 전환", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(SubjectTimer_English.this, SubjectTimer_English_analog.class);
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

        isOnlyReading=false;

        btn_onlyReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnlyReading=true;
                START_TIME_IN_MILLIS=45*60*1000;
                timeLeftInMillis=START_TIME_IN_MILLIS;
                updateCountDownText();
            }
        });
        btn_wholeEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnlyReading=false;
                START_TIME_IN_MILLIS=70*60*1000;
                timeLeftInMillis=START_TIME_IN_MILLIS;
                updateCountDownText();
            }
        });

        btn_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btn_stop_save.setEnabled(true);
                if(isTimerRunning) {
                    pauseTimer();
                    btn_start_pause.setText("계속");
                }
                else {
                    startTimer();
                    btn_start_pause.setText("일시정지");
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
//                btn_stop_save.setEnabled(false);
                btn_start_pause.setText("시작");
                btn_save_end.setVisibility(View.INVISIBLE);
            }
        });

        btn_stop_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimerRunning) {
                    mCountDownTimer.cancel();
                    mCountDownTimer.onFinish();
                }
//                if(isTimerRunning) {
//                    pauseTimer();
//                }
                btn_save_end.setVisibility(View.VISIBLE);
            }
        });

        btn_save_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder saveBuilder = new AlertDialog.Builder(SubjectTimer_English.this);
                saveBuilder.setMessage("타이머 기록의 제목을 입력해주세요");
                final EditText editText=new EditText(SubjectTimer_English.this);
                editText.setTextColor(Color.parseColor("#000000"));
                editText.setHint("10자까지 입력할 수 있습니다.");
                editText.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(10)
                });
                saveBuilder.setView(editText);
                saveBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(isOnlyReading){
                            mDBHelper=new DBHelper(SubjectTimer_English.this, "TABLE_SUBJECT_ENGLISH_ONLY_READING");
                            db=mDBHelper.getWritableDatabase();
                            mDBHelper.onCreate(db);
                            db.beginTransaction();

                            try{
                                ContentValues contentValues=new ContentValues();
//                        오늘 날짜 측정
                                Date currentTime= Calendar.getInstance().getTime();
                                SimpleDateFormat dayFormat=new SimpleDateFormat("dd", Locale.getDefault());
                                SimpleDateFormat monthFormat=new SimpleDateFormat("MM", Locale.getDefault());
                                SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy", Locale.getDefault());
                                String myear=yearFormat.format(currentTime);
                                String mmonth=monthFormat.format(currentTime);
                                String mday=dayFormat.format(currentTime);
                                String todayDate=myear+"-"+mmonth+"-"+mday;

//                        남은 시간 측정
                                long timeSpentInMillis=START_TIME_IN_MILLIS-timeLeftInMillis;
                                int spentMinutes=(int)(timeSpentInMillis/1000)/60;
                                int spentSeconds=(int)(timeSpentInMillis/1000)%60;
                                String spentTime=String.format(Locale.getDefault(), "%02d:%02d", spentMinutes, spentSeconds);

                                contentValues.put(mDBHelper.TITLE, editText.getText().toString());
                                contentValues.put(mDBHelper.DATE, todayDate);
                                contentValues.put(mDBHelper.TIME, spentTime);

                                db.insert(mDBHelper.TABLE_NAME, null, contentValues);

                                db.setTransactionSuccessful();
                            }catch(Exception e){
                                e.printStackTrace();
                            }finally{
                                db.endTransaction();
                            }
                        }
                        else{
                            mDBHelper=new DBHelper(SubjectTimer_English.this, "TABLE_SUBJECT_ENGLISH_TOTAL");
                            db=mDBHelper.getWritableDatabase();
                            mDBHelper.onCreate(db);
                            db.beginTransaction();

                            try{
                                ContentValues contentValues=new ContentValues();
//                        오늘 날짜 측정
                                Date currentTime= Calendar.getInstance().getTime();
                                SimpleDateFormat dayFormat=new SimpleDateFormat("dd", Locale.getDefault());
                                SimpleDateFormat monthFormat=new SimpleDateFormat("MM", Locale.getDefault());
                                SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy", Locale.getDefault());
                                String myear=yearFormat.format(currentTime);
                                String mmonth=monthFormat.format(currentTime);
                                String mday=dayFormat.format(currentTime);
                                String todayDate=myear+"-"+mmonth+"-"+mday;

//                        남은 시간 측정
                                long timeSpentInMillis=START_TIME_IN_MILLIS-timeLeftInMillis;
                                int spentMinutes=(int)(timeSpentInMillis/1000)/60;
                                int spentSeconds=(int)(timeSpentInMillis/1000)%60;
                                String spentTime=String.format(Locale.getDefault(), "%02d:%02d", spentMinutes, spentSeconds);

                                contentValues.put(mDBHelper.TITLE, editText.getText().toString());
                                contentValues.put(mDBHelper.DATE, todayDate);
                                contentValues.put(mDBHelper.TIME, spentTime);

                                db.insert(mDBHelper.TABLE_NAME, null, contentValues);

                                db.setTransactionSuccessful();
                            }catch(Exception e){
                                e.printStackTrace();
                            }finally{
                                db.endTransaction();
                            }
                        }

                        dialog.cancel();
                        Toast toast=Toast.makeText(SubjectTimer_English.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        SubjectTimer_English.super.onBackPressed();
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                });
                saveBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=saveBuilder.create();
                alertDialog.show();

            }
        });

        updateCountDownText();
    }

    private void startTimer(){
        mCountDownTimer=new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning=false;
                btn_start_pause.setText("시작");
                btn_save_end.setVisibility(View.VISIBLE);
                Toast toast=Toast.makeText(SubjectTimer_English.this, "시험이 종료되었습니다.\n화살표 버튼을 눌러 다음 과목으로 이동하세요", Toast.LENGTH_SHORT);
                toast.show();
            }
        }.start();
        isTimerRunning=true;
    }
    private void pauseTimer(){
        mCountDownTimer.cancel();
        isTimerRunning=false;
    }
    private void resetTimer(){
        timeLeftInMillis=START_TIME_IN_MILLIS;
        updateCountDownText();
        if(isTimerRunning) pauseTimer();
    }
    private void updateCountDownText(){
        int minutes=(int)(timeLeftInMillis/1000)/60;
        int seconds=(int)(timeLeftInMillis/1000)%60;

        String timeLeftFormatted=String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        TextView_time_English.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent tempIntent=new Intent(SubjectTimer_English.this, MainActivity.class);
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
