package com.example.shiftmanager.ui.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shiftmanager.R;
import com.example.shiftmanager.ui.database.DatabaseHelper;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    ImageView cellDay;
    List<Events> events;

    String month_year;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    TextView cellNum;
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    private DatabaseHelper databaseHelper;
    LayoutInflater inflater;

    public GridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events, String month_year) {
        super(context, R.layout.single_cell_layout); // passes context and layout for single cell

        this.dates = dates; // List of dates for grid
        this.currentDate = currentDate; // Current date
        this.events = events; // List of events to be associated with dates
        this.month_year = month_year;
        databaseHelper = new DatabaseHelper(getContext());
        inflater = LayoutInflater.from(context); // creates the views from the xml layout
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);



        //YYYY/MM/DD
        // assigns necessary data to required variables
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH); // get current day of the month
        String month_year_string = month_year;
        String[] month = month_year_string.split(" ", 2);

        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");

        Date date = new Date();
        String monthString = monthFormat.format(date); // ex. '03'
        String yearString = yearFormat.format(date); // ex. '2023'
        int monthInt = Integer.parseInt(monthString);
        int yearInt = Integer.parseInt(yearString);

        View view = convertView;

        int monthnum = getMonthNum(month[0]);
        // sets view for each cell in grid to singe_cell_layout_xml
        if (view == null) {
            // inflates
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
            cellDay = view.findViewById(R.id.shiftStatus);
        }

        // sets color of current month days BLACK
        if (displayMonth == currentMonth && displayYear == currentYear) {
            // WRITE FUNCTION HERE
            String dayNoString = String.format("%02d", dayNo);
            String dateString = month[1] + "-" + String.format("%02d", monthnum) + "-" +dayNoString;
            setIcon(dateString, cellDay);
            view.setBackgroundColor(getContext().getResources().getColor(R.color.black));
            Drawable backgroundDrawable = getContext().getResources().getDrawable(R.drawable.round_corner);
            view.setBackground(backgroundDrawable);
            view.setClickable(false); // Clickable even thought it seems like it should be

        }
        //sets color of previous and next month days to GREY
        else {
            cellDay.setImageDrawable(null); // removes the icons from previous or next months
            Drawable backgroundDrawable = getContext().getResources().getDrawable(R.drawable.rounded_corner2);
            view.setBackground(backgroundDrawable);
            view.setClickable(true); // Not clickable even thought it seems like it should be
        }
        // Obtains day numbers and set's it to each cell respectively
        TextView dayNumber = view.findViewById(R.id.calendarDay);
        dayNumber.setText(String.valueOf(dayNo));

        // Set's current day of month to the color gold (COLOUR TBD)
        if (dayNo == currentDay && displayMonth == monthInt && displayYear == yearInt) {
            dayNumber.setTextColor(Color.parseColor("#FFD700"));
        }

        Calendar shiftCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();


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

    // remove second for loop and try counter (if counter == loop limit -1...)
    public int setIcon(String date, ImageView cellDay) {
        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                "nightshift1_employee", "nightshift2_employee",
                "fullday1_employee", "fullday2_employee"};

        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{date});
        Log.d("DATE", date);
        int weekdaycounter = 0;
        int weekendcounter = 0;
        //Log.d("SIZE", "Size = " + employees.size());
            for(int i=0;i<employees.size(); i++){
                if (employees.get(i) != null) {
                    if (i < 4) {
                        weekdaycounter++;
                    }

                    else if (i > 3){
                        weekendcounter++;
                    }
                }

            }
            Log.d("ICON COUNTER", "DAY = " + String.valueOf(weekdaycounter) + "END" + String.valueOf(weekendcounter) + "Size = "+ String.valueOf(employees.size()));
            if (weekendcounter == 0 && weekdaycounter == 0) {
                cellDay.setImageResource(R.mipmap.exclamation);
            }

            else if (weekdaycounter < 4 && weekendcounter == 0) {
                cellDay.setImageResource(R.mipmap.warning);
            }

            else if (weekendcounter < 2 && weekdaycounter == 0) {
                cellDay.setImageResource(R.mipmap.warning);
            }

            else if (weekdaycounter == 4) {
                cellDay.setImageResource(R.mipmap.check);
            }

            else if (weekendcounter == 2) {
                cellDay.setImageResource(R.mipmap.check);
            }

        return 0;
    }

    private static int getMonthNum(String month) {

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);


        try {
            Date date = monthFormat.parse(month);
            return date.getMonth() + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
