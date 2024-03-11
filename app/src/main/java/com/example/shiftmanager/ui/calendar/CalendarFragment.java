package com.example.shiftmanager.ui.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentCalendarBinding;
import com.example.shiftmanager.ui.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    private DatabaseHelper databaseHelper;
    ImageButton nextButton, previousButton;
    TextView currentDate;
    GridView gridView;

    private static final int MAX_CALENDAR_DAYS = 42;

    // Initializes instance of a calendar with local date
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    SimpleDateFormat dayFormat =  new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    SimpleDateFormat standardFormat = new SimpleDateFormat("YYYY-MM-DD", Locale.ENGLISH);

    GridAdapter gridAdapter;

    AlertDialog alertDialog;

    ImageView cellDay;

    AutoCompleteTextView dayShift1;
    AutoCompleteTextView dayShift2;

    AutoCompleteTextView nightshift1;
    AutoCompleteTextView nightshift2;
    ArrayAdapter<String> adapterNames;

    ImageButton assignBackButton;

    Button confirmButton;


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
        databaseHelper = new DatabaseHelper(requireContext());

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

        // Clicking on a day in the calendar will open the assign shifts page
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ASSIGN SHIFTS PAGE

                // Launches assign shifts UI
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_shifts, null);
                builder.setView(addView);


                // builds and shows assign shifts page
                alertDialog = builder.create();
                alertDialog.show();

                // Assigning and displaying date of current day in assign shifts UI
                TextView assignDate = addView.findViewById(R.id.shiftDate);
                TextView day = view.findViewById(R.id.calendarDay); // Obtaining clicked cell day
                CharSequence charDay = day.getText();
                String dayNum = charDay.toString();

                String curMonth = monthFormat.format(calendar.getTime()); // obtains current year
                String curYear = yearFormat.format(calendar.getTime()); // obtains current month
                String Date = curMonth + " " + dayNum + ", " + curYear;
                assignDate.setText(Date); // Display's shifts date

                String dbDay = String.format("%02d", Integer.parseInt(charDay.toString()));
                String dbDate = curYear + "-" + curMonth + "-" + dbDay;

                //databaseHelper.insertDate(dbDate);

                Log.d("DbDay", "The date sire " + dbDate);
                // Set onClickListener for the back button inside the AlertDialog
                assignBackButton = addView.findViewById(R.id.exitAssign); // Initialize back button
                assignBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss(); // Closes assign shifts page
                    }
                });

                // Drop down list variables
                if (adapterNames == null) {
                    // Drop down list variables
                    String[] columns = {"preferred_name"};
                    List<String> employeeNames = databaseHelper.getAllEmployeePreferredNames(columns, null, null,null,null,null);
                    employeeNames.add(0, "");

                    String[] names = employeeNames.toArray(new String[employeeNames.size()]);

                    // Create the adapterNames only once
                    adapterNames = new ArrayAdapter<>(requireContext(), R.layout.list_names, names);
                }

                // Set's view for the shift dropdowns
                dayShift1 = addView.findViewById(R.id.dayShift1);
                dayShift2 = addView.findViewById(R.id.dayShift2);
                nightshift1 = addView.findViewById(R.id.nightShift1);
                nightshift2 = addView.findViewById(R.id.nightShift2);



                // DROP DOWN PERSISTENCE
                // Retrieve the saved values from SharedPreferences after selection has occured
                //SharedPreferences preferences = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
//                String savedDayShift1 = preferences.getString("dayShift1_" + Date, "");
//                String savedDayShift2 = preferences.getString("dayShift2_" + Date, "");
//                String savedNightShift1 = preferences.getString("nightShift1_" + Date, "");
//                String savedNightShift2 = preferences.getString("nightShift2_" + Date, "");
                String savedDayShift1 = databaseHelper.getShiftValues("employee_1_name", dbDate);
                String savedDayShift2 = databaseHelper.getShiftValues("employee_2_name", dbDate);
                String savedNightShift1 = databaseHelper.getShiftValues("employee_3_name", dbDate);
                String savedNightShift2 = databaseHelper.getShiftValues("employee_4_name", dbDate);
                // Set the saved values in the AutoCompleteTextViews

                dayShift1.getText().clear();
                dayShift2.getText().clear();
                nightshift1.getText().clear();
                nightshift2.getText().clear();

                if (!savedDayShift1.isEmpty()) {
                    dayShift1.setText(savedDayShift1);

                }

                if (!savedDayShift2.isEmpty()) {
                    dayShift2.setText(savedDayShift2);
                }

                if (!savedNightShift1.isEmpty()) {
                    nightshift1.setText(savedNightShift1);
                }

                if (!savedNightShift2.isEmpty()) {
                    nightshift2.setText(savedNightShift2);
                }


                // Set database employee names to shift dropdown menu's (creates list)
                dayShift1.setAdapter(adapterNames);
                dayShift2.setAdapter(adapterNames);
                nightshift1.setAdapter(adapterNames);
                nightshift2.setAdapter(adapterNames);

                dayShift1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = adapterView.getItemAtPosition(position).toString();
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                    }
                });

                // Listener for Dayshift 2
                dayShift2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = adapterView.getItemAtPosition(position).toString();
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                    }
                });

                // Listener for Nightshift 1
                nightshift1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = adapterView.getItemAtPosition(position).toString();
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                    }
                });

                // Listener for Nightshift 2
                nightshift2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = adapterView.getItemAtPosition(position).toString();
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                    }
                });


                // Confirm button
                confirmButton = addView.findViewById(R.id.confirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selected = 0;

                        // Obtains the selections for each box of a given day
                        String daySelection1 = dayShift1.getText().toString();
                        String daySelection2 = dayShift2.getText().toString();
                        String nightSelection1 = nightshift1.getText().toString();
                        String nightSelection2 = nightshift2.getText().toString();

                        databaseHelper.insertOrUpdateDailyAssignments(dbDate, daySelection1, daySelection2, nightSelection1, nightSelection2);

//                        SharedPreferences preferences = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        // stores the selection according to the specified day in 'Date'
//                        editor.putString("dayShift1_" + Date, appendNames(preferences.getString("dayShift1_" + Date, ""), daySelection1));
//                        editor.putString("dayShift2_" + Date, appendNames(preferences.getString("dayShift2_" + Date, ""), daySelection2));
//                        editor.putString("nightShift1_" + Date, appendNames(preferences.getString("nightShift1_" + Date, ""), nightSelection1));
//                        editor.putString("nightShift2_" + Date, appendNames(preferences.getString("nightShift2_" + Date, ""), nightSelection2));
//
//                        editor.apply(); // stores the preferences

                        if (!daySelection1.isEmpty()) {
                            selected++;
                        }
                        if (!daySelection2.isEmpty()) {
                            selected++;
                        }
                        if (!nightSelection1.isEmpty()) {
                            selected++;
                        }

                        if (!nightSelection2.isEmpty()) {
                            selected++;
                        }

                        ImageView dayImage = view.findViewById(R.id.shiftStatus);
                        dayImage.setImageDrawable(null);
                        if (selected == 4) {
                            // Variables for setting calendar icons
                            dayImage.setImageResource(R.mipmap.accept);
                        }

                        else if (selected > 0) {
                            dayImage.setImageResource(R.mipmap.warning);
                        }

                        else {
                            dayImage.setImageResource(R.mipmap.exclamation);
                        }

                        alertDialog.dismiss(); // Closes assign shifts page
                    }
                });
            }
        });

        // Call setup for calendar fragment
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

    private String appendNames(String existingNames, String newName) {
        if (!existingNames.isEmpty()) {
            return existingNames + "," + newName;
        }
        else {
            return newName;
        }
    }
}
