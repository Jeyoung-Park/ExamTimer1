package com.examtimer1.ui.total_timer;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.examtimer1.BroadCastD;
import com.examtimer1.MainActivity;
import com.examtimer1.OnBackPressedListener;
import com.examtimer1.TotalTimer.TotalTimer_analogClock;
import com.examtimer1.examtimer.R;
import com.examtimer1.TotalTimer.TotalTimer_Korean;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TotalTimerFragment extends Fragment
implements OnBackPressedListener {
    static final int SYEAR=2022, SMONTH=11, SDAY=17;

    private TotalTimerViewModel homeViewModel;
    private TextView TextView_Dday;
    private Button btn_start_totalTimer, btn_start_analogClock;
    private long backKeyPressedTime;
    private MainActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide(); // 상단 바 숨기기
        homeViewModel =
                ViewModelProviders.of(this).get(TotalTimerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_total_timer, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                activity=(MainActivity)getActivity();
            }
        });

//        root.setTheme(android.R.style.Theme_Black_NoTitleBar);

        TextView_Dday=root.findViewById(R.id.TextView_Dday);
        btn_start_totalTimer=root.findViewById(R.id.btn_start_totalTimer);
        btn_start_analogClock=root.findViewById(R.id.btn_start_analogClock);

        TextView_Dday.setText(getDday());

        btn_start_totalTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TotalTimerFragment.this.getActivity(), TotalTimer_Korean.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        btn_start_analogClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TotalTimerFragment.this.getActivity(), TotalTimer_analogClock.class);
                startActivity(intent);
            }
        });

//        setAlarm();

        return root;
    }

  /*  public void setAlarm(){
        AlarmManager am= (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(TotalTimerFragment.this.getActivity(), BroadCastD.class);

        PendingIntent sender= PendingIntent.getBroadcast(TotalTimerFragment.this.getActivity(), 0, intent, 0);

        Calendar calendar=Calendar.getInstance();

        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)+1, 0);
//        Log.d(TAG, "현재 날짜, 시간: "+calendar.get(Calendar.YEAR)+"년 "+calendar.get(Calendar.MONTH)+"월 "+calendar.get(Calendar.DATE)+"일 "+calendar.get(Calendar.HOUR_OF_DAY)+"시 "+calendar.get(Calendar.MINUTE)+"분");
//        Log.d(TAG, "calendar: "+calendar.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }*/

    @Override
    public void onBackPressed() {
        Toast toast=Toast.makeText(getContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            getActivity().finish();
            toast.cancel();
        }
    }

    @Override public void onResume() { super.onResume(); activity.setOnBackPressedListener(this); }

    private int countdday(int myear, int mmonth, int mday){
        try{
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

            Calendar todayCal=Calendar.getInstance();
            Calendar ddayCal=Calendar.getInstance();

            mmonth-=1;
            ddayCal.set(myear, mmonth, mday);

            long today=todayCal.getTimeInMillis()/(24*60*60*1000);
            long dday=ddayCal.getTimeInMillis()/(24*60*60*1000);
            long count=dday-today;
            return (int) count;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    private String getDday(){
        int dday_result=countdday(SYEAR, SMONTH, SDAY);
        String strFormat;
        if(dday_result>0) strFormat="D-%d";
        else if(dday_result==0) strFormat="D-day";
        else {
            dday_result*=-1;
            strFormat="D+%d";
        }
        return String.format(strFormat, dday_result);
    }



}

