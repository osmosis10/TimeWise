package com.example.shiftmanager.ui.employees;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeesViewModel extends ViewModel {

    private MutableLiveData<List<Employee>> employeesLiveData;

    public EmployeesViewModel() {
        employeesLiveData = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Employee>> getEmployees() {
        return employeesLiveData;
    }

    public void addEmployee(Employee employee) {
        List<Employee> currentList = employeesLiveData.getValue();
        if (currentList == null) currentList = new ArrayList<>();
        currentList.add(employee);
        employeesLiveData.setValue(currentList);
    }
}