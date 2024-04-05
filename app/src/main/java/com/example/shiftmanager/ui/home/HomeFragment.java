package com.example.shiftmanager.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentHomeBinding;
import com.example.shiftmanager.ui.database.DatabaseHelper;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper databaseHelper;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_HAS_ADDED_EMPLOYEES = "hasAddedEmployees";

    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat stringDateFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH);
    SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.ENGLISH);

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    SimpleDateFormat standardFormat = new SimpleDateFormat("YYYY-MM-DD", Locale.ENGLISH);

    TextView todayDate;

    TextView topLeft;
    TextView topRight;

    TextView middleLeft;
    TextView middleRight;
    TextView bottomLeft;
    TextView bottomRight;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseHelper = new DatabaseHelper(requireContext());

        String day = dayFormat.format(calendar.getTime());
        int intDay = Integer.parseInt(day); // get the current day as an int
        todayInfo(root, intDay);



        SharedPreferences prefs = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //Change this to true to wipe data base and add a fresh set of employees
        // then back to false to save the preference
        boolean hasAddedEmployees = prefs.getBoolean(KEY_HAS_ADDED_EMPLOYEES, false);

        if (hasAddedEmployees) {
            databaseHelper.removeAllEmployees();
            databaseHelper.makeEmployees();

            prefs.edit().putBoolean(KEY_HAS_ADDED_EMPLOYEES, true).apply();
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void todayInfo(View rootView, int day) {
        LinearLayout parentLayout = rootView.findViewById(R.id.employeeBox);
        Typeface boldTypeFace = Typeface.defaultFromStyle(Typeface.BOLD);


        // Set today's date at top of home page
        todayDate = rootView.findViewById(R.id.todayDate);
        String stringDate = stringDateFormat.format(calendar.getTime());
        todayDate.setText(stringDate);

        ImageView imageView = rootView.findViewById(R.id.icon);
        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        topLeft = rootView.findViewById(R.id.topLeft);
        topRight = rootView.findViewById(R.id.topRight);
        middleLeft = rootView.findViewById(R.id.middleLeft);
        middleRight = rootView.findViewById(R.id.middleRight);
        bottomLeft = rootView.findViewById(R.id.bottomLeft);
        bottomRight= rootView.findViewById(R.id.bottomRight);

        // Obtaining the dates for each iterated day
        String dayString = String.format("%02d", day); // obtains i'th day
        String curMonth = monthFormat.format(calendar.getTime()); // obtains current year
        String curYear = yearFormat.format(calendar.getTime()); // obtains current month
        int currentMonth = getMonthNum(curMonth);
        Calendar localCalendar = Calendar.getInstance(Locale.CANADA);
        localCalendar.set(Integer.parseInt(curYear), currentMonth, Integer.parseInt(dayString));
        String dateString = curYear + "-" + String.format("%02d", currentMonth) + "-" + dayString;

//        // Database query and employee info result
        String[] employeeColumn = {"dayshift1_employee", "dayshift2_employee",
                "nightshift1_employee", "nightshift2_employee",
                "fullday1_employee", "fullday2_employee"};

        List<String> employees = databaseHelper.getDailyAssignmentsEmployee(employeeColumn, "date = ?", new String[]{dateString});

        int counter = 5;
        // adds each employee on the i'th day of the month to the employeeNames string
        for(int j =0; j<employees.size(); j++){
            if (employees.get(j) != null) {
                Log.d("LOOP", "NOT NULL");
                if (counter == 5) {
                    topLeft.setText(employees.get(j));

                }

                if (counter == 4) {
                    topRight.setText(employees.get(j));
                }

                if (counter == 3) {
                    middleLeft.setText(employees.get(j));

                }

                if (counter == 2) {
                    middleRight.setText(employees.get(j));

                }

                if (counter == 1) {
                    bottomRight.setText(employees.get(j));

                }

                if (counter == 0) {
                    bottomRight.setText(employees.get(j));
                    resizeTextToFitContainer(bottomRight);
                }

                counter--;

            }

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

    private void resizeTextToFitContainer(TextView textView) {
        int maxLength = 20; // Maximum length of text allowed in the TextView
        float defaultTextSize = 14; // Default text size in sp
        float scaleFactor = 0.9f; // Scale factor to adjust text size

        String text = textView.getText().toString();
        if (text.length() > maxLength) {
            // Calculate new text size based on the length of the text
            float newTextSize = defaultTextSize * maxLength / text.length() * scaleFactor;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, newTextSize);
        } else {
            // Reset text size to default if the text length is within the limit
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, defaultTextSize);
        }
    }
}