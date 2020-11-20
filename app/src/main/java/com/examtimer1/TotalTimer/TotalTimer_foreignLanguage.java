package com.examtimer1.TotalTimer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.examtimer1.DBHelper_tt;
import com.examtimer1.MainActivity;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TotalTimer_foreignLanguage extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS=40*60*1000;

    private TextView TextView_time_foreignLanguage_tt;
    private Button btn_start_pause, btn_reset, btn_stop_save, btn_save_end_foreignLanguage;
    private CountDownTimer mCountDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis=START_TIME_IN_MILLIS;

    private DBHelper_tt mDBHelper;
    private SQLiteDatabase db;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreignlanguage_tt);

        mInterstitialAd = new InterstitialAd(TotalTimer_foreignLanguage.this);
        mInterstitialAd.setAdUnitId(String.valueOf(R.string.front_ad_unit));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

        this.getSupportActionBar().hide(); // 상단 바 숨기기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 꺼짐 방지


        TextView_time_foreignLanguage_tt=findViewById(R.id.TextView_time_foreignLanguage_tt);
        btn_start_pause=findViewById(R.id.btn_start_pause_foreignLanguage_tt);
        btn_stop_save=findViewById(R.id.btn_stop_save_foreignLanguage_tt);
        btn_reset=findViewById(R.id.btn_reset_foreignLanguage_tt);
        btn_save_end_foreignLanguage=findViewById(R.id.btn_save_end_foreignLanguage_tt);

        final Intent gintent=getIntent();

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
            }
        });

        btn_stop_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimerRunning) {
                    mCountDownTimer.cancel();
                    mCountDownTimer.onFinish();
                }
            }
        });

        btn_save_end_foreignLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder saveBuilder = new AlertDialog.Builder(TotalTimer_foreignLanguage.this);
                saveBuilder.setMessage("타이머 기록의 제목을 입력해주세요");
                final EditText editText=new EditText(TotalTimer_foreignLanguage.this);
                editText.setTextColor(Color.parseColor("#000000"));
                editText.setHint("10자까지 입력할 수 있습니다.");
                editText.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(10)
                });
                saveBuilder.setView(editText);
                saveBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        mDBHelper=new DBHelper_tt(TotalTimer_foreignLanguage.this);
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
                            contentValues.put(mDBHelper.TIME_FOREIGN_LANGUAGE, spentTime);
                            contentValues.put(mDBHelper.TIME_KOREAN, gintent.getExtras().getString("time_korean"));
                            contentValues.put(mDBHelper.TIME_MATH, gintent.getExtras().getString("time_math"));
                            contentValues.put(mDBHelper.TIME_ENGLISH, gintent.getExtras().getString("time_english"));
                            contentValues.put(mDBHelper.TIME_HISTORY, gintent.getExtras().getString("time_history"));
                            contentValues.put(mDBHelper.TIME_SCIENCE1, gintent.getExtras().getString("time_science1"));
                            contentValues.put(mDBHelper.TIME_SCIENCE2, gintent.getExtras().getString("time_science2"));


                            db.insert(mDBHelper.TABLE_NAME, null, contentValues);

                            db.setTransactionSuccessful();
                        }catch(Exception e){
                            e.printStackTrace();
                        }finally{
                            db.endTransaction();
                        }

                        Toast toast=Toast.makeText(TotalTimer_foreignLanguage.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent tempIntent=new Intent(TotalTimer_foreignLanguage.this, MainActivity.class);
                        tempIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(tempIntent);
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
                btn_save_end_foreignLanguage.setVisibility(View.VISIBLE);
                Toast toast=Toast.makeText(TotalTimer_foreignLanguage.this, "모든 시험이 종료되었습니다.\n저장&종료 버튼을 눌러 기록을 저장하세요.", Toast.LENGTH_SHORT);
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

        TextView_time_foreignLanguage_tt.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent tempIntent=new Intent(TotalTimer_foreignLanguage.this, MainActivity.class);
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
