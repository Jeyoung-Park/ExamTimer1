package com.examtimer1.TotalTimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.examtimer1.examtimer.R;

import java.util.Locale;

public class TotalTimer_history extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS=30*60*1000;

    private TextView TextView_time_history_tt;
    private Button btn_start_pause, btn_reset, btn_stop_save;
    private ImageButton btn_next_Korean;
    private CountDownTimer mCountDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis=START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_tt);

        this.getSupportActionBar().hide(); // 상단 바 숨기기

        TextView_time_history_tt=findViewById(R.id.TextView_time_history_tt);
        btn_start_pause=findViewById(R.id.btn_start_pause_tt_history);
        btn_stop_save=findViewById(R.id.btn_stop_save_tt_history);
        btn_reset=findViewById(R.id.btn_reset_tt_history);
        btn_next_Korean=findViewById(R.id.btn_next_history);

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
                btn_next_Korean.setVisibility(View.INVISIBLE);
            }
        });

        btn_stop_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimerRunning) {
                    mCountDownTimer.cancel();
                    mCountDownTimer.onFinish();
                }
                btn_next_Korean.setVisibility(View.VISIBLE);
            }
        });

        btn_next_Korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TotalTimer_history.this, TotalTimer_breaktime_after_history.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                long timeSpentInMillis=START_TIME_IN_MILLIS-timeLeftInMillis;
                int spentMinutes=(int)(timeSpentInMillis/1000)/60;
                int spentSeconds=(int)(timeSpentInMillis/1000)%60;
                String spentTime=String.format(Locale.getDefault(), "%02d:%02d", spentMinutes, spentSeconds);
                intent.putExtra("time_history", spentTime);
                intent.putExtra("time_english", gintent.getExtras().getString("time_english"));
                intent.putExtra("time_math", gintent.getExtras().getString("time_math"));
                intent.putExtra("time_korean", gintent.getExtras().getString("time_korean"));
                startActivity(intent);
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
                btn_next_Korean.setVisibility(View.VISIBLE);
                Toast toast=Toast.makeText(TotalTimer_history.this, "시험이 종료되었습니다.\n화살표 버튼을 눌러 다음 과목으로 이동하세요", Toast.LENGTH_SHORT);
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

        TextView_time_history_tt.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                TotalTimer_history.super.onBackPressed();//뒤로가기
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
