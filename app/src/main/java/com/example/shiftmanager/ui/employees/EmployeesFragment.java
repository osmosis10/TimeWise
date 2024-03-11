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

    private boolean isTrainedChecked = false;

    private boolean isUntrainedChecked = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        // you can uncomment the next two lines to remove all employees and populate
        // with 10 random employees, just remember to comment them out again and restart the
        // emulator once you run the first time
        //dbHelper.removeAllEmployees();
        //dbHelper.insertRandomEmployees();
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
                        boolean trained = result.getData().getBooleanExtra("trained", false);
                        // Update the UI with the employee name
                        //addEmployeeNameToUI(employeeName);
                        // Save our employee to the database
                        saveEmployeeToDB(employeeFirstName, employeeLastName, employeePreferredName,
                                        employeePhone, employeeEmail, employeeStartDate,
                                        mondayMorning, mondayAfternoon, tuesdayMorning, tuesdayAfternoon,
                                        wednesdayMorning, wednesdayAfternoon, thursdayMorning, thursdayAfternoon,
                                        fridayMorning, fridayAfternoon, saturdayFullday, sundayFullday, trained);
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
                                  boolean sundayFullday, boolean trained) {
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
                                saturdayFullday, sundayFullday, trained);

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

        binding.trainedCheckbox.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
            isTrainedChecked = isChecked;
            updateEmployeeList();
        }));

        binding.unTrainedCheckbox.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
            isUntrainedChecked = isChecked;
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

        // If both trained and untrained are checked
        if (isTrainedChecked && isUntrainedChecked) {
            // All the if (selection != null) statement are for if the query isnt empty
            // and selection has "preferred_name LIKE?" in it
            if (selection != null) {
                selection += " AND trained IN (?,?)";
            } else {
                selection = "trained IN (?,?)";
            }
            selectionArgsList.add("0");
            selectionArgsList.add("1");
        // If trained is checked
        } else if (isTrainedChecked) {
            if (selection != null) {
                selection += " AND trained = ?";
            } else {
                selection = "trained = ?";
            }
            selectionArgsList.add("1");
        // If untrained is checked
        } else if (isUntrainedChecked) {
            if (selection != null) {
                selection += " AND trained = ?";
            } else {
                selection = "trained = ?";
            }
            selectionArgsList.add("0");
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);
        updateEmployeeNamesUI(columns, selection, selectionArgs, null, null, null);
    }

    private void slideIn(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0f);
        slideIn.setDuration(500);

        slideIn.start();
    }
    private void slideOut(View view) {
        binding.EmployeeSearchBar.setQuery("", false);
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

        // Container to hold both the employee name and imagebutton
        LinearLayout nameContainer = new LinearLayout(getContext());
        nameContainer.setOrientation(LinearLayout.HORIZONTAL);
        nameContainer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.squared_background));

        TextView employeeNameView = new TextView(getContext());
        employeeNameView.setText(employeeName);
        employeeNameView.setTextSize(30);
        employeeNameView.setTextColor(Color.WHITE);
        //employeeNameView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_background)); // Set the rounded corner background

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

        // We set the image to edit_icon which we create in res/drawable/edit_icon.xml
        // Set the background to the options in /res/drawable/rounded_button.xml
        // setSoundEffectsEnabled turns on the android click sound
        ImageButton editButton = new ImageButton(getContext());
        editButton.setImageResource(R.drawable.edit_icon);
        editButton.setBackgroundResource(R.drawable.rounded_button);
        editButton.setSoundEffectsEnabled(true);

        // We have the dimensions set in res/values/dimens.xml
        int buttonWidth = (int) getResources().getDimension(R.dimen.button_width);
        int buttonHeight = (int) getResources().getDimension(R.dimen.button_height);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(buttonWidth,buttonHeight);
        buttonLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        editButton.setLayoutParams(buttonLayoutParams);

        // Simple animation that scales the edit_icon down on ACTION_DOWN and scales up on ACTION_CANCEL
        // Helps to communicate responsiveness of button
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



        // We set the employee name and the image button in a container called nameContainer
        nameContainer.addView(employeeNameView);
        nameContainer.addView(editButton);

        // We then bind nameContianer in another container Employee container
        binding.EmployeeContainer.addView(nameContainer);
        //binding.EmployeeContainer.addView(employeeNameView); // Add the name TextView to employee layout
        //binding.EmployeeContainer.addView(editButton);
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