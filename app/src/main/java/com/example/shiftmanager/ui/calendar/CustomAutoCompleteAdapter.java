package com.example.shiftmanager.ui.calendar;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class CustomAutoCompleteAdapter extends ArrayAdapter<String> {

    private final List<String> dataList;

    public CustomAutoCompleteAdapter(Context context, int resource, List<String> dataList) {
        super(context, resource, dataList);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }
}