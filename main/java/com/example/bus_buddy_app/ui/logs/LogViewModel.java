package com.example.bus_buddy_app.ui.logs;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bus_buddy_app.R;

public class LogViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LogViewModel() {
        mText = new MutableLiveData<>();
        String txt = "Not Logged In!";
        mText.setValue(txt);
    }

    public LiveData<String> getText() {
        return mText;
    }
}