package com.examtimer1.TotalTimer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.examtimer1.DBHelper_tt;
import com.examtimer1.examtimer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TotalTimer_breaktime4 extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS=18*60*1000;

    private TextView TextView_time_breaktime4, TextView_info_message;
    private Button btn_start_pause, btn_reset, btn_stop_save, btn_totalTimer_save_end_nofl;
    private ImageButton btn_next_Korean, Imagebtn_info;
    private CountDownTimer mCountDownTimer;
    private boolean isTimerRunning, isInfoClicked;
    private long timeLeftInMillis=START_TIME_IN_MILLIS;

    private DBHelper_tt mDBHelper;
    private SQLiteDatabase db;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt_breaktime4);

        mInterstitialAd = new InterstitialAd(TotalTimer_breaktime4.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());//전면광고 로드

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        TextView_time_breaktime4=findViewById(R.id.TextView_time_breaktime4);
        btn_start_pause=findViewById(R.id.btn_start_pause_breaktime4);
        btn_stop_save=findViewById(R.id.btn_stop_save_breaktime4);
        btn_reset=findViewById(R.id.btn_reset_breaktime4);
        btn_next_Korean=findViewById(R.id.btn_next_breaktime4);
        btn_totalTimer_save_end_nofl=findViewById(R.id.btn_totalTimer_save_end_nofl);
        TextView_info_message=findViewById(R.id.TextView_info_message);
        Imagebtn_info=findViewById(R.id.Imagebtn_info);

        final Intent gintent=getIntent();

        isInfoClicked=false;
        Imagebtn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInfoClicked){
                    isInfoClicked=true;
                    TextView_info_message.setVisibility(View.VISIBLE);
                }
                else{
                    isInfoClicked=false;
                    TextView_info_message.setVisibility(View.INVISIBLE);
                }
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

        btn_next_Korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TotalTimer_breaktime4.this, TotalTimer_foreignLanguage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("time_science2", gintent.getExtras().getString("time_science2"));
                intent.putExtra("time_science1", gintent.getExtras().getString("time_science1"));
                intent.putExtra("time_history", gintent.getExtras().getString("time_history"));
                intent.putExtra("time_english", gintent.getExtras().getString("time_english"));
                intent.putExtra("time_math", gintent.getExtras().getString("time_math"));
                intent.putExtra("time_korean", gintent.getExtras().getString("time_korean"));
                startActivity(intent);
            }
        });

        btn_totalTimer_save_end_nofl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder saveBuilder = new AlertDialog.Builder(TotalTimer_breaktime4.this);
                saveBuilder.setMessage("타이머 기록의 제목을 입력해주세요");
                final EditText editText=new EditText(TotalTimer_breaktime4.this);
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

                        mDBHelper=new DBHelper_tt(TotalTimer_breaktime4.this);
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

                            contentValues.put(mDBHelper.TITLE, editText.getText().toString());
                            contentValues.put(mDBHelper.DATE, todayDate);
                            contentValues.put(mDBHelper.TIME_FOREIGN_LANGUAGE, "00:00");
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
                        Toast toast=Toast.makeText(TotalTimer_breaktime4.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        TotalTimer_breaktime4.super.onBackPressed();
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
//                btn_next_Korean.setVisibility(View.VISIBLE);
                Toast toast=Toast.makeText(TotalTimer_breaktime4.this, "쉬는 시간이 종료되었습니다.\n 화살표 버튼을 눌러 다음 시험에 응시하세요", Toast.LENGTH_SHORT);                toast.show();
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

        TextView_time_breaktime4.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                TotalTimer_breaktime4.super.onBackPressed();//뒤로가기
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
