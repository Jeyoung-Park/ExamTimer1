package com.examtimer1.ui.record;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.examtimer1.MainActivity;
import com.examtimer1.OnBackPressedListener;
import com.examtimer1.examtimer.R;
import com.examtimer1.Record.Record_English;
import com.examtimer1.Record.Record_Korean;
import com.examtimer1.Record.Record_foreignLanguage;
import com.examtimer1.Record.Record_math;
import com.examtimer1.Record.Record_science;
import com.examtimer1.Record.Record_total;

public class RecordFragment extends Fragment implements OnBackPressedListener {

    private RecordViewModel notificationsViewModel;
    private Button btn_record_total, btn_record_Korean, btn_record_math, btn_record_English, btn_record_science, btn_record_foreignLanguage;
    private long backKeyPressedTime;
    MainActivity activity;
    private LayoutInflater myLayoutInflater;
    private ViewGroup myViewGroup;
    private Bundle myBundle;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide(); // 상단 바 숨기기

        notificationsViewModel =
                ViewModelProviders.of(this).get(RecordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_record, container, false);
        activity=(MainActivity)getActivity();
        myBundle=savedInstanceState;
        myLayoutInflater=inflater;
        myViewGroup=container;

//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        btn_record_Korean=root.findViewById(R.id.btn_record_Korean);
        btn_record_math=root.findViewById(R.id.btn_record_math);
        btn_record_English=root.findViewById(R.id.btn_record_English);
        btn_record_science=root.findViewById(R.id.btn_record_science);
        btn_record_foreignLanguage=root.findViewById(R.id.btn_record_foriegnLanguage);
        btn_record_total=root.findViewById(R.id.btn_record_total);

        btn_record_Korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(RecordFragment.this.getActivity(), Record_Korean.class);
                startActivity(intent1);
            }
        });

        btn_record_math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(RecordFragment.this.getActivity(), Record_math.class);
                startActivity(intent2);
            }
        });

        btn_record_English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(RecordFragment.this.getActivity(), Record_English.class);
                startActivity(intent3);
            }
        });

        btn_record_science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(RecordFragment.this.getActivity(), Record_science.class);
                startActivity(intent6);
            }
        });

        btn_record_foreignLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7=new Intent(RecordFragment.this.getActivity(), Record_foreignLanguage.class);
                startActivity(intent7);
            }
        });

        btn_record_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent8=new Intent(RecordFragment.this.getActivity(), Record_total.class);
                startActivity(intent8);
            }
        });
        return root;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            onCreateView(myLayoutInflater, myViewGroup, myBundle);
        }else{
            onCreateView(myLayoutInflater, myViewGroup, myBundle);
        }
    }

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
}