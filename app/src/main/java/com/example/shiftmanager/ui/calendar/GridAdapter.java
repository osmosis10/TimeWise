package com.example.shiftmanager.ui.calendar;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shiftmanager.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;

    List<Events> events;
    LayoutInflater inflater;

    public GridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout); // passes context and layout for single cell

        this.dates = dates; // List of dates for grid
        this.currentDate = currentDate; // Current date
        this.events = events; // List of events to be associated with dates
        inflater = LayoutInflater.from(context); // creates the views from the xml layout
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);

        // assigns necessary data to required variables
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;

        // sets view for each cell in grid to singe_cell_layout_xml
        if (view == null) {
            // inflates
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        // sets color of current month days BLACK
        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        }
        //sets color of previous and next month days to GREY
        else {
            view.setBackgroundColor(Color.parseColor("#808080"));
        }
        // Obtains day numbers and set's it to each cell respectively
        TextView dayNumber = view.findViewById(R.id.calendarDay);
        dayNumber.setText(String.valueOf(dayNo));

        return view;
    }

    // # of items to be in month grid
    @Override
    public int getCount() {
        return dates.size();
    }

    // gets position for item in dates list
    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    // returns date object at specified position
    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

}
