package com.example.shiftmanager.ui.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
    ArrayAdapter<String> adapterDayShift1;
    ArrayAdapter<String> adapterDayShift2;
    ArrayAdapter<String> adapterNightShift1;
    ArrayAdapter<String> adapterNightShift2;

    ImageButton assignBackButton;

    Button confirmButton;

    boolean dayshift1trained = false;
    boolean dayshift2trained = false;
    boolean nightshift1trained = false;
    boolean nightshift2trained = false;
    private String dayshift1temp = null;
    private String dayshift2temp = null;
    private String nightshift1temp = null;
    private String nightshift2temp = null;


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
                int currentMonth = getMonthNum(curMonth);

                Log.d("CurrentMonth", String.valueOf(currentMonth));
                Calendar localCalendar = Calendar.getInstance(Locale.CANADA);
                localCalendar.set(Integer.parseInt(curYear), currentMonth, Integer.parseInt(dayNum));

                int dayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);

                String dateString = curYear + "-" + String.format("%02d", currentMonth) + "-" + dbDay;
                int weekNumber = getWeekNumber(dateString);

                Log.d("WeekNumber", String.valueOf(weekNumber));
                Log.d("dateString", dateString);

                Log.d("dow", getDayOfWeekString(dayOfWeek));
                //databaseHelper.insertDate(dbDate);

                Log.d("DbDay", "The date sire " + dateString);
                // Set onClickListener for the back button inside the AlertDialog
                assignBackButton = addView.findViewById(R.id.exitAssign); // Initialize back button
                assignBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss(); // Closes assign shifts page
                    }
                });

                // Drop down list variables
                if (adapterDayShift1 == null) {
                    // Drop down list variables
                   adapterDayShift1 = setupAdapters(dateString, "morning");

                }
                // Drop down list variables
//                if (adapterDayShift2 == null) {
//                    // Drop down list variables
//                    adapterDayShift2 = setupAdapters(dateString, "morning");
//
//                }
                if (adapterNightShift1 == null) {
                    // Drop down list variables
                    adapterNightShift1 = setupAdapters(dateString, "afternoon");

                }
//                if (adapterNightShift2 == null) {
//                    // Drop down list variables
//                    adapterNightShift2 = setupAdapters(dateString, "afternoon");
//
//
//
//                }

                // Set's view for the shift dropdowns
                dayShift1 = addView.findViewById(R.id.dayShift1);
                dayShift2 = addView.findViewById(R.id.dayShift2);
                nightshift1 = addView.findViewById(R.id.nightShift1);
                nightshift2 = addView.findViewById(R.id.nightShift2);

                //databaseHelper.removeAllDailyAssignments();

                // DROP DOWN PERSISTENCE
                // Retrieve the saved values from SharedPreferences after selection has occured
                //SharedPreferences preferences = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
//                String savedDayShift1 = preferences.getString("dayShift1_" + Date, "");
//                String savedDayShift2 = preferences.getString("dayShift2_" + Date, "");
//                String savedNightShift1 = preferences.getString("nightShift1_" + Date, "");
//                String savedNightShift2 = preferences.getString("nightShift2_" + Date, "");
                String savedDayShift1 = databaseHelper.getShiftValues("dayshift1_employee", dateString);
                String savedDayShift2 = databaseHelper.getShiftValues("dayshift2_employee", dateString);
                String savedNightShift1 = databaseHelper.getShiftValues("nightshift1_employee", dateString);
                String savedNightShift2 = databaseHelper.getShiftValues("nightshift2_employee", dateString);
                // Set the saved values in the AutoCompleteTextViews

                dayShift1.getText().clear();
                dayShift2.getText().clear();
                nightshift1.getText().clear();
                nightshift2.getText().clear();

                if (savedDayShift1 != null && !savedDayShift1.isEmpty()) {
                    dayShift1.setText(savedDayShift1);

                }

                if (savedDayShift2 != null && !savedDayShift2.isEmpty()) {
                    dayShift2.setText(savedDayShift2);
                }

                if (savedNightShift1 != null && !savedNightShift1.isEmpty()) {
                    nightshift1.setText(savedNightShift1);
                }

                if (savedNightShift2 != null && !savedNightShift2.isEmpty()) {
                    nightshift2.setText(savedNightShift2);
                }


                // Set database employee names to shift dropdown menu's (creates list)
                dayShift1.setAdapter(adapterDayShift1);
                dayShift2.setAdapter(adapterDayShift1);
                nightshift1.setAdapter(adapterNightShift1);
                nightshift2.setAdapter(adapterNightShift1);

                dayShift1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = dayShift1.getText().toString();
                        String item2 = dayShift2.getText().toString();
                        Log.d("item1", item + " " + item2);
                        //Log.d("item", item);
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                        String[] column1 = {"dayshift1_employee"};
                        String[] column2 = {"dayshift2_employee"};
                        String currEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column1,"date = ?",new String[]{dateString});
                        String nextEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column2,"date = ?",new String[]{dateString});
                        Log.d("CurrentEmployees", currEmployee + " " + nextEmployee);
                        List<String> dayShiftNames = null;
                        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                                "nightshift1_employee", "nightshift2_employee"};
                        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{dateString});
                        if (currEmployee != null && !item.equals(currEmployee)) {
                            String[] columns = {"preferred_name"};
                            String empSelection = "(monday_morning = ? OR " +
                                    "tuesday_morning = ? OR " +
                                    "wednesday_morning = ? OR " +
                                    "thursday_morning = ? OR " +
                                    "friday_morning = ?)";
                            if (employees != null && !employees.isEmpty()) {
                                Log.d("Im in here", "Now");
                                List<String> selectionArgsList = new ArrayList<>(Arrays.asList("1", "1", "1", "1", "1"));
                                for (String employee : employees) {
                                    if (employee.equals(currEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item);
                                        Log.d("EmployeeStuff", employee);
                                    } else if (employee.equals(nextEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item2);
                                        Log.d("EmployeeStuff", employee);
                                    } else {
                                        empSelection += " AND preferred_name != ?";
                                        Log.d("EmployeeStuff", employee);
                                        selectionArgsList.add(employee);
                                    }
                                }
                                String[] selectionArgs = selectionArgsList.toArray(new String[0]);

                                dayShiftNames = databaseHelper.getAllEmployeePreferredNames(columns, empSelection, selectionArgs, null, null, null);
                            }
                            if (adapterDayShift1 == null) {
                                adapterDayShift1 = new ArrayAdapter<>(requireContext(), R.layout.list_names, dayShiftNames);
                                dayShift1.setAdapter(adapterDayShift1);
                            } else {
                                adapterDayShift1.clear();
                                adapterDayShift1.addAll(dayShiftNames);
                                adapterDayShift1.notifyDataSetChanged();
                                dayShift1.setAdapter(adapterDayShift1);
                            }
                        }
                    }
                });
//


                // Listener for Dayshift 2
                dayShift2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = dayShift1.getText().toString();
                        String item2 = dayShift2.getText().toString();
                        Log.d("item2", item + " " + item2);
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                        String[] column1 = {"dayshift2_employee"};
                        String[] column2 = {"dayshift1_employee"};
                        String currEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column1,"date = ?",new String[]{dateString});
                        String previousEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column2,"date = ?",new String[]{dateString});
                        List<String> dayShiftNames = null;
                        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                                "nightshift1_employee", "nightshift2_employee"};
                        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{dateString});
                        if (currEmployee != null && !item2.equals(currEmployee)) {
                            String[] columns = {"preferred_name"};
                            String empSelection = "(monday_morning = ? OR " +
                                    "tuesday_morning = ? OR " +
                                    "wednesday_morning = ? OR " +
                                    "thursday_morning = ? OR " +
                                    "friday_morning = ?)";
                            if (employees != null && !employees.isEmpty()) {
                                List<String> selectionArgsList = new ArrayList<>(Arrays.asList("1", "1", "1", "1", "1"));
                                for (String employee : employees) {
                                    if (employee.equals(currEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item2);
                                        Log.d("EmployeeStuff", employee);
                                    } else if (employee.equals(previousEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item);
                                        Log.d("EmployeeStuff", employee);
                                    } else {
                                        empSelection += " AND preferred_name != ?";
                                        Log.d("EmployeeStuff", employee);
                                        selectionArgsList.add(employee);
                                    }
                                }
                                String[] selectionArgs = selectionArgsList.toArray(new String[0]);

                                dayShiftNames = databaseHelper.getAllEmployeePreferredNames(columns, empSelection, selectionArgs, null, null, null);
                            }
                            if (adapterDayShift1 == null) {
                                adapterDayShift1 = new ArrayAdapter<>(requireContext(), R.layout.list_names, dayShiftNames);
                                dayShift2.setAdapter(adapterDayShift1);
                            } else {
                                adapterDayShift1.clear();
                                adapterDayShift1.addAll(dayShiftNames);
                                adapterDayShift1.notifyDataSetChanged();
                                dayShift2.setAdapter(adapterDayShift1);
                            }
                        }
                    }
                });

                // Listener for Nightshift 1
                nightshift1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = nightshift1.getText().toString();
                        String item2 = nightshift2.getText().toString();
                        Log.d("item", item);
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                        String[] column1 = {"nightshift1_employee"};
                        String[] column2 = {"nightshift2_employee"};
                        String currEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column1,"date = ?",new String[]{dateString});
                        String nextEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column2,"date = ?",new String[]{dateString});
                        List<String> nightShiftNames = null;
                        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                                "nightshift1_employee", "nightshift2_employee"};
                        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{dateString});
                        if (currEmployee != null && !item.equals(currEmployee)) {
                            String[] columns = {"preferred_name"};
                            String empSelection = "(monday_afternoon = ? OR " +
                                    "tuesday_afternoon = ? OR " +
                                    "wednesday_afternoon = ? OR " +
                                    "thursday_afternoon = ? OR " +
                                    "friday_afternoon = ?)";
                            if (employees != null && !employees.isEmpty()) {
                                List<String> selectionArgsList = new ArrayList<>(Arrays.asList("1", "1", "1", "1", "1"));
                                for (String employee : employees) {
                                    if (employee.equals(currEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item);
                                        Log.d("EmployeeStuff", employee);
                                    } else if (employee.equals(nextEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item2);
                                        Log.d("EmployeeStuff", employee);
                                    } else {
                                        empSelection += " AND preferred_name != ?";
                                        Log.d("EmployeeStuff", employee);
                                        selectionArgsList.add(employee);
                                    }
                                }
                                String[] selectionArgs = selectionArgsList.toArray(new String[0]);

                                nightShiftNames = databaseHelper.getAllEmployeePreferredNames(columns, empSelection, selectionArgs, null, null, null);
                            }
                            if (adapterNightShift1 == null) {
                                adapterNightShift1 = new ArrayAdapter<>(requireContext(), R.layout.list_names, nightShiftNames);
                                nightshift1.setAdapter(adapterNightShift1);
                            } else {
                                adapterNightShift1.clear();
                                adapterNightShift1.addAll(nightShiftNames);
                                adapterNightShift1.notifyDataSetChanged();
                                nightshift1.setAdapter(adapterNightShift1);
                            }
                        }
                    }
                });

                // Listener for Nightshift 2
                nightshift2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String item = nightshift1.getText().toString();
                        String item2 = nightshift2.getText().toString();
                        Log.d("item", item);
                        Toast.makeText(requireContext(), "Employee: " + item, Toast.LENGTH_SHORT).show();
                        String[] column1 = {"nightshift2_employee"};
                        String[] column2 = {"nightshift1_employee"};
                        String currEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column1,"date = ?",new String[]{dateString});
                        String prevEmployee = databaseHelper.getSingleDailyAssignmentsEmployee(column2,"date = ?",new String[]{dateString});
                        List<String> dayShiftNames = null;
                        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                                "nightshift1_employee", "nightshift2_employee"};
                        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{dateString});
                        if (currEmployee != null && !item.equals(currEmployee)) {
                            String[] columns = {"preferred_name"};
                            String empSelection = "(monday_afternoon = ? OR " +
                                    "tuesday_afternoon = ? OR " +
                                    "wednesday_afternoon = ? OR " +
                                    "thursday_afternoon = ? OR " +
                                    "friday_afternoon = ?)";
                            if (employees != null && !employees.isEmpty()) {
                                List<String> selectionArgsList = new ArrayList<>(Arrays.asList("1", "1", "1", "1", "1"));
                                for (String employee : employees) {
                                    if (employee.equals(currEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item2);
                                        Log.d("EmployeeStuff", employee);
                                    } else if (employee.equals(prevEmployee)) {
                                        empSelection += " AND preferred_name != ?";
                                        selectionArgsList.add(item);
                                        Log.d("EmployeeStuff", employee);
                                    } else {
                                        empSelection += " AND preferred_name != ?";
                                        Log.d("EmployeeStuff", employee);
                                        selectionArgsList.add(employee);
                                    }
                                }
                                String[] selectionArgs = selectionArgsList.toArray(new String[0]);

                                dayShiftNames = databaseHelper.getAllEmployeePreferredNames(columns, empSelection, selectionArgs, null, null, null);
                            }
                            if (adapterNightShift1 == null) {
                                adapterNightShift1 = new ArrayAdapter<>(requireContext(), R.layout.list_names, dayShiftNames);
                                nightshift2.setAdapter(adapterNightShift1);
                            } else {
                                adapterNightShift1.clear();
                                adapterNightShift1.addAll(dayShiftNames);
                                adapterNightShift1.notifyDataSetChanged();
                                nightshift2.setAdapter(adapterNightShift1);
                            }
                        }
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

                        databaseHelper.insertOrUpdateDailyAssignments(dateString, daySelection1, daySelection2, null,
                                nightSelection1, nightSelection2, null, null, null, weekNumber);

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

    private ArrayAdapter<String> setupAdapters(String date, String time) {
        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                "nightshift1_employee", "nightshift2_employee"};
        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{date});

        String[] columns = {"preferred_name"};
        String selection = "(monday_" + time + " = ? OR " +
                "tuesday_" + time + " = ? OR " +
                "wednesday_" + time + " = ? OR " +
                "thursday_" + time + " = ? OR " +
                "friday_" + time + " = ?)";
        if (employees != null && !employees.isEmpty()) {
            List<String> selectionArgsList = new ArrayList<>(Arrays.asList("1", "1", "1", "1", "1"));
            for (String employee : employees) {
                selection += " AND preferred_name != ?";
                selectionArgsList.add(employee);
            }
            String[] selectionArgs = selectionArgsList.toArray(new String[0]);
            List<String> dayShiftNames = databaseHelper.getAllEmployeePreferredNames(columns, selection, selectionArgs,null,null,null);
            // Create the adapterNames only once
            return new ArrayAdapter<>(requireContext(), R.layout.list_names, dayShiftNames);
        } else {
            String[] selectionArgs = new String[]{"1", "1", "1", "1", "1",};
            List<String> dayShiftNames = databaseHelper.getAllEmployeePreferredNames(columns, selection, selectionArgs, null, null, null);

            // Create the adapterNames only once
            return new ArrayAdapter<>(requireContext(), R.layout.list_names, dayShiftNames);
        }
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

    public static int getWeekNumber(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);

            Calendar locCalendar = Calendar.getInstance();
            locCalendar.setFirstDayOfWeek(Calendar.MONDAY);
            locCalendar.setTime(date);

            int weekNumber = locCalendar.get(Calendar.WEEK_OF_YEAR);

            return weekNumber;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    private String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Unknown Day";
        }
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
