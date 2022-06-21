package com.example.bus_buddy_app.ui.log_in;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogInViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LogInViewModel() {
        mText = new MutableLiveData<>();
        String txt = "log in";
        mText.setValue("This is " + txt + " fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}