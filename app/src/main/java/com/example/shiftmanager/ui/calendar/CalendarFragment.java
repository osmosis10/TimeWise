package com.example.shiftmanager.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.databinding.FragmentCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        LinearLayout customCalendarLayout = binding.customCalendar;
        GridView gridView = binding.grid;
        ImageButton prevButton = binding.prevButton;
        ImageButton nextButton = binding.nextButton;
        TextView currentDateView = binding.currentDateText;

        currentDateView.setText(getCurrentDate());

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getCurrentDate() {
        SimpleDateFormat currDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return currDate.format(new Date());
    }
}