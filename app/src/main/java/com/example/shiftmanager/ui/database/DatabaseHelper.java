package com.example.shiftmanager.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.ContextThemeWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "schedulingdb.db";
    private static final int DATABASE_VERSION = 11;
    private static final String TABLE_EMPLOYEE = "employees";
    // Employee Table
    private static final String COL_EMPLOYEE_ID = "id";
    private static final String COL_EMPLOYEE_NAME = "name";
    private static final String COL_EMPLOYEE_PHONE = "phone";
    private static final String COL_EMPLOYEE_EMAIL = "email";
    private static final String COL_EMPLOYEE_START_DATE = "start_date";
    /*
    private static final String COL_EMPLOYEE_AVAILABILITY_TIME = "availability_time";
    private static final String COL_EMPLOYEE_AVAILABILITY_DAY = "availability_day";
    */
    private static final String COL_EMPLOYEE_MONDAY_MORNING = "monday_morning";
    private static final String COL_EMPLOYEE_MONDAY_AFTERNOON = "monday_afternoon";

    private static final String COL_EMPLOYEE_TUESDAY_MORNING = "tuesday_morning";
    private static final String COL_EMPLOYEE_TUESDAY_AFTERNOON = "tuesday_afternoon";

    private static final String COL_EMPLOYEE_WEDNESDAY_MORNING = "wednesday_morning";
    private static final String COL_EMPLOYEE_WEDNESDAY_AFTERNOON = "wednesday_afternoon";

    private static final String COL_EMPLOYEE_THURSDAY_MORNING = "thursday_morning";
    private static final String COL_EMPLOYEE_THURSDAY_AFTERNOON = "thursday_afternoon";

    private static final String COL_EMPLOYEE_FRIDAY_MORNING = "friday_morning";
    private static final String COL_EMPLOYEE_FRIDAY_AFTERNOON = "friday_afternoon";

    private static final String COL_EMPLOYEE_SATURDAY = "saturday";
    private static final String COL_EMPLOYEE_SUNDAY = "sunday";

    private static final String COL_EMPLOYEE_TRAINED = "trained";

    // Shift Table
    private static final String TABLE_SHIFT = "shifts";
    private static final String COL_SHIFT_ID = "id";
    private static final String COL_EMPLOYEE_NAME_SHIFT = "employee_name";
    private static final String COL_SHIFT_AVAILABILITY_TIME = "availability_time";
    private static final String COL_SHIFT_AVAILABILITY_DAY = "availability_day";
    private static final String COL_SHIFT_TRAINED = "trained";


    // Daily Assignments
    /*
    private static final String COL_EMPLOYEE_NAME_SHIFT = "employee_name"; need
    private static final String COL_SHIFT_AVAILABILITY_TIME = "availability_time"; maybe
    private static final String COL_SHIFT_AVAILABILITY_DAY = "availability_day"; might not need
    private static final String COL_SHIFT_NEW_HIRE = "new_hire"; need
    private static final String COL_SHIFT_CAN_OPEN = "can_open"; need
    private static final String COL_SHIFT_CAN_CLOSE = "can_close"; need
    These should be included in daily assignments, they are already assigned, maybe union
     */
    private static final String TABLE_DAILY_ASSIGNMENTS = "daily_assignment";
    private static final String COL_DAILY_ASSIGNMENT_ID = "id";
    private static final String COL_DAILY_ASSIGNMENT_STATUS = "assignment_status";
    private static final String COL_DAILY_ASSIGNMENT_EMPLOYEE_NAME = "employee_name";
    private static final String COL_DAILY_ASSIGNMENT_DATE = "date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createEmployeeTable());
        db.execSQL(createShiftTable());
        db.execSQL(createDailyAssignment());
    }
    public String createEmployeeTable() {
        return "CREATE TABLE " + TABLE_EMPLOYEE + " (" +
                COL_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_EMPLOYEE_NAME + " TEXT NOT NULL," +
                COL_EMPLOYEE_PHONE + " TEXT NOT NULL," +
                COL_EMPLOYEE_EMAIL + " TEXT NOT NULL," +
                COL_EMPLOYEE_START_DATE + " DATE," +
                COL_EMPLOYEE_MONDAY_MORNING + " INTEGER," +
                COL_EMPLOYEE_MONDAY_AFTERNOON + " INTEGER," +
                COL_EMPLOYEE_TUESDAY_MORNING + " INTEGER," +
                COL_EMPLOYEE_TUESDAY_AFTERNOON + " INTEGER," +
                COL_EMPLOYEE_WEDNESDAY_MORNING + " INTEGER," +
                COL_EMPLOYEE_WEDNESDAY_AFTERNOON + " INTEGER," +
                COL_EMPLOYEE_THURSDAY_MORNING + " INTEGER," +
                COL_EMPLOYEE_THURSDAY_AFTERNOON + " INTEGER," +
                COL_EMPLOYEE_FRIDAY_MORNING + " INTEGER," +
                COL_EMPLOYEE_FRIDAY_AFTERNOON + " INTEGER," +
                COL_EMPLOYEE_SATURDAY + " INTEGER," +
                COL_EMPLOYEE_SUNDAY + " INTEGER," +
                COL_EMPLOYEE_TRAINED + " INTEGER" +
                ")";
    }
    public String createShiftTable() {
        return "CREATE TABLE " + TABLE_SHIFT + " (" +
                COL_SHIFT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_EMPLOYEE_NAME_SHIFT + " TEXT NOT NULL," +
                COL_SHIFT_AVAILABILITY_TIME + " TEXT NOT NULL," +
                COL_SHIFT_AVAILABILITY_DAY + " TEXT," +
                COL_SHIFT_TRAINED + " INTEGER" +
                ")";
    }

    public String createDailyAssignment() {
        return "CREATE TABLE " + TABLE_DAILY_ASSIGNMENTS + " (" +
                COL_DAILY_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_DAILY_ASSIGNMENT_EMPLOYEE_NAME + " TEXT NOT NULL," +
                COL_DAILY_ASSIGNMENT_DATE + " DATE," +
                COL_DAILY_ASSIGNMENT_STATUS + " INTEGER" +
                ")";
    }

    public long insertEmployee(String name, String phone, String email, String startdate,
                               boolean mondayMorning, boolean mondayAfternoon,
                               boolean tuesdayMorning, boolean tuesdayAfternoon, boolean wednesdayMorning,
                               boolean wednesdayAfternoon, boolean thursdayMorning, boolean thursdayAfternoon,
                               boolean fridayMorning, boolean fridayAfternoon, boolean saturdayFullday,
                               boolean sundayFullday, int trained) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(COL_EMPLOYEE_NAME, name);
        data.put(COL_EMPLOYEE_PHONE, phone);
        data.put(COL_EMPLOYEE_EMAIL, email);
        data.put(COL_EMPLOYEE_START_DATE, startdate);
        data.put(COL_EMPLOYEE_MONDAY_MORNING, mondayMorning);
        data.put(COL_EMPLOYEE_MONDAY_AFTERNOON, mondayAfternoon);
        data.put(COL_EMPLOYEE_TUESDAY_MORNING, tuesdayMorning);
        data.put(COL_EMPLOYEE_TUESDAY_AFTERNOON, tuesdayAfternoon);
        data.put(COL_EMPLOYEE_WEDNESDAY_MORNING, wednesdayMorning);
        data.put(COL_EMPLOYEE_WEDNESDAY_AFTERNOON, wednesdayAfternoon);
        data.put(COL_EMPLOYEE_THURSDAY_MORNING, thursdayMorning);
        data.put(COL_EMPLOYEE_THURSDAY_AFTERNOON, thursdayAfternoon);
        data.put(COL_EMPLOYEE_FRIDAY_MORNING, fridayMorning);
        data.put(COL_EMPLOYEE_FRIDAY_AFTERNOON, fridayAfternoon);
        data.put(COL_EMPLOYEE_SATURDAY, saturdayFullday);
        data.put(COL_EMPLOYEE_SUNDAY, sundayFullday);
        data.put(COL_EMPLOYEE_TRAINED, trained);
        long rowID = db.insert(TABLE_EMPLOYEE, null, data);
        db.close();
        return rowID;
    }

    public long insertShift(String name, String availabilityTime, String availabilityDay,
                            int trained) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(COL_EMPLOYEE_NAME_SHIFT, name);
        data.put(COL_SHIFT_AVAILABILITY_TIME, availabilityTime);
        data.put(COL_SHIFT_AVAILABILITY_DAY, availabilityDay);
        data.put(COL_SHIFT_TRAINED, trained);
        long rowID = db.insert(TABLE_SHIFT, null, data);
        db.close();
        return rowID;

    }

    public long insertDailyAssignment(String name, String date, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(COL_DAILY_ASSIGNMENT_EMPLOYEE_NAME, name);
        data.put(COL_DAILY_ASSIGNMENT_DATE, date);
        data.put(COL_DAILY_ASSIGNMENT_STATUS, status);
        long rowID = db.insert(TABLE_DAILY_ASSIGNMENTS, null, data);
        db.close();
        return rowID;

    }

    /*
    Get all current employees from TABLE_EMPLOYEE
     */
    public Cursor getAllEmployees() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_EMPLOYEE, null, null, null, null, null, null);
    }

    /*
    Remove all employees from TABLE_EMPLOYEE
     */
    public int removeAllEmployees() {
        SQLiteDatabase db = getWritableDatabase();
        // We can use rows deleted to verify if the deletion was completed succesfully
        int rowsDeleted = db.delete(TABLE_EMPLOYEE, null, null);
        db.close();
        return rowsDeleted;
    }

    public int removeEmployee(String employeeName) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsDeleted = db.delete(TABLE_EMPLOYEE, "columnName = ?", new String[]{employeeName});
        db.close();
        return rowsDeleted;
    }
    /*
    Insert 10 random employees into the database
     */
    public void insertRandomEmployees() {
        ArrayList<String> firstNames = new ArrayList<>();
        ArrayList<String> lastNames = new ArrayList<>();
        List<String> fNames = Arrays.asList("Bob", "Samantha", "Jerry",
                                            "John", "Daniel", "Lisa",
                                            "Perry", "Frank", "Brock",
                                            "Zeus");
        List<String> lNames = Arrays.asList("Sanders", "Smith", "Scarborough",
                "Johnson", "Pickles", "Brown",
                "Pickering", "Rutherford", "Hamilton",
                "La");
        Random rand = new Random();

        firstNames.addAll(fNames);
        lastNames.addAll(lNames);

        for (int i = 0; i < 10; i++) {
            String randomFirstName = firstNames.get(rand.nextInt(10));
            String randomLastName = lastNames.get(rand.nextInt(10));
            String name = randomFirstName + " " + randomLastName;
            int num = rand.nextInt(2);
            boolean randBool = rand.nextBoolean();
            insertEmployee(name ,"780-292-2020", "example@email.com", "10-02-2024",
                    randBool, randBool, randBool, randBool,
                    randBool, randBool,randBool,randBool,randBool,
                    randBool,randBool, randBool, num);
        }

    }
    /*
    Retrieves the names of all employess in TABLE_EMPLOYEE
     */
    public List<String> getAllEmployeeNames() {
        List<String> employeeNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EMPLOYEE,
                new String[]{COL_EMPLOYEE_NAME},
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex(COL_EMPLOYEE_NAME);
                if (columnIndex != -1) {
                    String name = cursor.getString(columnIndex);
                    employeeNames.add(name);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return employeeNames;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS employees");
        db.execSQL("DROP TABLE IF EXISTS shifts");
        db.execSQL("DROP TABLE IF EXISTS daily_assignments");
        onCreate(db);
    }
}
