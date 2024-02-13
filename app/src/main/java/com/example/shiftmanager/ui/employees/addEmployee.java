package com.example.shiftmanager.ui.employees;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentEmployeesBinding;

public class addEmployee extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employee);

        EmployeesViewModel employeesViewModel = new ViewModelProvider(this).get(EmployeesViewModel.class);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));

        ImageButton closeButton = findViewById(R.id.AddEmployeeCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addButton = findViewById(R.id.AddEmployeeAddButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate the form first
                if (!validateForm()) {
                    // If the form is not valid, stop the method execution here, and return error
                    return;
                }

                // Employee information
                String name = ((EditText) findViewById(R.id.AddEmployeeNameInput)).getText().toString();
                String phone = ((EditText) findViewById(R.id.AddEmployeePhoneInput)).getText().toString();
                String email = ((EditText) findViewById(R.id.AddEmployeeEmailInput)).getText().toString();
                String startDate = ((EditText) findViewById(R.id.AddEmployeeStartDateInput)).getText().toString();

                // Availability
                boolean mondayMorning = ((CheckBox) findViewById(R.id.AddEmployeeMondayMorningCheckbox)).isChecked();
                boolean mondayAfternoon = ((CheckBox) findViewById(R.id.AddEmployeeMondayAfternoonCheckbox)).isChecked();
                boolean tuesdayMorning = ((CheckBox) findViewById(R.id.AddEmployeeTuesdayMorningCheckbox)).isChecked();
                boolean tuesdayAfternoon = ((CheckBox) findViewById(R.id.AddEmployeeTuesdayAfternoonCheckbox)).isChecked();
                boolean wednesdayMorning = ((CheckBox) findViewById(R.id.AddEmployeeWednesdayMorningCheckbox)).isChecked();
                boolean wednesdayAfternoon = ((CheckBox) findViewById(R.id.AddEmployeeWednesdayAfternoonCheckbox)).isChecked();
                boolean thursdayMorning = ((CheckBox) findViewById(R.id.AddEmployeeThursdayMorningCheckbox)).isChecked();
                boolean thursdayAfternoon = ((CheckBox) findViewById(R.id.AddEmployeeThursdayAfternoonCheckbox)).isChecked();
                boolean fridayMorning = ((CheckBox) findViewById(R.id.AddEmployeeFridayMorningCheckbox)).isChecked();
                boolean fridayAfternoon = ((CheckBox) findViewById(R.id.AddEmployeeFridayAfternoonCheckbox)).isChecked();
                boolean saturdayFullday = ((CheckBox) findViewById(R.id.AddEmployeeSatrudayFulldayCheckbox)).isChecked();
                boolean sundayFullday = ((CheckBox) findViewById(R.id.AddEmployeeSundayFulldayCheckbox)).isChecked();

                // Create the Employee object
                Employee newEmployee = new Employee(name, phone, email, startDate,
                        mondayMorning, mondayAfternoon,
                        tuesdayMorning, tuesdayAfternoon,
                        wednesdayMorning, wednesdayAfternoon,
                        thursdayMorning, thursdayAfternoon,
                        fridayMorning, fridayAfternoon,
                        saturdayFullday, sundayFullday);

                Intent data = new Intent();
                data.putExtra("employeeName", name);
                // Added the rest of the employee information to pass to another instance
                // Used to create an employee and insert into db
                data.putExtra("employeePhone", phone);
                data.putExtra("employeeEmail", email);
                data.putExtra("employeeStartDate", startDate);
                data.putExtra("mondayMorning", mondayMorning);
                data.putExtra("mondayAfternoon", mondayAfternoon);
                data.putExtra("tuesdayMorning", tuesdayMorning);
                data.putExtra("tuesdayAfternoon", tuesdayAfternoon);
                data.putExtra("wednesdayMorning", wednesdayMorning);
                data.putExtra("wednesdayAfternoon", wednesdayAfternoon);
                data.putExtra("thursdayMorning", thursdayMorning);
                data.putExtra("thursdayAfternoon", thursdayAfternoon);
                data.putExtra("fridayMorning", fridayMorning);
                data.putExtra("fridayAfternoon", fridayAfternoon);
                data.putExtra("saturdayFullday", saturdayFullday);
                data.putExtra("sundayFullday", sundayFullday);

                setResult(Activity.RESULT_OK, data);
                // Close the current activity and return to the previous screen
                finish();
            }
        });


    }

    private boolean validateForm() {
        EditText nameInput = findViewById(R.id.AddEmployeeNameInput);
        EditText phoneInput = findViewById(R.id.AddEmployeePhoneInput);
        EditText emailInput = findViewById(R.id.AddEmployeeStartDateInput);

        // Check if any required field is empty
        if (TextUtils.isEmpty(nameInput.getText().toString().trim()) ||
                TextUtils.isEmpty(phoneInput.getText().toString().trim()) ||
                TextUtils.isEmpty(emailInput.getText().toString().trim())) {

            // Show a general error message
            Toast.makeText(this, "All fields with * must be filled", Toast.LENGTH_LONG).show();
            return false; // Form is not valid
        }
        return true; // Form is valid
    }
}
