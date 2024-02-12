package com.example.shiftmanager.ui.employees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanager.databinding.FragmentEmployeesBinding;
import com.example.shiftmanager.databinding.FragmentEmployeesBinding;

public class EmployeesFragment extends Fragment {

    private FragmentEmployeesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmployeesViewModel employeesViewModel =
                new ViewModelProvider(this).get(EmployeesViewModel.class);

        binding = FragmentEmployeesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        employeesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}