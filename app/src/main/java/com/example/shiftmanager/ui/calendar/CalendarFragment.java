package com.example.shiftmanager.ui.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentCalendarBinding;
import com.example.shiftmanager.ui.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    AutoCompleteTextView afternoonShift1;
    AutoCompleteTextView afternoonShift2;

    AutoCompleteTextView fulldayShift1;
    AutoCompleteTextView fulldayShift2;
    ArrayAdapter<String> adapterDayShift1;
    ArrayAdapter<String> adapterDayShift2;
    ArrayAdapter<String> adapterDayShift3;
    ArrayAdapter<String> adapterafternoonShift1;
    ArrayAdapter<String> adapterafternoonShift2;
    ArrayAdapter<String> adapterafternoonShift3;

    ArrayAdapter<String> adapterfulldayShift1;
    ArrayAdapter<String> adapterfulldayShift2;

    ImageButton assignBackButton;

    Button confirmButton;

    ImageButton employeeList;


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
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ASSIGN SHIFTS PAGE

                // Launches assign shifts UI
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setCancelable(true);
                TextView day = view.findViewById(R.id.calendarDay); // Obtaining clicked cell day
                CharSequence charDay = day.getText();
                String dayNum = charDay.toString();

                String curMonth = monthFormat.format(calendar.getTime()); // obtains current year
                String curYear = yearFormat.format(calendar.getTime()); // obtains current month
                String Date = curMonth + " " + dayNum + ", " + curYear;
                String dbDay = String.format("%02d", Integer.parseInt(charDay.toString()));

                String dbDate = curYear + "-" + curMonth + "-" + dbDay;
                int currentMonth = getMonthNum(curMonth);

                String dow = getDayOfWeekString(FindDay.dayofweek(Integer.parseInt(dbDay), currentMonth, Integer.parseInt(curYear)));

                Log.d("CurrentMonth", String.valueOf(currentMonth));
                Calendar localCalendar = Calendar.getInstance(Locale.CANADA);
                localCalendar.set(Integer.parseInt(curYear), currentMonth, Integer.parseInt(dayNum));


                String dateString = curYear + "-" + String.format("%02d", currentMonth) + "-" + dbDay;
                int weekNumber = getWeekNumber(dateString);
                boolean isWeekendLayout = dow.equals("sunday") || dow.equals("saturday");
                View addView;
                if (dow.equals("sunday") || dow.equals("saturday")) {
                    addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_shifts_weekends, null);
                    addView.setId(R.id.assign_shifts_weekends);
                } else {
                    addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_shifts_weekdays, null);
                    addView.setId(R.id.assign_shifts_weekdays);
                }
                builder.setView(addView);

                Log.d("Dayoftheweek", dow);
                Log.d("Dayoftheweek", String.valueOf(isWeekendLayout));

                Log.d("WeekNum", String.valueOf(weekNumber));

                Date[] weekDates = getWeekDates(dateString);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Log.d("Date",  dateFormat.format(weekDates[0]));
                Log.d("Date",  dateFormat.format(weekDates[1]));
                // builds and shows assign shifts page
                alertDialog = builder.create();
                alertDialog.show();

                // Assigning and displaying date of current day in assign shifts UI
                TextView assignDate = addView.findViewById(R.id.shiftDate);

                assignDate.setText(Date); // Display's shifts date


                Log.d("WeekNumber", String.valueOf(weekNumber));
                Log.d("dateString", dateString);

                Log.d("dow",dow);
                //databaseHelper.insertDate(dbDate);
                //List<String> unscheduledEmployees = databaseHelper.getUnscheduledEmployeeForWeek(weekNumber);

//                for (String emps:unscheduledEmployees) {
//                    Log.d("Unscheduled Employees", emps);
//                }
                Log.d("DbDay", "The date sire " + dateString);
                // Set onClickListener for the back button inside the AlertDialog
                assignBackButton = addView.findViewById(R.id.exitAssign); // Initialize back button
                assignBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss(); // Closes assign shifts page
                    }
                });

                employeeList = addView.findViewById(R.id.unassignedList);
                employeeList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMenu(v, weekNumber, dateString);
                    }
                });

                int addViewId = addView.getId();
                if (addViewId == R.id.assign_shifts_weekends) {

                    if (adapterfulldayShift1 == null) {
                        adapterfulldayShift1 = setupAdapters(dateString, dow, "fullday");
                        Log.d("Array", adapterfulldayShift1.getItem(2));
                    }
                    if (adapterfulldayShift2 == null) {
                        adapterfulldayShift2 = setupAdapters(dateString, dow, "fullday");
                    }

                    fulldayShift1 = addView.findViewById(R.id.fulldayShift1);
                    fulldayShift2 = addView.findViewById(R.id.fulldayShift2);

                    String savedFulldayShift1 = databaseHelper.getShiftValues("fullday1_employee", dateString);
                    String savedFulldayShift2 = databaseHelper.getShiftValues("fullday2_employee", dateString);

                    fulldayShift1.getText().clear();
                    fulldayShift2.getText().clear();

                    if (savedFulldayShift1 != null && !savedFulldayShift1.isEmpty()) {
                        fulldayShift1.setText(savedFulldayShift1);
                    }
                    if (savedFulldayShift2 != null && !savedFulldayShift2.isEmpty()) {
                        fulldayShift2.setText(savedFulldayShift2);
                    }
                    fulldayShift1.setAdapter(adapterfulldayShift1);
                    fulldayShift2.setAdapter(adapterfulldayShift2);
                    fulldayShift1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String name1 = fulldayShift1.getText().toString();
                            String name2 = fulldayShift2.getText().toString();

                            Log.d("DayofWeek", dow);
                            fillArrayAdapters(adapterfulldayShift1, adapterfulldayShift2, fulldayShift1, fulldayShift2,
                                    dow, "fullday", name1, name2, databaseHelper);
                        }
                    });
                    fulldayShift2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String name1 = fulldayShift1.getText().toString();
                            String name2 = fulldayShift2.getText().toString();

                            Log.d("DayofWeek", dow);
                            fillArrayAdapters(adapterfulldayShift2, adapterfulldayShift1, fulldayShift2, fulldayShift1,
                                    dow, "fullday", name2, name1, databaseHelper);
                        }
                    });

                } else {
                    // Drop down list variables
                    if (adapterDayShift1 == null) {
                        // Drop down list variables
                        adapterDayShift1 = setupAdapters(dateString, dow, "morning");

                    }
                    // Drop down list variables
                    if (adapterDayShift2 == null) {
                        // Drop down list variables
                        adapterDayShift2 = setupAdapters(dateString, dow,  "morning");

                    }
                    if (adapterafternoonShift1 == null) {
                        // Drop down list variables
                        adapterafternoonShift1 = setupAdapters(dateString, dow,  "afternoon");

                    }
                    if (adapterafternoonShift2 == null) {
                        // Drop down list variables
                        adapterafternoonShift2 = setupAdapters(dateString, dow,  "afternoon");
                    }



                    // Set's view for the shift dropdowns
                    dayShift1 = addView.findViewById(R.id.dayShift1);
                    dayShift2 = addView.findViewById(R.id.dayShift2);
                    afternoonShift1 = addView.findViewById(R.id.nightShift1);
                    afternoonShift2 = addView.findViewById(R.id.nightShift2);




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
                    afternoonShift1.getText().clear();
                    afternoonShift2.getText().clear();


                    if (savedDayShift1 != null && !savedDayShift1.isEmpty()) {
                        dayShift1.setText(savedDayShift1);

                    }

                    if (savedDayShift2 != null && !savedDayShift2.isEmpty()) {
                        dayShift2.setText(savedDayShift2);
                    }

                    if (savedNightShift1 != null && !savedNightShift1.isEmpty()) {
                        afternoonShift1.setText(savedNightShift1);
                    }

                    if (savedNightShift2 != null && !savedNightShift2.isEmpty()) {
                        afternoonShift2.setText(savedNightShift2);
                    }


                    // Set database employee names to shift dropdown menu's (creates list)
                    dayShift1.setAdapter(adapterDayShift1);
                    dayShift2.setAdapter(adapterDayShift2);
                    afternoonShift1.setAdapter(adapterafternoonShift1);
                    afternoonShift2.setAdapter(adapterafternoonShift2);


                    dayShift1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String name1 = dayShift1.getText().toString();
                            String name2 = dayShift2.getText().toString();
                            Log.d("name1 and name2", name1 + " " +  name2);
                            if (name2.isEmpty()) {
                                Log.d("name 1 and name2", "yes");
                            }

                            Log.d("DayofWeek", dow);
                            fillArrayAdapters(adapterDayShift1, adapterDayShift2, dayShift1, dayShift2,
                                    dow, "morning", name1, name2, databaseHelper);
                        }
                    });

                    // Listener for Dayshift 2
                    dayShift2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String name1 = dayShift1.getText().toString();
                            String name2 = dayShift2.getText().toString();

                            Log.d("DayofWeek", dow);
                            fillArrayAdapters(adapterDayShift2, adapterDayShift1, dayShift2, dayShift1,
                                    dow, "morning", name2, name1, databaseHelper);
                        }
                    });

                    // Listener for Nightshift 1
                    afternoonShift1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String name1 = afternoonShift1.getText().toString();
                            String name2 = afternoonShift2.getText().toString();

                            Log.d("DayofWeek", dow);
                            fillArrayAdapters(adapterafternoonShift1, adapterafternoonShift2, afternoonShift1, afternoonShift2,
                                    dow, "afternoon", name1, name2, databaseHelper);
                        }
                    });


                    // Listener for Nightshift 2
                    afternoonShift2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String name1 = afternoonShift1.getText().toString();
                            String name2 = afternoonShift2.getText().toString();

                            Log.d("DayofWeek", dow);
                            fillArrayAdapters(adapterafternoonShift2, adapterafternoonShift1, afternoonShift2, afternoonShift1,
                                    dow, "afternoon", name2, name1, databaseHelper);
                        }
                    });

                }


                // Confirm button
                confirmButton = addView.findViewById(R.id.confirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selected = 0;
                        int selectedweekends = 0;

                        // Obtains the selections for each box of a given day
                        String daySelection1;
                        String daySelection2;
                        String nightSelection1;
                        String nightSelection2;
                        String fulldaySelection1;
                        String fulldaySelection2;

                        ImageView dayImage = view.findViewById(R.id.shiftStatus);
                        dayImage.setImageDrawable(null);

                        if (addView.getId() == R.id.assign_shifts_weekends) {
                            fulldaySelection1 = fulldayShift1.getText().toString();
                            fulldaySelection2 = fulldayShift2.getText().toString();

                            if (!fulldaySelection1.isEmpty() || !fulldaySelection2.isEmpty()) {
                                dayImage.setImageResource(R.mipmap.warning);
                            }
                            if (!fulldaySelection1.isEmpty() && !fulldaySelection2.isEmpty()) {
                                dayImage.setImageResource(R.mipmap.accept);
                            }
                            if (fulldaySelection1.isEmpty() && fulldaySelection2.isEmpty()) {
                                dayImage.setImageResource(R.mipmap.exclamation);
                            }


                            databaseHelper.insertOrUpdateDailyAssignments(dateString, null, null, null,
                                    null, null, null, fulldaySelection1, fulldaySelection2, weekNumber);
                        } else {
                            daySelection1 = dayShift1.getText().toString();
                            daySelection2 = dayShift2.getText().toString();
                            nightSelection1 = afternoonShift1.getText().toString();
                            nightSelection2 = afternoonShift2.getText().toString();
                            databaseHelper.insertOrUpdateDailyAssignments(dateString, daySelection1, daySelection2, null,
                                    nightSelection1, nightSelection2, null, null, null, weekNumber);
                        }


//                        SharedPreferences preferences = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        // stores the selection according to the specified day in 'Date'
//                        editor.putString("dayShift1_" + Date, appendNames(preferences.getString("dayShift1_" + Date, ""), daySelection1));
//                        editor.putString("dayShift2_" + Date, appendNames(preferences.getString("dayShift2_" + Date, ""), daySelection2));
//                        editor.putString("nightShift1_" + Date, appendNames(preferences.getString("nightShift1_" + Date, ""), nightSelection1));
//                        editor.putString("nightShift2_" + Date, appendNames(preferences.getString("nightShift2_" + Date, ""), nightSelection2));
//
//                        editor.apply(); // stores the preferences

//                        if (!daySelection1.isEmpty()) {
//                            selected++;
//                        }
//                        if (!daySelection2.isEmpty()) {
//                            selected++;
//                        }
//                        if (!nightSelection1.isEmpty()) {
//                            selected++;
//                        }
//
//                        if (!nightSelection2.isEmpty()) {
//                            selected++;
//                        }
//
//                        if (!fulldaySelection1.isEmpty()) {
//                            selectedweekends++;
//                        }
//                        if (!fulldaySelection2.isEmpty()) {
//                            selectedweekends++;
//                        }
//                        ImageView dayImage = view.findViewById(R.id.shiftStatus);
//                        dayImage.setImageDrawable(null);
//                        if (selected == 4 || selectedweekends == 2) {
//                            // Variables for setting calendar icons
//                            dayImage.setImageResource(R.mipmap.accept);
//                        }
//
//                        else if (selected > 0 || selectedweekends > 0) {
//                            dayImage.setImageResource(R.mipmap.warning);
//                        }
//
//                        else {
//                            dayImage.setImageResource(R.mipmap.exclamation);
//                        }

                        alertDialog.dismiss(); // Closes assign shifts page
                    }
                });
            }
        });

        // Call setup for calendar fragment
        setUpCalendar(requireContext());

        return root;
    }

    private ArrayAdapter<String> setupAdapters(String date, String dow, String tod) {
        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                "nightshift1_employee", "nightshift2_employee",
                "fullday1_employee", "fullday2_employee"};
        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{date});
        List<String> shiftNames = new ArrayList<>();

        
        shiftNames.add("");

        String[] columns = {"preferred_name"};
        String selection = dow + "_" + tod + " = ?";
        if (employees != null && !employees.isEmpty()) {
            List<String> selectionArgsList = new ArrayList<>(Arrays.asList("1"));
            for (String employee : employees) {
                if (employee != null) {
                    selection += " AND preferred_name != ?";
                    selectionArgsList.add(employee);
                }
            }
            String[] selectionArgs = selectionArgsList.toArray(new String[0]);
            shiftNames.addAll(databaseHelper.getAllEmployeePreferredNames(columns, selection, selectionArgs,null,null,null));
            // Create the adapterNames only once
            return new ArrayAdapter<>(requireContext(), R.layout.list_names, shiftNames);
        } else {
            String[] selectionArgs = new String[]{"1"};
            shiftNames.addAll(databaseHelper.getAllEmployeePreferredNames(columns, selection, selectionArgs, null, null, null));
            for (String emp : shiftNames) {
                Log.d("Employees", emp);
            }
            // Create the adapterNames only once
            return new ArrayAdapter<>(requireContext(), R.layout.list_names, shiftNames);
        }
    }

    private void fillArrayAdapters(ArrayAdapter<String> shiftAdapter1, ArrayAdapter<String> shiftAdapter2,
                                   AutoCompleteTextView shiftName1, AutoCompleteTextView shiftName2,
                                   String dow, String tod, String name1, String name2,
                                   DatabaseHelper databaseHelper) {
        List<String> shiftNames1 = new ArrayList<>();
        List<String> shiftNames2 = new ArrayList<>();
        String[] columns = {"preferred_name"};
        String selection1 = dow + "_" + tod + " = ?";
        String selection2 = dow + "_" + tod + " = ?";
        List<String> selectionArgsList1 = new ArrayList<>(Arrays.asList("1"));
        List<String> selectionArgsList2 = new ArrayList<>(Arrays.asList("1"));

        boolean name1_trained_opening = databaseHelper.isEmployeeTrainedOpening(name1);
        boolean name2_trained_opening = databaseHelper.isEmployeeTrainedOpening(name2);

        boolean name1_trained_closing = databaseHelper.isEmployeeTrainedClosing(name1);
        boolean name2_trained_closing = databaseHelper.isEmployeeTrainedClosing(name2);

        shiftNames1.add("");
        shiftNames2.add("");

        if (tod.equals("morning")) {
            if (!name1.isEmpty()) {
                selection1 += " AND preferred_name != ?";
                selection2 += " AND preferred_name != ?";
                selectionArgsList1.add(name1);
                selectionArgsList2.add(name1);
            }
            if (!name2.isEmpty()) {
                selection1 += " AND preferred_name != ?";
                selection2 += " AND preferred_name != ?";
                selectionArgsList1.add(name2);
                selectionArgsList2.add(name2);
            }

            if (!name1.isEmpty() && name2.isEmpty()) {
                if (!name1_trained_opening) {
                    selection2 += " AND trained_opening = ?";
                    selectionArgsList2.add("1");
                }
            } else if (name1.isEmpty() && !name2.isEmpty()) {
                if (!name2_trained_opening) {
                    selection1 += " AND trained_opening = ?";
                    selectionArgsList1.add("1");
                }
            } else if (!name1.isEmpty() && !name2.isEmpty()) {
                if (!name1_trained_opening) {
                    selection2 += " AND trained_opening = ?";
                    selectionArgsList2.add("1");
                } else if (!name2_trained_opening) {
                    selection1 += " AND trained_opening = ?";
                    selectionArgsList1.add("1");
                }
            }
        } else if (tod.equals("afternoon")) {
            if (!name1.isEmpty()) {
                selection1 += " AND preferred_name != ?";
                selection2 += " AND preferred_name != ?";
                selectionArgsList1.add(name1);
                selectionArgsList2.add(name1);
            }
            if (!name2.isEmpty()) {
                selection1 += " AND preferred_name != ?";
                selection2 += " AND preferred_name != ?";
                selectionArgsList1.add(name2);
                selectionArgsList2.add(name2);
            }

            if (!name1.isEmpty() && name2.isEmpty()) {
                if (!name1_trained_closing) {
                    selection2 += " AND trained_closing = ?";
                    selectionArgsList2.add("1");
                }
            } else if (name1.isEmpty() && !name2.isEmpty()) {
                if (!name2_trained_closing) {
                    selection1 += " AND trained_closing = ?";
                    selectionArgsList1.add("1");
                }
            } else if (!name1.isEmpty() && !name2.isEmpty()) {
                if (!name1_trained_closing) {
                    selection2 += " AND trained_closing = ?";
                    selectionArgsList2.add("1");
                } else if (!name2_trained_closing) {
                    selection1 += " AND trained_closing = ?";
                    selectionArgsList1.add("1");
                }
            }
        } else if (tod.equals("fullday")) {
            // Possibly a lot of redundancy, but will fix for the next sprint
            if (!name1.isEmpty()) {
                selection1 += " AND preferred_name != ?";
                selection2 += " AND preferred_name != ?";
                selectionArgsList1.add(name1);
                selectionArgsList2.add(name1);
            }
            if (!name2.isEmpty()) {
                selection1 += " AND preferred_name != ?";
                selection2 += " AND preferred_name != ?";
                selectionArgsList1.add(name2);
                selectionArgsList2.add(name2);
            }
            if (!name1.isEmpty() && name2.isEmpty()) {
                if (name1_trained_opening && name1_trained_closing) {
                    selection2 += " AND (trained_opening = ? OR trained_closing = ?)";
                    selectionArgsList2.add("1");
                    selectionArgsList2.add("1");
                } else if (name1_trained_closing) {
                    selection2 += " AND trained_opening = ?";
                    selectionArgsList2.add("1");
                } else if (name1_trained_opening) {
                    selection2 += " AND trained_closing = ?";
                    selectionArgsList2.add("1");
                }
            } else if (name1.isEmpty() && !name2.isEmpty()) {
                if (name2_trained_opening && name2_trained_closing) {
                    selection2 += " AND trained_opening = ? OR trained_closing = ?";
                    selectionArgsList2.add("1");
                    selectionArgsList2.add("1");
                } else if (name2_trained_closing) {
                    selection1 += " AND trained_opening = ?";
                    selectionArgsList1.add("1");
                } else if (name2_trained_opening) {
                    selection1 += " AND trained_closing = ?";
                    selectionArgsList1.add("1");
                }

            } else if (!name1.isEmpty() && !name2.isEmpty()) {
                if (name1_trained_opening) {
                    selection2 += " AND trained_closing = ?";
                    selectionArgsList2.add("1");
                } else if (name1_trained_closing) {
                    selection2 += " AND trained_opening = ?";
                    selectionArgsList2.add("1");
                }
                if (name2_trained_opening) {
                    selection1 += " AND trained_closing = ?";
                    selectionArgsList1.add("1");
                } else if (name2_trained_closing) {
                    selection1 += " AND trained_opening = ?";
                    selectionArgsList1.add("1");
                }
            }
        }
//            if (!name1.isEmpty()) {
//                selection1 += " AND preferred_name != ?";
//                selection2 += " AND preferred_name != ?";
//                selectionArgsList1.add(name1);
//                selectionArgsList2.add(name1);
//            }
//            if (!name2.isEmpty()) {
//                selection1 += " AND preferred_name != ?";
//                selection2 += " AND preferred_name != ?";
//                selectionArgsList1.add(name2);
//                selectionArgsList2.add(name2);
//            }
//            if (!name1.isEmpty() && name2.isEmpty()) {
//                if (!name1_trained_closing) {
//                    selection2 += " AND trained_opening = ?";
//                    selectionArgsList2.add("1");
//                }
//            } else if (name1.isEmpty() && !name2.isEmpty()) {
//                if (!name2_trained_closing) {
//                    selection1 += " AND trained_opening = ?";
//                    selectionArgsList1.add("1");
//                }
//            } else if (!name1.isEmpty() && !name2.isEmpty()) {
//                if (!name1_trained_closing) {
//                    selection2 += " AND trained_opening = ?";
//                    selectionArgsList2.add("1");
//                } else if (!name2_trained_closing) {
//                    selection1 += " AND trained_opening = ?";
//                    selectionArgsList1.add("1");
//                }
//            }



        String[] selectionArgs1 = selectionArgsList1.toArray(new String[0]);
        String[] selectionArgs2 = selectionArgsList2.toArray(new String[0]);

        shiftNames1.addAll(databaseHelper.getAllEmployeePreferredNames(columns,
                selection1,
                selectionArgs1,
                null, null,null));
        shiftNames2.addAll(databaseHelper.getAllEmployeePreferredNames(columns,
                selection2,
                selectionArgs2,
                null, null,null));

        for (String emp2 : shiftNames2) {
            Log.d("ShiftNames2", emp2);
        }
        if (shiftAdapter1 == null) {
            shiftAdapter1 = new ArrayAdapter<>(requireContext(), R.layout.list_names, shiftNames1);
            shiftName1.setAdapter(shiftAdapter1);
            shiftAdapter2 = new ArrayAdapter<>(requireContext(), R.layout.list_names, shiftNames2);
            shiftName2.setAdapter(shiftAdapter2);
        } else {
            shiftAdapter1.clear();
            shiftAdapter1.addAll(shiftNames1);
            shiftAdapter1.notifyDataSetChanged();
            shiftName1.setAdapter(shiftAdapter1);

            shiftAdapter2.clear();
            shiftAdapter2.addAll(shiftNames2);
            shiftAdapter2.notifyDataSetChanged();
            shiftName2.setAdapter(shiftAdapter2);
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

    public static Date[] getWeekDates(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date startDate = cal.getTime();

        cal.add(Calendar.DATE, 6);
        Date endDate = cal.getTime();
        return new Date[]{startDate, endDate};
    }
    private String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case 0:
                return "sunday";
            case 1:
                return "monday";
            case 2:
                return "tuesday";
            case 3:
                return "wednesday";
            case 4:
                return "thursday";
            case 5:
                return "friday";
            case 6:
                return "saturday";
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
    private void showMenu(View v, int weekNumber, String dateString) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        List<String> unscheduledEmployees = databaseHelper.getUnscheduledEmployeeForWeek(weekNumber); // list of employees
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        Date[] weekDates = getWeekDates(dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("Date", String.valueOf(weekDates[0]));
        Log.d("Date",  dateFormat.format(weekDates[1]));
        String startDate = String.valueOf(dateFormat.format(weekDates[0]));
        String endDate = String.valueOf(dateFormat.format(weekDates[1]));
        String datesofWeek = startDate + " - " + endDate;
        popupMenu.getMenu().add(Menu.NONE, Menu.NONE, Menu.NONE, datesofWeek);
        popupMenu.getMenu().add(Menu.NONE, Menu.NONE, Menu.NONE, "Unscheduled Employees:");
        // Dynamically add names to pop up
        for (String name : unscheduledEmployees) {
            popupMenu.getMenu().add(Menu.NONE, Menu.NONE, unscheduledEmployees.indexOf(name), name);
        }
        // Set up a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the selected menu item click
                switch (item.getItemId()) {
                    case Menu.NONE:
                        // Handle click on dynamically added menu item
                        String selectedName = item.getTitle().toString();
                        //Toast.makeText(getApplicationContext(), selectedName + " clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }
}
