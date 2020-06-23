package com.examtimer1.SubjectTimer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.examtimer1.DBHelper;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SubjectTimer_foreignLanguage extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS=40*60*1000;

    private android.widget.TextView TextView_time_fl;
    private Button btn_start_pause, btn_reset, btn_stop_save, btn_save_end;
    private CountDownTimer mCountDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis=START_TIME_IN_MILLIS;

    private SQLiteDatabase db;
    private DBHelper mDBHelper;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreignlanguage);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3081286779348377/7794370244");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        TextView_time_fl=findViewById(R.id.TextView_time_foreignLanguage);
        btn_start_pause=findViewById(R.id.btn_start_pause_foreignLanguage);
        btn_stop_save=findViewById(R.id.btn_stop_save_foreignLanguage);
        btn_reset=findViewById(R.id.btn_reset_foreignLanguage);
        btn_save_end=findViewById(R.id.btn_save_end_foreignLanguage);

        btn_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                btn_save_end.setVisibility(View.VISIBLE);
            }
        });

        btn_save_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder saveBuilder = new AlertDialog.Builder(SubjectTimer_foreignLanguage.this);
                saveBuilder.setMessage("타이머 기록의 제목을 입력해주세요");
                final EditText editText=new EditText(SubjectTimer_foreignLanguage.this);
                editText.setTextColor(Color.parseColor("#000000"));
                saveBuilder.setView(editText);
                saveBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDBHelper=new DBHelper(SubjectTimer_foreignLanguage.this, "TABLE_SUBJECT_FL");
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

                        dialog.cancel();
                        Toast toast=Toast.makeText(SubjectTimer_foreignLanguage.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        SubjectTimer_foreignLanguage.super.onBackPressed();
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
                alertDialog.show();            }
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
                Toast toast=Toast.makeText(SubjectTimer_foreignLanguage.this, "시험이 종료되었습니다.", Toast.LENGTH_SHORT);
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

        TextView_time_fl.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                SubjectTimer_foreignLanguage.super.onBackPressed();//뒤로가기
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
