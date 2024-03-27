package com.example.shiftmanager.ui.employees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentEmployeesBinding;
import com.example.shiftmanager.ui.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EmployeesFragment extends Fragment {

    private FragmentEmployeesBinding binding;
    private EmployeesViewModel employeesViewModel;
    // Define ActivityResultLauncher
    private ActivityResultLauncher<Intent> addEmployeeLauncher;

    private DatabaseHelper dbHelper;

    private boolean isTrainedOpeningChecked = false;
    private boolean isTrainedClosingChecked = false;

    private boolean isUntrainedOpeningChecked = false;
    private boolean isunTrainedClosingCheckbox = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        // you can uncomment the next two lines to remove all employees and populate
        // with 10 random employees, just remember to comment them out again and restart the
        // emulator once you run the first time
        //dbHelper.removeAllEmployees();
        //dbHelper.insertRandomEmployees();
        //dbHelper.insertRandomEmployees();
        //dbHelper.makeEmployees();
        /*
        employeesViewModel = new ViewModelProvider(this).get(EmployeesViewModel.class);
        employeesViewModel.getCleanAndPopulateComplete().observe(this, cleanAndPopulateCompleted -> {
            Log.d("cleanAndPopulate", "Value: " + cleanAndPopulateCompleted);
            if (!cleanAndPopulateCompleted) {
                dbHelper.removeAllEmployees();
                dbHelper.insertRandomEmployees();
                employeesViewModel.markCleanAndPopulateComplete();
            }
        });
*/
        // Initialize the ActivityResultLauncher
        addEmployeeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Get the employee name from the intent
                        // We retrieve everything that makes up an employee from intent
                        String employeeFirstName = result.getData().getStringExtra("employeeFirstName");
                        String employeeLastName = result.getData().getStringExtra("employeeLastName");
                        String employeePreferredName = result.getData().getStringExtra("employeePreferredName");
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
                        boolean trainedOpening = result.getData().getBooleanExtra("trainedOpening", false);
                        boolean trainedClosing = result.getData().getBooleanExtra("trainedClosing", false);
                        // Update the UI with the employee name
                        //addEmployeeNameToUI(employeeName);
                        // Save our employee to the database
                        saveEmployeeToDB(employeeFirstName, employeeLastName, employeePreferredName,
                                        employeePhone, employeeEmail, employeeStartDate,
                                        mondayMorning, mondayAfternoon, tuesdayMorning, tuesdayAfternoon,
                                        wednesdayMorning, wednesdayAfternoon, thursdayMorning, thursdayAfternoon,
                                        fridayMorning, fridayAfternoon, saturdayFullday, sundayFullday,
                                        trainedOpening, trainedClosing);
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh your employee list here
        updateEmployeeList();
    }

    /*
    Insert the employee into our database
     */
    private void saveEmployeeToDB(String employeeFirstName, String employeeLastName, String employeePreferredName,
                                  String employeePhone, String employeeEmail,
                                  String employeeStartDate, boolean mondayMorning, boolean mondayAfternoon,
                                  boolean tuesdayMorning, boolean tuesdayAfternoon, boolean wednesdayMorning,
                                  boolean wednesdayAfternoon, boolean thursdayMorning, boolean thursdayAfternoon,
                                  boolean fridayMorning, boolean fridayAfternoon, boolean saturdayFullday,
                                  boolean sundayFullday, boolean trained_opening, boolean trained_closing) {
        //dbHelper = new DatabaseHelper(requireContext());
        dbHelper.insertEmployee(employeeFirstName,
                                employeeLastName,
                                employeePreferredName,
                                employeePhone,
                                employeeEmail,
                                employeeStartDate,
                                mondayMorning, mondayAfternoon,
                                tuesdayMorning, tuesdayAfternoon,
                                wednesdayMorning, wednesdayAfternoon,
                                thursdayMorning, thursdayAfternoon,
                                fridayMorning, fridayAfternoon,
                                saturdayFullday, sundayFullday, trained_opening, trained_closing);

        addEmployeeNameToUI(employeePreferredName);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false);

        // Setup addEmployeeButton click listener
        binding.EmployeeAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), addEmployee.class);
            addEmployeeLauncher.launch(intent);
        });

        binding.EmployeeSearchIconButton.setOnClickListener(v -> {
            slideIn(binding.SearchBarLinearLayout);
            slideOut(binding.EmployeeLinearLayout);
            //binding.SearchBarLinearLayout.setVisibility(View.VISIBLE);
            //binding.EmployeeLinearLayout.setVisibility(View.GONE);
        });
        binding.EmployeeSearchBackIconButton.setOnClickListener(v -> {
            slideIn(binding.EmployeeLinearLayout);
            slideOut(binding.SearchBarLinearLayout);
            //binding.SearchBarLinearLayout.setVisibility(View.GONE);
            //binding.EmployeeLinearLayout.setVisibility(View.VISIBLE);
        });

        binding.trainedOpeningCheckbox.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
            isTrainedOpeningChecked = isChecked;
            updateEmployeeList();
        }));

        binding.trainedClosingCheckbox.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
            isTrainedClosingChecked = isChecked;
            updateEmployeeList();
        }));

        binding.unTrainedOpeningCheckbox.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
            isUntrainedOpeningChecked = isChecked;
            updateEmployeeList();
        }));
        binding.unTrainedClosingCheckbox.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
            isunTrainedClosingCheckbox = isChecked;
            updateEmployeeList();
        }));
        binding.EmployeeSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (binding.SearchBarLinearLayout.getVisibility() == View.VISIBLE) {
                    updateEmployeeList();
                }
                return false;
            }
        });


        if (binding.EmployeeLinearLayout.getVisibility() == View.VISIBLE) {
            String[] columns = {"preferred_name"};
            updateEmployeeNamesUI(columns ,null,null,null,null,null);
        }

        return binding.getRoot();
    }

    private void updateEmployeeList() {
        String[] columns = {"preferred_name"};
        String selection = null;
        List<String> selectionArgsList = new ArrayList<>();

        CharSequence query = binding.EmployeeSearchBar.getQuery();
        if (!TextUtils.isEmpty(query)) {
            selection = "preferred_name LIKE ?";
            selectionArgsList.add("%" + query.toString() + "%");
        }


        // If trained is checked
        if (isTrainedOpeningChecked && isTrainedClosingChecked && !isUntrainedOpeningChecked
                && !isunTrainedClosingCheckbox) {
            if (selection != null) {
                selection += " AND trained_opening = ? AND trained_closing = ?";
            } else {
                selection = "trained_opening = ? AND trained_closing = ?";
            }
            selectionArgsList.add("1");
            selectionArgsList.add("1");
        } else if (isTrainedOpeningChecked && !isTrainedClosingChecked && !isUntrainedOpeningChecked
                && !isunTrainedClosingCheckbox) {
            if (selection != null) {
                selection += " AND trained_opening = ?";
            } else {
                selection = "trained_opening = ?";
            }
            selectionArgsList.add("1");
        // If untrained is checked
        } else if (isTrainedClosingChecked && !isTrainedOpeningChecked && !isUntrainedOpeningChecked
                && !isunTrainedClosingCheckbox) {
            if (selection != null) {
                selection += " AND trained_closing = ?";
            } else {
                selection = "trained_closing = ?";
            }
            selectionArgsList.add("1");
        } else if (isunTrainedClosingCheckbox && !isTrainedOpeningChecked && !isTrainedClosingChecked
                && !isUntrainedOpeningChecked) {
            if (selection != null) {
                selection += " AND trained_closing = ?";
            } else {
                selection = "trained_closing = ?";
            }
            selectionArgsList.add("0");
        } else if (isUntrainedOpeningChecked && !isTrainedOpeningChecked && !isTrainedClosingChecked
                && !isunTrainedClosingCheckbox) {
            if (selection != null) {
                selection += " AND trained_opening = ?";
            } else {
                selection = "trained_opening = ?";
            }
            selectionArgsList.add("0");
        } else if (isUntrainedOpeningChecked && !isTrainedOpeningChecked && !isTrainedClosingChecked
                && isunTrainedClosingCheckbox) {
            if (selection != null) {
                selection += " AND trained_opening = ? AND trained_closing = ?";
            } else {
                selection = "trained_opening = ? AND trained_closing = ?";
            }
            selectionArgsList.add("0");
            selectionArgsList.add("0");
        } else if (isUntrainedOpeningChecked && isTrainedOpeningChecked && isTrainedClosingChecked
                && isunTrainedClosingCheckbox) {
            if (selection != null) {
                selection += "AND 1 = 0";
            } else {
                selection = " 1 = 0";
            }
        }


        String[] selectionArgs = selectionArgsList.toArray(new String[0]);
        updateEmployeeNamesUI(columns, selection, selectionArgs, null, null, "preferred_name ASC");
    }



    private void slideIn(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0f);
        slideIn.setDuration(500);

        slideIn.start();
    }
    private void slideOut(View view) {
        binding.EmployeeSearchBar.setQuery("", false);
        if (binding.trainedOpeningCheckbox.isChecked()) {
            binding.trainedOpeningCheckbox.toggle();
        }
        if (binding.trainedClosingCheckbox.isChecked()) {
            binding.trainedClosingCheckbox.toggle();
        }
        if (binding.unTrainedOpeningCheckbox.isChecked()) {
            binding.unTrainedOpeningCheckbox.toggle();
        }
        if (binding.unTrainedClosingCheckbox.isChecked()) {
            binding.unTrainedClosingCheckbox.toggle();
        }
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(view, "translationY", 0f, view.getHeight());
        slideOut.setDuration(500);

        slideOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                view.setTranslationY(0f);
            }
        });

        slideOut.start();

        view.setVisibility(View.VISIBLE);
    }
    /*
    Looks through db and queries for employee names
    If the employee name is already displayed it wont display them
    calls addEmployeeToUI in order to display names that are already in the db
     */
    private void updateEmployeeNamesUI(String[] columns, String selection, String[] selectionArgs,
                                       String groupBy, String having, String orderBy) {
        //List<String> employeeNames = dbHelper.getAllEmployeeNames();
        // Remove all existing child views so we can cleanly repopulate
        binding.EmployeeContainer.removeAllViews();
        List<String> employeeNames = dbHelper.getAllEmployeePreferredNames(columns,selection,selectionArgs,groupBy,having,orderBy);

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
    @SuppressLint("ClickableViewAccessibility")
    private void addEmployeeNameToUI(String employeeName) {
        // Parent container for all views
        LinearLayout mainContainer = new LinearLayout(getContext());
        mainContainer.setOrientation(LinearLayout.VERTICAL); // Set the main container to vertical
        mainContainer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.squared_background));

        // Adjust the mainContainer's height
        LinearLayout.LayoutParams mainContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mainContainerParams.height = 220; // Make the container default height for 3 lines
        mainContainer.setLayoutParams(mainContainerParams);

        // Container for the employee name and edit button
        LinearLayout nameContainer = new LinearLayout(getContext());
        nameContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams nameContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nameContainer.setLayoutParams(nameContainerParams);

        // TextView for employee name
        TextView employeeNameView = new TextView(getContext());
        employeeNameView.setText(employeeName);
        employeeNameView.setTextSize(30);
        employeeNameView.setTextColor(Color.WHITE);

        // Setting layout parameters and padding
        int paddingPx = (int) (8 * getResources().getDisplayMetrics().density); // Example padding in dp
        employeeNameView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx); // Apply padding
        employeeNameView.setWidth(650); // Set width
        employeeNameView.setHeight(100); // Set height

        // ImageButton for edit
        ImageButton editButton = new ImageButton(getContext());
        // Setting image resource, background, sound effects, and layout params
        editButton.setImageResource(R.drawable.edit_icon);
        editButton.setBackgroundResource(R.drawable.rounded_button);
        editButton.setSoundEffectsEnabled(true);
        int buttonWidth = (int) getResources().getDimension(R.dimen.button_width);
        int buttonHeight = (int) getResources().getDimension(R.dimen.button_height);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(buttonWidth, buttonHeight);
        buttonLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        editButton.setLayoutParams(buttonLayoutParams);

        // Adding views to the nameContainer
        nameContainer.addView(employeeNameView);
        nameContainer.addView(editButton);

        // Edit button Touch listener
        editButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        animateButton(view, 0.8f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        animateButton(view, 1.0f);
                        break;
                }
                return false;
            }
        });

        // Use onclick for opening a new ui
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent editintent = new Intent(getActivity(), editEmployee.class);
                editintent.putExtra("preferredName", employeeName);
                addEmployeeLauncher.launch(editintent);
            }

        });

        // Add nameContainer to mainContainer
        mainContainer.addView(nameContainer); // Add the container with the name and edit button

        // Get employeeEmail and employeePhone
        String employeeEmail = dbHelper.getEmployeeInformation(employeeName).get(4).toString();
        String employeePhone = dbHelper.getEmployeeInformation(employeeName).get(3).toString();

        // If employeeEmail <= 17 characters, put employeePhone and employeeEmail on same line
        if (employeeEmail.length() <= 17) {
            // TextView for employee email
            TextView employeeContactView = new TextView(getContext());
            employeeContactView.setText(employeeEmail + " | " + employeePhone); // Format for email and phone
            employeeContactView.setTextSize(20); // Adjusted text size for differentiation
            employeeContactView.setTextColor(Color.WHITE);
            employeeContactView.setPadding(paddingPx, 0, 0, 0); // Apply padding
            employeeContactView.setWidth(650); // Set width
            mainContainerParams.height = 170;
            // Adding employeeContactView to the main container
            mainContainer.addView(employeeContactView);

        // If employeeEmail > 32 characters, put employeeEmail on two lines
        } else if (employeeEmail.length() > 32){
            // TextView for employee email
            TextView employeeEmailView = new TextView(getContext()); // 4
            employeeEmailView.setText(employeeEmail); // Email Text
            employeeEmailView.setTextSize(20); // Adjusted text size for differentiation
            employeeEmailView.setTextColor(Color.WHITE);
            employeeEmailView.setPadding(paddingPx, 0, 0, 0); // Apply padding
            employeeEmailView.setWidth(650); // Set width
            mainContainerParams.height = 270; // Adjust mainContainer height to add another line
            // TextView for employee phone
            TextView employeePhoneView = new TextView(getContext());
            employeePhoneView.setText(employeePhone); // Phone Number text
            employeePhoneView.setTextSize(20); // Adjusted text size for differentiation
            employeePhoneView.setTextColor(Color.WHITE);
            employeePhoneView.setPadding(paddingPx, 0, 0, 0); // Apply padding
            employeePhoneView.setWidth(650); // Set width

            // Adding phone and email containers to the main container
            mainContainer.addView(employeeEmailView); // Add the email view on two lines
            mainContainer.addView(employeePhoneView); // Add the phone view on another new line

        // Otherwise, employeeEmail on one line and employeePhone on one line
        } else {
            // TextView for employee email
            TextView employeeEmailView = new TextView(getContext());
            employeeEmailView.setText(employeeEmail); // Email Text
            employeeEmailView.setTextSize(20); // Adjusted text size for differentiation
            employeeEmailView.setTextColor(Color.WHITE);
            employeeEmailView.setPadding(paddingPx, 0, 0, 0); // Apply padding
            employeeEmailView.setWidth(650); // Set width
            // TextView for employee phone
            TextView employeePhoneView = new TextView(getContext());
            employeePhoneView.setText(employeePhone); // Phone Number text
            employeePhoneView.setTextSize(20); // Adjusted text size for differentiation
            employeePhoneView.setTextColor(Color.WHITE);
            employeePhoneView.setPadding(paddingPx, 0, 0, 0); // Apply padding
            employeePhoneView.setWidth(650); // Set width

            // Adding phone and email containers to the main container
            mainContainer.addView(employeeEmailView); // Add the email view on a new line
            mainContainer.addView(employeePhoneView); // Add the phone view on another new line
        }
        // Finally, add the main container to the EmployeeContainer in your layout
        binding.EmployeeContainer.addView(mainContainer);
    }


    private void animateButton(View view, float scale) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDown.start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.close();
        binding = null;
    }
}