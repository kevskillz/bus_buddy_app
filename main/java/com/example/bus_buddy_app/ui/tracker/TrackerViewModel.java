package com.example.bus_buddy_app.ui.tracker;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bus_buddy_app.R;

public class TrackerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TrackerViewModel() {
        mText = new MutableLiveData<>();
        String txt = "tracker";
        mText.setValue("X minutes away\n EST: 0:00 AM");
    }

    public LiveData<String> getText() {
        return mText;
    }
}