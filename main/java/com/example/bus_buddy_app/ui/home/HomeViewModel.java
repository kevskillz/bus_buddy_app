package com.example.bus_buddy_app.ui.home;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bus_buddy_app.R;


public class HomeViewModel extends ViewModel  {

    private static int minsAway;
    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        minsAway = 0;
    }


    public LiveData<String> getText() {

        return mText;
    }


}