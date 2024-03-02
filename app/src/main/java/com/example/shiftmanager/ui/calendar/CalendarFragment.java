package com.example.shiftmanager.ui.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    ImageButton nextButton, previousButton;
    TextView currentDate;
    GridView gridView;

    private static final int MAX_CALENDAR_DAYS = 42;

    // Initializes instance of a calendar with local date
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    GridAdapter gridAdapter;

    AlertDialog alertDialog;

    AutoCompleteTextView autoCompleteTextViewday1;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtaining root view
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Linking layout objects to variables
        nextButton = root.findViewById(R.id.nextButton);
        previousButton = root.findViewById(R.id.prevButton);
        currentDate = root.findViewById(R.id.currentDate);
        gridView = root.findViewById(R.id.gridView);
        //autoCompleteTextViewday1

        // Clicking will cycle to previous month
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                setUpCalendar(requireContext());
            }
        });

        // Clicking will cycle to next month
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                setUpCalendar(requireContext());
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_day_shits, null);
                builder.setView(addView);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // Call the initial setup
        setUpCalendar(requireContext());

        return root;
    }

    // Set's up calendar for each month after button click
    private void setUpCalendar(Context context) {
        String curDate = dateFormat.format(calendar.getTime()); // obtains current date
        currentDate.setText(curDate);
        dates.clear(); // clears dates array to repopulate later

        // Creates copy of calendar for current month
        Calendar monthCalendar = (Calendar) calendar.clone();

        monthCalendar.set(Calendar.DAY_OF_MONTH, 1); // Sets first day of the month

        // index for first day of month
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;

        // moves calendar back to first day of week
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);

        // Creates dates for given month and stores in array
        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }
        // sets the gridview to according to GridAdapter constructor
        gridAdapter = new GridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);

    }

    private void CollectShiftsPerMonth(String Month, String Year) {

    }
}
