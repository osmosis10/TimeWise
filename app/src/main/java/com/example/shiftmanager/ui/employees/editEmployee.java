package com.example.shiftmanager.ui.employees;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.R;
import com.example.shiftmanager.ui.database.DatabaseHelper;

import java.util.List;

public class editEmployee extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(editEmployee.this);

        setContentView(R.layout.edit_employee);

        EmployeesViewModel employeesViewModel = new ViewModelProvider(this).get(EmployeesViewModel.class);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Intent intent = getIntent();
        String preferredName = intent.getStringExtra("preferredName");
        getWindow().setLayout((int) (width), (int) (height));

        ImageButton closeButton = findViewById(R.id.EditEmployeeCloseButton);

        List<String> employeeInformation = dbHelper.getEmployeeInformation(preferredName);

        Log.d(preferredName, "Preferred Name: " + employeeInformation);

        EditText EditTextFirstName = findViewById(R.id.EditEmployeeFirstNameInput);
        EditTextFirstName.setText(employeeInformation.get(0));
        EditText EditTextLastName = findViewById(R.id.EditEmployeeLastNameInput);
        EditTextLastName.setText(employeeInformation.get(1));
        EditText EditTextPreferredName = findViewById(R.id.EditEmployeePreferredNameInput);
        EditTextPreferredName.setText(employeeInformation.get(2));
        EditText EditTextPhone = findViewById(R.id.EditEmployeePhoneInput);
        EditTextPhone.setText(employeeInformation.get(3));
        EditText EditTextEmail = findViewById(R.id.EditEmployeeEmailInput);
        EditTextEmail.setText(employeeInformation.get(4));

        CheckBox checkBoxMondayMorning = findViewById(R.id.EditEmployeeMondayMorningCheckbox);
        checkBoxMondayMorning.setChecked("1".equals(employeeInformation.get(6)));
        CheckBox checkBoxMondayAfternoon = findViewById(R.id.EditEmployeeMondayAfternoonCheckbox);
        checkBoxMondayAfternoon.setChecked("1".equals(employeeInformation.get(7)));

        CheckBox checkBoxTuesdayMorning = findViewById(R.id.EditEmployeeTuesdayMorningCheckbox);
        checkBoxTuesdayMorning.setChecked("1".equals(employeeInformation.get(8)));
        CheckBox checkBoxTuesdayAfternoon = findViewById(R.id.EditEmployeeTuesdayAfternoonCheckbox);
        checkBoxTuesdayAfternoon.setChecked("1".equals(employeeInformation.get(9)));

        CheckBox checkBoxWednesdayMorning = findViewById(R.id.EditEmployeeWednesdayMorningCheckbox);
        checkBoxWednesdayMorning.setChecked("1".equals(employeeInformation.get(10)));
        CheckBox checkBoxWednesdayAfternoon = findViewById(R.id.EditEmployeeWednesdayAfternoonCheckbox);
        checkBoxWednesdayAfternoon.setChecked("1".equals(employeeInformation.get(11)));

        CheckBox checkBoxThursdayMorning = findViewById(R.id.EditEmployeeThursdayMorningCheckbox);
        checkBoxThursdayMorning.setChecked("1".equals(employeeInformation.get(12)));
        CheckBox checkBoxThursdayAfternoon = findViewById(R.id.EditEmployeeThursdayAfternoonCheckbox);
        checkBoxThursdayAfternoon.setChecked("1".equals(employeeInformation.get(13)));

        CheckBox checkBoxFridayMorning = findViewById(R.id.EditEmployeeFridayMorningCheckbox);
        checkBoxFridayMorning.setChecked("1".equals(employeeInformation.get(14)));
        CheckBox checkBoxFridayAfternoon = findViewById(R.id.EditEmployeeFridayAfternoonCheckbox);
        checkBoxFridayAfternoon.setChecked("1".equals(employeeInformation.get(15)));

        CheckBox checkBoxSaturday = findViewById(R.id.EditEmployeeSatrudayFulldayCheckbox);
        checkBoxSaturday.setChecked("1".equals(employeeInformation.get(16)));

        CheckBox checkBoxSunday = findViewById(R.id.EditEmployeeSundayFulldayCheckbox);
        checkBoxSunday.setChecked("1".equals(employeeInformation.get(17)));

        CheckBox checkBoxTrained = findViewById(R.id.EditEmployeeTrainedCheckBox);
        checkBoxTrained.setChecked("1".equals(employeeInformation.get(18)));
//        TextView textViewLastName = findViewById(R.id.EditEmployeeLastNameInput);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addButton = findViewById(R.id.EditEmployeeAddButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate the form first
                if (!validateForm()) {
                    // If the form is not valid, stop the method execution here, and return error
                    return;
                }

                // Employee information
                String first_name = ((EditText) findViewById(R.id.EditEmployeeFirstNameInput)).getText().toString();
                String last_name = ((EditText) findViewById(R.id.EditEmployeeLastNameInput)).getText().toString();
                String preferred_name = ((EditText) findViewById(R.id.EditEmployeePreferredNameInput)).getText().toString();

                // Check if the preferred name input is empty
                if (preferred_name.isEmpty()) {
                    // If its empty assign the first name as preferred + suffix if needed
                    preferred_name = dbHelper.getUniquePreferredName(first_name);
                } else {
                    // If its not empty, still check if there are similar names in the db
                    preferred_name = dbHelper.getUniquePreferredName(preferred_name);
                }

                String phone = ((EditText) findViewById(R.id.EditEmployeePhoneInput)).getText().toString();
                String email = ((EditText) findViewById(R.id.EditEmployeeEmailInput)).getText().toString();

                // Availability
                boolean mondayMorning = ((CheckBox) findViewById(R.id.EditEmployeeMondayMorningCheckbox)).isChecked();
                boolean mondayAfternoon = ((CheckBox) findViewById(R.id.EditEmployeeMondayAfternoonCheckbox)).isChecked();
                boolean tuesdayMorning = ((CheckBox) findViewById(R.id.EditEmployeeTuesdayMorningCheckbox)).isChecked();
                boolean tuesdayAfternoon = ((CheckBox) findViewById(R.id.EditEmployeeTuesdayAfternoonCheckbox)).isChecked();
                boolean wednesdayMorning = ((CheckBox) findViewById(R.id.EditEmployeeWednesdayMorningCheckbox)).isChecked();
                boolean wednesdayAfternoon = ((CheckBox) findViewById(R.id.EditEmployeeWednesdayAfternoonCheckbox)).isChecked();
                boolean thursdayMorning = ((CheckBox) findViewById(R.id.EditEmployeeThursdayMorningCheckbox)).isChecked();
                boolean thursdayAfternoon = ((CheckBox) findViewById(R.id.EditEmployeeThursdayAfternoonCheckbox)).isChecked();
                boolean fridayMorning = ((CheckBox) findViewById(R.id.EditEmployeeFridayMorningCheckbox)).isChecked();
                boolean fridayAfternoon = ((CheckBox) findViewById(R.id.EditEmployeeFridayAfternoonCheckbox)).isChecked();
                boolean saturdayFullday = ((CheckBox) findViewById(R.id.EditEmployeeSatrudayFulldayCheckbox)).isChecked();
                boolean sundayFullday = ((CheckBox) findViewById(R.id.EditEmployeeSundayFulldayCheckbox)).isChecked();
                boolean trained = ((CheckBox) findViewById(R.id.EditEmployeeTrainedCheckBox)).isChecked();

                dbHelper.updateEmployeeInformation(preferredName, first_name, last_name, preferred_name, phone, email,
                        mondayMorning, mondayAfternoon, tuesdayMorning, tuesdayAfternoon, wednesdayMorning,
                        wednesdayAfternoon, thursdayMorning, thursdayAfternoon, fridayMorning, fridayAfternoon,
                        saturdayFullday, sundayFullday, trained);

                setResult(Activity.RESULT_OK);
                // Close the current activity and return to the previous screen
                finish();
            }
        });


    }



    private boolean validateForm() {
        // The only validation needs to be whether the new entered preferred name already exits.
        return true; // Form is valid
    }
}
