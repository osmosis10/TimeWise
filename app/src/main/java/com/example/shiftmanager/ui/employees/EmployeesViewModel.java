package com.example.shiftmanager.ui.employees;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeesViewModel extends ViewModel {

    private MutableLiveData<List<Employee>> employeesLiveData;
    private final MutableLiveData<Boolean> cleanAndPopulateCompleted = new MutableLiveData<>();

    public EmployeesViewModel() {

        employeesLiveData = new MutableLiveData<>(new ArrayList<>());
        cleanAndPopulateCompleted.setValue(false);
    }

    public LiveData<List<Employee>> getEmployees() {
        return employeesLiveData;
    }

    public LiveData<Boolean> getCleanAndPopulateComplete() {
        return cleanAndPopulateCompleted;
    }
    public void markCleanAndPopulateComplete() {
        cleanAndPopulateCompleted.setValue(true);
    }
    public void addEmployee(Employee employee) {
        List<Employee> currentList = employeesLiveData.getValue();
        if (currentList == null) currentList = new ArrayList<>();
        currentList.add(employee);
        employeesLiveData.setValue(currentList);
    }
}