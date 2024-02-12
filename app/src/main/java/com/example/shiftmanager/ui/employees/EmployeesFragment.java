package com.example.shiftmanager.ui.employees;

import android.app.Activity;
import android.content.Intent;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.MainActivity;
import com.example.shiftmanager.R;
import com.example.shiftmanager.databinding.FragmentEmployeesBinding;
import com.example.shiftmanager.databinding.FragmentEmployeesBinding;

import java.util.List;

public class EmployeesFragment extends Fragment {

    private FragmentEmployeesBinding binding;
    private EmployeesViewModel employeesViewModel;
    // Define ActivityResultLauncher
    private ActivityResultLauncher<Intent> addEmployeeLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ActivityResultLauncher
        addEmployeeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Get the employee name from the intent
                        String employeeName = result.getData().getStringExtra("employeeName");
                        // Update the UI with the employee name
                        addEmployeeNameToUI(employeeName);
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false);

        // Setup addEmployeeButton click listener
        binding.EmployeeAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), addEmployee.class);
            addEmployeeLauncher.launch(intent);
        });

        return binding.getRoot();
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
        binding = null;
    }
}