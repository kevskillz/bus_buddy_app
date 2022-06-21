package com.example.bus_buddy_app.ui.user_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserInfoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UserInfoViewModel() {
        mText = new MutableLiveData<>();
        String txt = "logs";
        mText.setValue("This is " + txt + " fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}