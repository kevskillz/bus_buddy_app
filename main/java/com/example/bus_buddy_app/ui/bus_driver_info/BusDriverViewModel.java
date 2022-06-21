package com.example.bus_buddy_app.ui.bus_driver_info;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bus_buddy_app.R;

public class BusDriverViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BusDriverViewModel() {
        mText = new MutableLiveData<>();

        String txt = "driver info";
        mText.setValue("This is " + txt + " fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}