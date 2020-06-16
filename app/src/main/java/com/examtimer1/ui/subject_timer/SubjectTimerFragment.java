package com.examtimer1.ui.subject_timer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.examtimer1.SubjectTimer.SubjectTimer_English;
import com.examtimer1.SubjectTimer.SubjectTimer_Korean;
import com.examtimer1.SubjectTimer.SubjectTimer_foreignLanguage;
import com.examtimer1.SubjectTimer.SubjectTimer_math;
import com.examtimer1.SubjectTimer.SubjectTimer_science;

import static android.content.ContentValues.TAG;

public class SubjectTimerFragment extends Fragment implements OnBackPressedListener {

    private SubjectTimerViewModel subjectTimerViewModel;
    private Button btn_Korean, btn_math, btn_English, btn_science, btn_foreignLanguage;
    private long backKeyPressedTime;
    private MainActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide(); // 상단 바 숨기기

        Log.d(TAG, "isBtn_KoreanClicked?0");
        subjectTimerViewModel =
                ViewModelProviders.of(this).get(SubjectTimerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subject_timer, container, false);
        activity=(MainActivity)getActivity();

//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        btn_Korean=root.findViewById(R.id.btn_Korean);
        btn_math=root.findViewById(R.id.btn_math);
        btn_English=root.findViewById(R.id.btn_English);
        btn_science=root.findViewById(R.id.btn_science);
        btn_foreignLanguage=root.findViewById(R.id.btn_foreignLanguage);

        btn_Korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_Korean.class);
                startActivity(intent);
            }
        });

        btn_math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_math.class);
                startActivity(intent);
            }
        });

        btn_English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_English.class);
                startActivity(intent);
            }
        });

        btn_science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_science.class);
                startActivity(intent);
            }
        });

        btn_foreignLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_foreignLanguage.class);
                startActivity(intent);
            }
        });

//        Button.OnClickListener listener=new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                switch(v.getId()){
//                    case R.id.btn_Korean:
//                        Log.d(TAG, "isBtn_KoreanClicked?1");
//                        Intent intent =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_Korean.class);
//                        startActivity(intent);
//                        Log.d(TAG, "isBtn_KoreanClicked?2");
//                        break;
//                    case R.id.btn_math:
//                        Intent intent2 =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_math.class);
//                        startActivity(intent2);
//                        break;
//                    case R.id.btn_English:
//                        Intent intent3 =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_English.class);
//                        startActivity(intent3);
//                        break;
//                    case R.id.btn_science:
//                        Intent intent4 =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_science.class);
//                        startActivity(intent4);
//                        break;
//                    case R.id.btn_foreignLanguage:
//                        Intent intent5 =new Intent(SubjectTimerFragment.this.getActivity(), SubjectTimer_foreignLanguage.class);
//                        startActivity(intent5);
//                        break;
//                }
//            }
//        };
        return root;
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