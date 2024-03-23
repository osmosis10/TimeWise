package com.example.shiftmanager.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.databinding.FragmentHomeBinding;
import com.example.shiftmanager.ui.database.DatabaseHelper;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper dbHelper;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_HAS_ADDED_EMPLOYEES = "hasAddedEmployees";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        textView.setTextColor(Color.BLACK);
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        dbHelper = new DatabaseHelper(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //Change this to true to wipe data base and add a fresh set of employees
        // then back to false to save the preference
        boolean hasAddedEmployees = prefs.getBoolean(KEY_HAS_ADDED_EMPLOYEES, false);

        if (hasAddedEmployees) {
            dbHelper.removeAllEmployees();
            dbHelper.makeEmployees();

            prefs.edit().putBoolean(KEY_HAS_ADDED_EMPLOYEES, true).apply();
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}