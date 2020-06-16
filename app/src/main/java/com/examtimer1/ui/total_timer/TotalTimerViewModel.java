package com.examtimer1.ui.total_timer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TotalTimerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TotalTimerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}