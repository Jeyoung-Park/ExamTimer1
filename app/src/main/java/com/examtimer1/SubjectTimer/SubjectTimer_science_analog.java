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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.examtimer1.CustomAnalogClock_science;
import com.examtimer1.examtimer.R;

public class SubjectTimer_science_analog extends AppCompatActivity {

    private TextView TextView_analogClock_subject_name;
    private Button btn_start_analogClock, btn_next_analogClock;
//    private CustomAnalogClock customAnalogClock;
//    private int current_subject;
    private CustomAnalogClock_science analogClock;
    private int current_subject;
    private Thread thread;
    private ImageButton  btn_toDigitalMode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock_science);
        
        this.getSupportActionBar().hide(); // 상단 바 숨기기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 꺼짐 방지

//
//        XmlPullParser parser = .getXml(R.layout.activity_analog_clock);
//        AttributeSet attributes = Xml.asAttributeSet(parser);

//        current_subject=1;

//        customAnalogClock.setStart_hour(8);
//        customAnalogClock.setStart_minute(40);

        btn_start_analogClock=findViewById(R.id.btn_start_analogClock_science);
//        btn_next_analogClock=findViewById(R.id.btn_next_analogClock);
        TextView_analogClock_subject_name=findViewById(R.id.TextView_analogClock_science);
        analogClock=findViewById(R.id.analogClock_science);
        btn_toDigitalMode=findViewById(R.id.btn_toDigitalMode_science);

        TextView_analogClock_subject_name.bringToFront(); //레이아웃을 맨 앞으로

        /*if(!analogClock.isStart()&&analogClock.getCnt_btn_pressed()>=1){
            btn_start_analogClock.setText("계속");
        }
        else if(analogClock.isStart()&&analogClock.getCnt_btn_pressed()>=1){
            btn_start_analogClock.setText("일시정지");
        }*/

        btn_toDigitalMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SubjectTimer_science_analog.this);
                builder.setMessage("타이머가 진행 중인 상황에서 디지털 시계 모드로 변환할 시 현재 타이머 기록이 초기화됩니다.\n디지털 시계 모드로 전환하시겠습니까?");
                builder.setPositiveButton("디지털 시계 모드로 전환", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(SubjectTimer_science_analog.this, SubjectTimer_science.class);
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
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run()
                            {
                                current_subject=analogClock.getCurrent_subject();
                                Log.d("thread", "thread 실행중");
                                Log.d("thread", "current_subject="+current_subject);

                                if(current_subject==2) TextView_analogClock_subject_name.setText("시험지 교체 시간 (10분)\n15:20~15:30");
                                else if(current_subject==3) TextView_analogClock_subject_name.setText("탐구 영역 1(30분)\n15:30~16:00");
                                else if(current_subject==4) TextView_analogClock_subject_name.setText("시험지 교체 시간 (2분)\n16:00~16:02");
                                else if(current_subject==5) TextView_analogClock_subject_name.setText("탐구 영역 2(30분)\n16:02~16:32");

                                if(analogClock.isTestEnd()) {
                                    Toast.makeText(SubjectTimer_science_analog.this, "시험이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                    Log.d("tag_time", "isTestEnd");
                                }
                                if(analogClock.isBreakTimeEnd()) {
                                    Toast.makeText(SubjectTimer_science_analog.this, "쉬는 시간이 종료되었습니다.\n시험을 시작하세요.", Toast.LENGTH_SHORT).show();
                                    Log.d("tag_time", "isBreakTimeEnd");

                                }
                                if(analogClock.isExamEnd()) {
                                    Toast.makeText(SubjectTimer_science_analog.this, "모든 시험이 종료되었습니다.\n뒤로 가기 버튼을 눌러 시험을 종료하세요", Toast.LENGTH_SHORT).show();
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
  /*      Handler mHandler =new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        current_subject=analogClock.getCurrent_subject();
                        Thread.sleep(1000);
                        Log.d("thread", "thread 실행중");
                        Log.d("thread", "current_subject="+current_subject);

                        if(current_subject==2) {
                            TextView_analogClock_subject_name.setText("쉬는 시간 (20분)\n10:00~10:20\n(10:30에 시험 시작)");
                            Log.d("tag", "두 번째 과목 돌입");
                        }
                        else if(current_subject==3) TextView_analogClock_subject_name.setText("수학 (100분)\n10:30~12:10");
                        else if(current_subject==4) TextView_analogClock_subject_name.setText("점심 시간 (50분)\n12:10~13:00\n(13:10에 시험 시작)");
                        else if(current_subject==5) TextView_analogClock_subject_name.setText("영어(70분)\n13:10~14:20");
                        else if(current_subject==6) TextView_analogClock_subject_name.setText("쉬는 시간 (20분)\n14:20~14:40\n(14:50에 시험 시작)");
                        else if(current_subject==7) TextView_analogClock_subject_name.setText("한국사(30분)\n14:50~15:20");
                        else if(current_subject==8) TextView_analogClock_subject_name.setText("시험지 교체 시간 (10분)\n15:20~15:30");
                        else if(current_subject==9) TextView_analogClock_subject_name.setText("탐구 영역 1(30분)\n15:30~16:00");
                        else if(current_subject==10) TextView_analogClock_subject_name.setText("시험지 교체 시간 (2분)\n16:00~16:02");
                        else if(current_subject==11) TextView_analogClock_subject_name.setText("탐구 영역 2(30분)\n16:02~16:32");
                        else if(current_subject==12) TextView_analogClock_subject_name.setText("쉬는 시간 (18분)\n16:32~16:50\n(17:00에 시험 시작)");
                        else if(current_subject==13) TextView_analogClock_subject_name.setText("제2외국어/한문(40분)\n17:00~17:40");

                        if(analogClock.isTestEnd()) {
                            Toast.makeText(TotalTimer_analogClock.this, "시험이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("tag_time", "isTestEnd");
                        }
                        if(analogClock.isBreakTimeEnd()) {
                            Toast.makeText(TotalTimer_analogClock.this, "쉬는 시간이 종료되었습니다.\n시험을 시작하세요.", Toast.LENGTH_SHORT).show();
                            Log.d("tag_time", "isBreakTimeEnd");

                        }
                        if(analogClock.isExamEnd()) Toast.makeText(TotalTimer_analogClock.this, "모든 시험이 종료되었습니다.\n뒤로 가기 버튼을 눌러 시험을 종료하세요", Toast.LENGTH_SHORT).show();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();      
                    }
            }
        }, 0);*/

        /*
        btn_next_analogClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analogClock.setCurrent_subject(current_subject+1);
//                if(current_subject==1) {
//                    TextView_analogClock_subject_name.setText("국어 (80분)\n8:40~10:00");
//                    customAnalogClock.setStart_hour(8);
//                    customAnalogClock.setStart_minute(40);
//                }
                if(current_subject==2){
                    TextView_analogClock_subject_name.setText("쉬는 시간 (20분)\n10:00~10:20\n(10:30에 시험 시작)");
                    analogClock.setStart_hour(10);
                    analogClock.setStart_minute(0);
                    analogClock.setStart_second(0);
//                    Log.d("TAG2", "start_hour:"+customAnalogClock.getStart_hour());
//                    Log.d("TAG2", "start_minute:"+customAnalogClock.getStart_minute());

                }
                else if(current_subject==3) {
                    TextView_analogClock_subject_name.setText("수학 (100분)\n10:30~12:10");
                    analogClock.setStart_hour(10);
                    analogClock.setStart_minute(30);
                    analogClock.setStart_second(0);

//                    Log.d("TAG2", "start_hour:"+customAnalogClock.getStart_hour());
//                    Log.d("TAG2", "start_minute:"+customAnalogClock.getStart_minute());
                }
                else if(current_subject==4){
                    TextView_analogClock_subject_name.setText("점심 시간 (50분)\n12:10~13:00\n(13:10에 시험 시작)");
                    analogClock.setStart_hour(0);
                    analogClock.setStart_minute(10);
                    analogClock.setStart_second(0);

//                    Log.d("TAG2", "start_hour:"+customAnalogClock.getStart_hour());
//                    Log.d("TAG2", "start_minute:"+customAnalogClock.getStart_minute());
                }
                else if(current_subject==5) {
                    TextView_analogClock_subject_name.setText("영어(70분)\n13:10~14:20");
                    analogClock.setStart_hour(1);
                    analogClock.setStart_minute(10);
                    analogClock.setStart_second(0);

//                    Log.d("TAG2", "start_hour:"+customAnalogClock.getStart_hour());
//                    Log.d("TAG2", "start_minute:"+customAnalogClock.getStart_minute());
                }
                else if(current_subject==6){
                    TextView_analogClock_subject_name.setText("쉬는 시간 (20분)\n14:20~14:40\n(14:50에 시험 시작)");
                    analogClock.setStart_hour(2);
                    analogClock.setStart_minute(20);
                    analogClock.setStart_second(0);

                }
                else if(current_subject==7) {
                    TextView_analogClock_subject_name.setText("한국사(30분)\n14:50~15:20");
                    analogClock.setStart_hour(2);
                    analogClock.setStart_minute(50);
                    analogClock.setStart_second(0);

                }
                else if(current_subject==8){
                    TextView_analogClock_subject_name.setText("시험지 교체 시간 (10분)\n15:20~15:30");
                    analogClock.setStart_hour(3);
                    analogClock.setStart_minute(20);
                    analogClock.setStart_second(0);

                }
                else if(current_subject==9) {
                    TextView_analogClock_subject_name.setText("탐구 영역 1(30분)\n15:30~16:00");
                    analogClock.setStart_hour(3);
                    analogClock.setStart_minute(30);
                    analogClock.setStart_second(0);

                }
                else if(current_subject==10){
                    TextView_analogClock_subject_name.setText("시험지 교체 시간 (2분)\n16:00~16:02");
                    analogClock.setStart_hour(4);
                    analogClock.setStart_minute(0);
                    analogClock.setStart_second(0);

                }
                else if(current_subject==11) {
                    TextView_analogClock_subject_name.setText("탐구 영역 2(30분)\n16:02~16:32");
                    analogClock.setStart_hour(4);
                    analogClock.setStart_minute(2);
                    analogClock.setStart_second(0);

                }
                else if(current_subject==12){
                    TextView_analogClock_subject_name.setText("쉬는 시간 (18분)\n16:32~16:50\n(17:00에 시험 시작)");
                    analogClock.setStart_hour(4);
                    analogClock.setStart_minute(32);
                    analogClock.setStart_second(0);

                }
                else if(current_subject==13) {
                    TextView_analogClock_subject_name.setText("제2외국어/한문(40분)\n17:00~17:40");
                    analogClock.setStart_hour(5);
                    analogClock.setStart_minute(0);
                    analogClock.setStart_second(0);

                }
                else if(current_subject>13){
                    btn_next_analogClock.setText("시험 종료");
                    finish();
                }

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("시험이 아직 진행중입니다. 나가시겠습니까?");
        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                thread.interrupt();
                SubjectTimer_science_analog.super.onBackPressed();//뒤로가기
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
        thread.interrupt();
        super.onDestroy();
    }
}