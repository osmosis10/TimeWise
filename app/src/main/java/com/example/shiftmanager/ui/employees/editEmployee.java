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
import android.text.TextWatcher;
import android.text.Editable;
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

        // Dimensions
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Intent intent = getIntent();
        String preferredName = intent.getStringExtra("preferredName");
        getWindow().setLayout((int) (width), (int) (height));

        // Close Button
        ImageButton closeButton = findViewById(R.id.EditEmployeeCloseButton);

        // Listener for Close Button to finish the activity
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<String> employeeInformation = dbHelper.getEmployeeInformation(preferredName);

        Log.d(preferredName, "Preferred Name: " + employeeInformation);

        // Populate the form with the employee information
        EditText EditTextFirstName = findViewById(R.id.EditEmployeeFirstNameInput);
        // Ensure valid first name
        EditTextFirstName.addTextChangedListener(createNameValidationWatcher(EditTextFirstName));
        EditTextFirstName.setText(employeeInformation.get(0));
        EditText EditTextLastName = findViewById(R.id.EditEmployeeLastNameInput);
        // Ensure valid last name
        EditTextLastName.addTextChangedListener(createNameValidationWatcher(EditTextLastName));
        EditTextLastName.setText(employeeInformation.get(1));
        EditText EditTextPreferredName = findViewById(R.id.EditEmployeePreferredNameInput);
        EditTextPreferredName.setText(employeeInformation.get(2));
        EditText EditTextPhone = findViewById(R.id.EditEmployeePhoneInput);
        // Ensure valid and correct formatting for phone number
        EditTextPhone.addTextChangedListener(createPhoneNumberFormatter());
        EditTextPhone.setText(employeeInformation.get(3));
        EditText EditTextEmail = findViewById(R.id.EditEmployeeEmailInput);
        // Ensure valid email input
        EditTextEmail.addTextChangedListener(createEmailValidationWatcher(EditTextEmail));
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
        CheckBox checkBoxTrainedOpening = findViewById(R.id.EditEmployeeTrainedOpeningCheckBox);
        checkBoxTrainedOpening.setChecked("1".equals(employeeInformation.get(18)));
        CheckBox checkBoxTrainedClosing = findViewById(R.id.EditEmployeeTrainedClosingCheckBox);
        checkBoxTrainedClosing.setChecked("1".equals(employeeInformation.get(19)));

        Button editButton = findViewById(R.id.EditEmployeeAddButton);

        // Listener for Edit Button to edit Employee information
        editButton.setOnClickListener(new View.OnClickListener() {
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
                    boolean check = dbHelper.isPreferredNameExists(preferred_name);
                    if (check) {
                        Log.d("UniqueNameExits", preferred_name);
                    } else {
                        // If its not empty, still check if there are similar names in the db
                        preferred_name = dbHelper.getUniquePreferredName(preferred_name);
                    }
                }

                // Contact information
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
                boolean trained_opening = ((CheckBox) findViewById(R.id.EditEmployeeTrainedOpeningCheckBox)).isChecked();
                boolean trained_closing = ((CheckBox) findViewById(R.id.EditEmployeeTrainedClosingCheckBox)).isChecked();
                boolean is_archived = false;

                // Modify the database to reflect updated employee information
                dbHelper.updateEmployeeInformation(preferredName, first_name, last_name, preferred_name, phone, email,
                        mondayMorning, mondayAfternoon, tuesdayMorning, tuesdayAfternoon, wednesdayMorning,
                        wednesdayAfternoon, thursdayMorning, thursdayAfternoon, fridayMorning, fridayAfternoon,
                        saturdayFullday, sundayFullday, trained_opening, trained_closing, is_archived);

                setResult(Activity.RESULT_OK);
                // Close the current activity and return to the previous screen
                finish();
            }
        });


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
        EditText firstNameInput = findViewById(R.id.EditEmployeeFirstNameInput);
        EditText lastNameInput = findViewById(R.id.EditEmployeeLastNameInput);
        EditText phoneInput = findViewById(R.id.EditEmployeePhoneInput);
        EditText emailInput = findViewById(R.id.EditEmployeeEmailInput);
        EditText preferredNameInput = findViewById(R.id.EditEmployeePreferredNameInput);

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
