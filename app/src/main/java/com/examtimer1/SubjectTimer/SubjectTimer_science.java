package com.examtimer1.SubjectTimer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
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

public class SubjectTimer_science extends AppCompatActivity {
    private long START_TIME_IN_MILLIS=102*60*1000;

    private android.widget.TextView TextView_time_science;
    private Button btn_start_pause, btn_reset, btn_stop_save, btn_save_end, btn_oneScience, btn_threeScience;
    private CountDownTimer mCountDownTimer;
    private boolean isTimerRunning, isSingleScience=false;
    private long timeLeftInMillis=START_TIME_IN_MILLIS;

    private SQLiteDatabase db;
    private DBHelper mDBHelper;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3081286779348377/7794370244");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        TextView_time_science=findViewById(R.id.TextView_time_science);
        btn_start_pause=findViewById(R.id.btn_start_pause_science);
        btn_stop_save=findViewById(R.id.btn_stop_save_science);
        btn_reset=findViewById(R.id.btn_reset_science);
        btn_save_end=findViewById(R.id.btn_save_end_science);
        btn_oneScience = findViewById(R.id.btn_oneScience);
        btn_threeScience=findViewById(R.id.btn_threeScience);

        isSingleScience=false;

        btn_oneScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                START_TIME_IN_MILLIS=30*60*1000;
                timeLeftInMillis=START_TIME_IN_MILLIS;
                updateCountDownText();
                isSingleScience=true;
            }
        });
        btn_threeScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                START_TIME_IN_MILLIS=102*60*1000;
                timeLeftInMillis=START_TIME_IN_MILLIS;
                updateCountDownText();
                isSingleScience=false;
            }
        });

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
                if(!isSingleScience){
                    final AlertDialog.Builder saveBuilder = new AlertDialog.Builder(SubjectTimer_science.this);
                    saveBuilder.setMessage("타이머 기록의 제목을 입력해주세요");
                    final EditText editText=new EditText(SubjectTimer_science.this);
                    editText.setTextColor(Color.parseColor("#000000"));
                    editText.setHint("10자까지 입력할 수 있습니다.");
                    editText.setFilters(new InputFilter[] {
                            new InputFilter.LengthFilter(10)
                    });
                    saveBuilder.setView(editText);
                    saveBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mDBHelper=new DBHelper(SubjectTimer_science.this, "TABLE_SUBJECT_SCIENCE_TOTAL");
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
                            Toast toast=Toast.makeText(SubjectTimer_science.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                            SubjectTimer_science.super.onBackPressed();
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
                else{
                    final String[] items=new String[]{"한국사", "탐구 1", "탐구 2"};
                    final int[] selectedItem={0};
                    final AlertDialog.Builder saveBuilder = new AlertDialog.Builder(SubjectTimer_science.this);
                    saveBuilder.setTitle("타이머 기록의 제목을 입력하고 저장 폴더를 지정해주세요");
                    final EditText editText=new EditText(SubjectTimer_science.this);
                    saveBuilder.setView(editText);
                    saveBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedItem[0]=which;
                        }
                    });
                    saveBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(selectedItem[0]==0) mDBHelper=new DBHelper(SubjectTimer_science.this, "TABLE_SUBJECT_HISTORY");
                            else if(selectedItem[0]==1) mDBHelper=new DBHelper(SubjectTimer_science.this, "TABLE_SUBJECT_SCIENCE1");
                            else if(selectedItem[0]==2) mDBHelper=new DBHelper(SubjectTimer_science.this, "TABLE_SUBJECT_SCIENCE2");
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
                            Toast toast=Toast.makeText(SubjectTimer_science.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                            SubjectTimer_science.super.onBackPressed();

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
                Toast toast=Toast.makeText(SubjectTimer_science.this, "시험이 종료되었습니다.", Toast.LENGTH_SHORT);
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

        TextView_time_science.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {
//
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                SubjectTimer_science.super.onBackPressed();//뒤로가기
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
