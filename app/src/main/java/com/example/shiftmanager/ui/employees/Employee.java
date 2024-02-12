package com.example.shiftmanager.ui.employees;

public class Employee {
    private String name;
    private String phone;
    private String email;
    private String startDate;
    // Availability
    private boolean mondayMorning;
    private boolean mondayAfternoon;
    private boolean tuesdayMorning;
    private boolean tuesdayAfternoon;
    private boolean wednesdayMorning;
    private boolean wednesdayAfternoon;
    private boolean thursdayMorning;
    private boolean thursdayAfternoon;
    private boolean fridayMorning;
    private boolean fridayAfternoon;
    private boolean saturdayFullday;
    private boolean sundayFullday;

    // Constructor
    public Employee(String name, String phone, String email, String startDate,
                    boolean mondayMorning, boolean mondayAfternoon,
                    boolean tuesdayMorning, boolean tuesdayAfternoon,
                    boolean wednesdayMorning, boolean wednesdayAfternoon,
                    boolean thursdayMorning, boolean thursdayAfternoon,
                    boolean fridayMorning, boolean fridayAfternoon,
                    boolean saturdayFullday, boolean sundayFullday) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.startDate = startDate;
        this.mondayMorning = mondayMorning;
        this.mondayAfternoon = mondayAfternoon;
        this.tuesdayMorning = tuesdayMorning;
        this.tuesdayAfternoon = tuesdayAfternoon;
        this.wednesdayMorning = wednesdayMorning;
        this.wednesdayAfternoon = wednesdayAfternoon;
        this.thursdayMorning = thursdayMorning;
        this.thursdayAfternoon = thursdayAfternoon;
        this.fridayMorning = fridayMorning;
        this.fridayAfternoon = fridayAfternoon;
        this.saturdayFullday = saturdayFullday;
        this.sundayFullday = sundayFullday;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getStartDate() {
        return startDate;
    }

    public boolean isMondayMorning() {
        return mondayMorning;
    }

    public boolean isMondayAfternoon() {
        return mondayAfternoon;
    }

    public boolean isTuesdayMorning() {
        return tuesdayMorning;
    }

    public boolean isTuesdayAfternoon() {
        return tuesdayAfternoon;
    }

    public boolean isWednesdayMorning() {
        return wednesdayMorning;
    }

    public boolean isWednesdayAfternoon() {
        return wednesdayAfternoon;
    }

    public boolean isThursdayMorning() {
        return thursdayMorning;
    }

    public boolean isThursdayAfternoon() {
        return thursdayAfternoon;
    }

    public boolean isFridayMorning() {
        return fridayMorning;
    }

    public boolean isFridayAfternoon() {
        return fridayAfternoon;
    }

    public boolean isSaturdayFullday() {
        return saturdayFullday;
    }

    public boolean isSundayFullday() {
        return sundayFullday;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setMondayMorning(boolean mondayMorning) {
        this.mondayMorning = mondayMorning;
    }

    public void setMondayAfternoon(boolean mondayAfternoon) {
        this.mondayAfternoon = mondayAfternoon;
    }

    public void setTuesdayMorning(boolean tuesdayMorning) {
        this.tuesdayMorning = tuesdayMorning;
    }

    public void setTuesdayAfternoon(boolean tuesdayAfternoon) {
        this.tuesdayAfternoon = tuesdayAfternoon;
    }

    public void setWednesdayMorning(boolean wednesdayMorning) {
        this.wednesdayMorning = wednesdayMorning;
    }

    public void setWednesdayAfternoon(boolean wednesdayAfternoon) {
        this.wednesdayAfternoon = wednesdayAfternoon;
    }

    public void setThursdayMorning(boolean thursdayMorning) {
        this.thursdayMorning = thursdayMorning;
    }

    public void setThursdayAfternoon(boolean thursdayAfternoon) {
        this.thursdayAfternoon = thursdayAfternoon;
    }

    public void setFridayMorning(boolean fridayMorning) {
        this.fridayMorning = fridayMorning;
    }

    public void setFridayAfternoon(boolean fridayAfternoon) {
        this.fridayAfternoon = fridayAfternoon;
    }

    public void setSaturdayFullday(boolean saturdayFullday) {
        this.saturdayFullday = saturdayFullday;
    }

    public void setSundayFullday(boolean sundayFullday) {
        this.sundayFullday = sundayFullday;
    }
}
