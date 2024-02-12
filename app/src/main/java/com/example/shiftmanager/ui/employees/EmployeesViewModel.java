package com.example.shiftmanager.ui.employees;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployeesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EmployeesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is employees fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}