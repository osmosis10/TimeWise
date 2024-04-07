package com.example.shiftmanager.ui.employees;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.text.Editable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.Nullable;
import com.example.shiftmanager.R;
import com.example.shiftmanager.ui.database.DatabaseHelper;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Locale;
public class addEmployee extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(addEmployee.this);

        setContentView(R.layout.add_employee);

        // Ensure valid and correct formatting for phone number
        EditText phoneInput = findViewById(R.id.AddEmployeePhoneInput);
        phoneInput.addTextChangedListener(createPhoneNumberFormatter());

        // Prevents keyboard from opening for date input
        EditText startDateInput = findViewById(R.id.AddEmployeeStartDateInput);
        startDateInput.setFocusable(false);

        // Ensure valid first and last names
        EditText firstNameInput = findViewById(R.id.AddEmployeeFirstNameInput);
        EditText lastNameInput = findViewById(R.id.AddEmployeeLastNameInput);
        firstNameInput.addTextChangedListener(createNameValidationWatcher(firstNameInput));
        lastNameInput.addTextChangedListener(createNameValidationWatcher(lastNameInput));

        // Ensure valid email input
        EditText emailInput = findViewById(R.id.AddEmployeeEmailInput);
        emailInput.addTextChangedListener(createEmailValidationWatcher(emailInput));

        // Create a listener for the date input, to open Calendar Dialog
        startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog();
            }
        });

        EmployeesViewModel employeesViewModel = new ViewModelProvider(this).get(EmployeesViewModel.class);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Dimensions
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));

        ImageButton closeButton = findViewById(R.id.AddEmployeeCloseButton);

        // Listener for Close Button ot finish the activity
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addButton = findViewById(R.id.AddEmployeeAddButton);

        // Listener for Add Button to create Employee
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate the form first
                if (!validateForm()) {
                    // If the form is not valid, stop the method execution here, and return error
                    return;
                }

                // Employee information
                String first_name = ((EditText) findViewById(R.id.AddEmployeeFirstNameInput)).getText().toString();
                String last_name = ((EditText) findViewById(R.id.AddEmployeeLastNameInput)).getText().toString();
                String preferred_name = ((EditText) findViewById(R.id.AddEmployeePreferredNameInput)).getText().toString();

                // Check if the preferred name input is empty
                if (preferred_name.isEmpty()) {
                    // If its empty assign the first name as preferred + suffix if needed
                    preferred_name = dbHelper.getUniquePreferredName(first_name);
                } else {
                    // If its not empty, still check if there are similar names in the db
                    preferred_name = dbHelper.getUniquePreferredName(preferred_name);
                }

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
                boolean trained_opening = ((CheckBox) findViewById(R.id.TrainedOpeningCheckbox)).isChecked();
                boolean trained_closing = ((CheckBox) findViewById(R.id.TrainedClosingCheckbox)).isChecked();
                boolean is_archived = false;

                // Create the Employee object
                Employee newEmployee = new Employee(first_name, last_name, preferred_name,
                        phone, email, startDate,
                        mondayMorning, mondayAfternoon,
                        tuesdayMorning, tuesdayAfternoon,
                        wednesdayMorning, wednesdayAfternoon,
                        thursdayMorning, thursdayAfternoon,
                        fridayMorning, fridayAfternoon,
                        saturdayFullday, sundayFullday,
                        trained_opening, trained_closing,
                        is_archived);

                Intent data = new Intent();
                data.putExtra("employeeFirstName", first_name);
                data.putExtra("employeeLastName", last_name);
                data.putExtra("employeePreferredName", preferred_name);
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
                data.putExtra("trainedOpening", trained_opening);
                data.putExtra("trainedClosing", trained_closing);
                data.putExtra("is_archived", is_archived);


                setResult(Activity.RESULT_OK, data);
                // Close the current activity and return to the previous screen
                finish();
            }
        });


    }

    // Method to open Calendar Dialog
    private void showCalendarDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format the date properly
                        String selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        EditText startDateInput = findViewById(R.id.AddEmployeeStartDateInput);
                        startDateInput.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Method to watch and format phone number input field
    private TextWatcher createPhoneNumberFormatter() {
        return new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to add anything for this method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need to add anything for this method
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString();
                if (currentText.equals(previousText)) {
                    return; // No change in the text, no need to reformat
                }

                // Removing every character except digits
                String digits = currentText.replaceAll("[^\\d]", "");
                StringBuilder formatted = new StringBuilder();

                // Allow only up to 10 digits
                int digitCount = Math.min(digits.length(), 10);

                for (int i = 0; i < digitCount; i++) {
                    char c = digits.charAt(i);
                    if (i == 3 || i == 6) {
                        // Insert dashes after 3rd and 6th digits, for formatting
                        formatted.append("-");
                    }
                    formatted.append(c);
                }

                previousText = formatted.toString();
                // To avoid infinite loop with setText, use replace on Editable directly
                s.replace(0, s.length(), formatted.toString());
            }
        };
    }

    // Method to ensure the phone number is valid
    private boolean isValidPhone(String phone) {
        // Remove any non-digit characters from the phone number
        String digitsOnly = phone.replaceAll("[^\\d]", "");
        // Ensure the resulting string has exactly 10 digits
        return digitsOnly.length() == 10;
    }

    // Method to check if email is valid using regex expression
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        return email.matches(emailRegex);
    }



    // Method to watch the email input, ensuring its valid
    private TextWatcher createEmailValidationWatcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to add anything for this method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need to add anything for this method
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isValidEmail(s.toString())) {
                    // Show an error message
                    editText.setError("Invalid email. Please follow the email rules.");
                }
            }
        };
    }

    // Method to check whether a name is valid, based on Government of Canada name restrictions
    private boolean isValidName(String name) {
        // Check for initial letter and allowed characters, letters, hyphens, apostrophes, periods, and/or spaces
        if (!name.matches("^[A-Za-z][A-Za-z\\s.'-]*$")) {
            return false;
        }

        // Don't allow consecutive invalid characters (hyphens, apostrophes, periods)
        if (name.contains("..") || name.contains("--") || name.contains("''")) {
            return false;
        }

        // Ensure the name does not consist only of punctuation character
        if (name.matches("^['.-]+$")) {
            return false;
        }

        // Reutrn true if valid name
        return true;
    }

    // Method to watch the First name and Last name fields, ensuring correct formatting
    private TextWatcher createNameValidationWatcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isValidName(s.toString())) {
                    // Return an error message, if the name is invalid
                    editText.setError("Invalid name. Please follow the naming rules.");
                }
            }
        };
    }

    // Validates the form information
    private boolean validateForm() {
        // Get input fields information
        EditText firstNameInput = findViewById(R.id.AddEmployeeFirstNameInput);
        EditText lastNameInput = findViewById(R.id.AddEmployeeLastNameInput);
        EditText phoneInput = findViewById(R.id.AddEmployeePhoneInput);
        EditText emailInput = findViewById(R.id.AddEmployeeEmailInput);
        EditText preferredNameInput = findViewById(R.id.AddEmployeePreferredNameInput);

        // Check if any required field is empty or invalid
        if (TextUtils.isEmpty(firstNameInput.getText().toString().trim()) ||
                TextUtils.isEmpty(lastNameInput.getText().toString().trim()) ||
                TextUtils.isEmpty(phoneInput.getText().toString().trim()) ||
                TextUtils.isEmpty(emailInput.getText().toString().trim()) ||
                !isValidEmail(emailInput.getText().toString().trim()) ||
                !isValidName(firstNameInput.getText().toString().trim()) ||
                !isValidName(lastNameInput.getText().toString().trim()) ||
                !isValidPhone(phoneInput.getText().toString().trim())
        ) {
            if (TextUtils.isEmpty(firstNameInput.getText().toString().trim()) ||
                    TextUtils.isEmpty(lastNameInput.getText().toString().trim()) ||
                    TextUtils.isEmpty(phoneInput.getText().toString().trim()) ||
                    TextUtils.isEmpty(emailInput.getText().toString().trim())) {
                Toast.makeText(this, "All fields with * must be filled", Toast.LENGTH_LONG).show();
                return false; // Form is not valid
            } else if (!isValidEmail(emailInput.getText().toString().trim())) {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
                return false;
            } else if (!isValidName(firstNameInput.getText().toString().trim())) {
                Toast.makeText(this, "Please enter a valid first name.", Toast.LENGTH_LONG).show();
                return false;
            } else if (!isValidName(lastNameInput.getText().toString().trim())) {
                Toast.makeText(this, "Please enter a valid last name.", Toast.LENGTH_LONG).show();
                return false;
            } else if (!isValidPhone(phoneInput.getText().toString().trim())) {
                Toast.makeText(this, "Please enter a valid phone number.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true; // Form is valid
    }

}
