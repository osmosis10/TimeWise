package com.example.shiftmanager.ui.employees;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.MainActivity;
import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentEmployeesBinding;
import com.example.shiftmanager.databinding.FragmentEmployeesBinding;
import com.example.shiftmanager.ui.database.DatabaseHelper;
import android.database.Cursor;
import org.w3c.dom.Text;

import java.util.List;

public class EmployeesFragment extends Fragment {

    private FragmentEmployeesBinding binding;
    private EmployeesViewModel employeesViewModel;
    // Define ActivityResultLauncher
    private ActivityResultLauncher<Intent> addEmployeeLauncher;

    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());

        employeesViewModel = new ViewModelProvider(this).get(EmployeesViewModel.class);
        employeesViewModel.getCleanAndPopulateComplete().observe(this, cleanAndPopulateCompleted -> {
            Log.d("cleanAndPopulate", "Value: " + cleanAndPopulateCompleted);
            if (!cleanAndPopulateCompleted) {
                dbHelper.removeAllEmployees();
                dbHelper.insertRandomEmployees();
                employeesViewModel.markCleanAndPopulateComplete();
            }
        });

        // Initialize the ActivityResultLauncher
        addEmployeeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Get the employee name from the intent
                        // We retrieve everything that makes up an employee from intent
                        String employeeName = result.getData().getStringExtra("employeeName");
                        String employeePhone = result.getData().getStringExtra("employeePhone");
                        String employeeEmail = result.getData().getStringExtra("employeeEmail");
                        String employeeStartDate = result.getData().getStringExtra("employeeStartDate");
                        boolean mondayMorning = result.getData().getBooleanExtra("mondayMorning", false);
                        boolean mondayAfternoon = result.getData().getBooleanExtra("mondayAfternoon", false);
                        boolean tuesdayMorning = result.getData().getBooleanExtra("tuesdayMorning", false);
                        boolean tuesdayAfternoon = result.getData().getBooleanExtra("tuesdayAfternoon", false);
                        boolean wednesdayMorning = result.getData().getBooleanExtra("wednesdayMorning", false);
                        boolean wednesdayAfternoon = result.getData().getBooleanExtra("wednesdayAfternoon", false);
                        boolean thursdayMorning = result.getData().getBooleanExtra("thursdayMorning", false);
                        boolean thursdayAfternoon = result.getData().getBooleanExtra("thursdayAfternoon", false);
                        boolean fridayMorning = result.getData().getBooleanExtra("fridayMorning", false);
                        boolean fridayAfternoon = result.getData().getBooleanExtra("fridayAfternoon", false);
                        boolean saturdayFullday = result.getData().getBooleanExtra("saturdayFullday", false);
                        boolean sundayFullday = result.getData().getBooleanExtra("sundayFullday", false);

                        // Update the UI with the employee name
                        //addEmployeeNameToUI(employeeName);
                        // Save our employee to the database
                        saveEmployeeToDB(employeeName, employeePhone, employeeEmail, employeeStartDate,
                                        mondayMorning, mondayAfternoon, tuesdayMorning, tuesdayAfternoon,
                                        wednesdayMorning, wednesdayAfternoon, thursdayMorning, thursdayAfternoon,
                                        fridayMorning, fridayAfternoon, saturdayFullday, sundayFullday, 0);
                    }
                }
        );
    }

    /*
    Insert the employee into our database
     */
    private void saveEmployeeToDB(String employeeName, String employeePhone, String employeeEmail,
                                  String employeeStartDate, boolean mondayMorning, boolean mondayAfternoon,
                                  boolean tuesdayMorning, boolean tuesdayAfternoon, boolean wednesdayMorning,
                                  boolean wednesdayAfternoon, boolean thursdayMorning, boolean thursdayAfternoon,
                                  boolean fridayMorning, boolean fridayAfternoon, boolean saturdayFullday,
                                  boolean sundayFullday, int trained) {
        //dbHelper = new DatabaseHelper(requireContext());
        dbHelper.insertEmployee(employeeName,
                                employeePhone,
                                employeeEmail,
                                employeeStartDate,
                                mondayMorning, mondayAfternoon,
                                tuesdayMorning, tuesdayAfternoon,
                                wednesdayMorning, wednesdayAfternoon,
                                thursdayMorning, thursdayAfternoon,
                                fridayMorning, fridayAfternoon,
                                saturdayFullday, sundayFullday, trained);

        addEmployeeNameToUI(employeeName);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false);

        // Setup addEmployeeButton click listener
        binding.EmployeeAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), addEmployee.class);
            addEmployeeLauncher.launch(intent);
        });

        // Update employee fragment with all employees in database
        updateEmployeeNamesUI();
        return binding.getRoot();
    }
    /*
    Looks through db and queries for employee names
    If the employee name is already displayed it wont display them
    calls addEmployeeToUI in order to display names that are already in the db
     */
    private void updateEmployeeNamesUI() {
        List<String> employeeNames = dbHelper.getAllEmployeeNames();

        for (String employeeName : employeeNames) {
            if (!isEmployeeNameInUI(employeeName)) {
                Log.d("EmployeeNames", "Employee Name: " + employeeName);
                addEmployeeNameToUI(employeeName);
            }
        }
    }

    /*
        Checks each employeeName against each child in the Employee Container
        The container containing all the names in the Employee UI
        Returns try if the employee is already in the container, else false
     */
    private boolean isEmployeeNameInUI(String employeeName) {
        int childCount = binding.EmployeeContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = binding.EmployeeContainer.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (textView.getText().toString().equals(employeeName)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void addEmployeeNameToUI(String employeeName) {
        TextView employeeNameView = new TextView(getContext());
        employeeNameView.setText(employeeName);
        employeeNameView.setTextSize(30);
        employeeNameView.setTextColor(Color.WHITE);
        employeeNameView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_background)); // Set the rounded corner background

        // Set layout parameters to center the name horizontally
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        int marginTopPx = (int) (10 * getResources().getDisplayMetrics().density);
        layoutParams.setMargins(0, marginTopPx, 0, 0); // Apply top margin
        employeeNameView.setLayoutParams(layoutParams);

        int paddingDp = 8; // Example padding in dp
        int paddingPx = (int) (paddingDp * getResources().getDisplayMetrics().density);
        employeeNameView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx); // Apply padding
        employeeNameView.setWidth(650);
        employeeNameView.setHeight(100);

        binding.EmployeeContainer.addView(employeeNameView); // Add the name TextView to employee layout

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.close();
        binding = null;
    }
}