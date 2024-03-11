package com.example.shiftmanager.ui.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "schedulingdb.db";
    private static final int DATABASE_VERSION = 19;
    private static final String TABLE_EMPLOYEE = "employees";
    // Employee Table
    private static final String COL_EMPLOYEE_ID = "id";
    private static final String COL_EMPLOYEE_FIRST_NAME = "first_name";
    private static final String COL_EMPLOYEE_LAST_NAME = "last_name";
    private static final String COL_EMPLOYEE_PREFERRED_NAME = "preferred_name";
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
    //private static final String COL_DAILY_ASSIGNMENT_ID = "id";
    private static final String COL_DAILY_ASSIGNMENT_DATE = "date";
    //private static final String COL_DAILY_ASSIGNMENT_STATUS = "assignment_status";
    private static final String COL_DAILY_ASSIGNMENT_DAYSHIFT1_PREFERRED_NAME = "dayshift1_employee";
    private static final String COL_DAILY_ASSIGNMENT_DAYSHIFT2_PREFERRED_NAME = "dayshift2_employee";
    private static final String COL_DAILY_ASSIGNMENT_DAYSHIFT3_PREFERRED_NAME = "dayshift3_employee";
    private static final String COL_DAILY_ASSIGNMENT_NIGHTSHIFT1_PREFERRED_NAME = "nightshift1_employee";
    private static final String COL_DAILY_ASSIGNMENT_NIGHTSHIFT2_PREFERRED_NAME = "nightshift2_employee";
    private static final String COL_DAILY_ASSIGNMENT_NIGHTSHIFT3_PREFERRED_NAME = "nightshift3_employee";
    private static final String COL_DAILY_ASSIGNMENT_FULLDAY1_PREFERRED_NAME = "fullday1_employee";
    private static final String COL_DAILY_ASSIGNMENT_FULLDAY2_PREFERRED_NAME = "fullday2_employee";



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
                COL_EMPLOYEE_FIRST_NAME + " TEXT NOT NULL," +
                COL_EMPLOYEE_LAST_NAME + " TEXT NOT NULL," +
                COL_EMPLOYEE_PREFERRED_NAME + " TEXT NOT NULL," +
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
                COL_DAILY_ASSIGNMENT_DATE + " DATE PRIMARY KEY," +
                COL_DAILY_ASSIGNMENT_DAYSHIFT1_PREFERRED_NAME + " TEXT NULL," +
                COL_DAILY_ASSIGNMENT_DAYSHIFT2_PREFERRED_NAME + " TEXT NULL," +
                COL_DAILY_ASSIGNMENT_DAYSHIFT3_PREFERRED_NAME + " TEXT NULL," +
                COL_DAILY_ASSIGNMENT_NIGHTSHIFT1_PREFERRED_NAME + " TEXT NULL," +
                COL_DAILY_ASSIGNMENT_NIGHTSHIFT2_PREFERRED_NAME + " TEXT NULL," +
                COL_DAILY_ASSIGNMENT_NIGHTSHIFT3_PREFERRED_NAME + " TEXT NULL," +
                COL_DAILY_ASSIGNMENT_FULLDAY1_PREFERRED_NAME + " TEXT NULL," +
                COL_DAILY_ASSIGNMENT_FULLDAY2_PREFERRED_NAME + " TEXT NULL" +
                ")";
    }

    public long insertEmployee(String first_name, String last_name, String preferred_name,
                               String phone, String email, String startdate,
                               boolean mondayMorning, boolean mondayAfternoon,
                               boolean tuesdayMorning, boolean tuesdayAfternoon, boolean wednesdayMorning,
                               boolean wednesdayAfternoon, boolean thursdayMorning, boolean thursdayAfternoon,
                               boolean fridayMorning, boolean fridayAfternoon, boolean saturdayFullday,
                               boolean sundayFullday, boolean trained) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(COL_EMPLOYEE_FIRST_NAME, first_name);
        data.put(COL_EMPLOYEE_LAST_NAME, last_name);
        data.put(COL_EMPLOYEE_PREFERRED_NAME, preferred_name);
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

    public long insertDailyAssignment(String date,String dayshift1_employee,
                                      String dayshift2_employee,
                                      String dayshift3_employee,
                                      String nightshift1_employee,
                                      String nightshift2_employee,
                                      String nightshift3_employee,
                                      String fullday1_employee,
                                      String fullday2_employee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(COL_DAILY_ASSIGNMENT_DATE, date);
        data.put(COL_DAILY_ASSIGNMENT_DAYSHIFT1_PREFERRED_NAME, dayshift1_employee);
        data.put(COL_DAILY_ASSIGNMENT_DAYSHIFT1_PREFERRED_NAME, dayshift2_employee);
        data.put(COL_DAILY_ASSIGNMENT_DAYSHIFT1_PREFERRED_NAME, dayshift3_employee);
        data.put(COL_DAILY_ASSIGNMENT_NIGHTSHIFT1_PREFERRED_NAME, nightshift1_employee);
        data.put(COL_DAILY_ASSIGNMENT_NIGHTSHIFT2_PREFERRED_NAME, nightshift2_employee);
        data.put(COL_DAILY_ASSIGNMENT_NIGHTSHIFT3_PREFERRED_NAME, nightshift3_employee);
        data.put(COL_DAILY_ASSIGNMENT_FULLDAY1_PREFERRED_NAME, fullday1_employee);
        data.put(COL_DAILY_ASSIGNMENT_NIGHTSHIFT3_PREFERRED_NAME, fullday2_employee);
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
        ArrayList<String> preferredNames = new ArrayList<>();
        List<String> fNames = Arrays.asList("Bob", "Samantha", "Jerry",
                                            "John", "Daniel", "Lisa",
                                            "Perry", "Frank", "Brock",
                                            "Zeus");
        List<String> lNames = Arrays.asList("Sanders", "Smith", "Scarborough",
                "Johnson", "Pickles", "Brown",
                "Pickering", "Rutherford", "Hamilton",
                "La");

        List<String> pNames = Arrays.asList("Bob", "Sam", "Speedy",
                "Fire", "Richard", "Steve",
                "Plank", "Mote", "Beaver",
                "Lem");
        Random rand = new Random();

        firstNames.addAll(fNames);
        lastNames.addAll(lNames);
        preferredNames.addAll(pNames);

        for (int i = 0; i < 10; i++) {
            String randomFirstName = firstNames.get(rand.nextInt(10));
            String randomLastName = lastNames.get(rand.nextInt(10));
            String randomPreferredName = preferredNames.get(rand.nextInt(10));
            String first_name = randomFirstName;
            String last_name = randomLastName;
            String preferred_name = randomPreferredName;
            boolean randBool = rand.nextBoolean();
            insertEmployee(first_name, last_name, preferred_name,
                    "780-292-2020", "example@email.com", "10-02-2024",
                    randBool, randBool, randBool, randBool,
                    randBool, randBool,randBool,randBool,randBool,
                    randBool,randBool, randBool, randBool);
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
                new String[]{COL_EMPLOYEE_FIRST_NAME, COL_EMPLOYEE_LAST_NAME},
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int firstNameIndex = cursor.getColumnIndex(COL_EMPLOYEE_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(COL_EMPLOYEE_LAST_NAME);
                if (firstNameIndex != -1 && lastNameIndex != -1) {
                    String first_name = cursor.getString(firstNameIndex);
                    String last_name = cursor.getString(lastNameIndex);
                    String full_name = first_name + " " + last_name;

                    Log.d("EmployeeNames", "Adding employee name: " + full_name);
                    employeeNames.add(full_name);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return employeeNames;
    }
    public List<String> getAllEmployeePreferredNames(String[] columns, String selection, String[] selectionArgs,
                                                     String groupBy, String having, String orderBy) {
        List<String> employeeNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EMPLOYEE,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int preferredNameIndex = cursor.getColumnIndex(COL_EMPLOYEE_PREFERRED_NAME);
                if (preferredNameIndex != -1) {
                    String preferred_name = cursor.getString(preferredNameIndex);


                    Log.d("EmployeeNames", "Adding employee name: " + preferred_name);
                    employeeNames.add(preferred_name);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return employeeNames;
    }

    /*
    This returns the preferredName + highestSuffix
    So if a preffered name John3 exists, this would return John4 if the input was John
     */
    public String getUniquePreferredName(String preferredName) {
        SQLiteDatabase db = getWritableDatabase();
        String uniquePreferredName = preferredName;

        if (isPreferredNameExists(preferredName)) {
            int highestSuffix = getHighestSuffix(preferredName);

            int suffix = highestSuffix + 1;

            uniquePreferredName = preferredName + suffix;
        }
        Log.d(uniquePreferredName, "Unique Name: " + uniquePreferredName);
        return uniquePreferredName;
    }
    /*
    This gets the current highest suffix used in the preferred names of TABLE_EMPLOYEE
    So if we have John, John2, John3 in the preferred names column, and we are checking the name
    John, we would get the suffix 4 and return it as John, John2 and John3 exist.
     */
    public int getHighestSuffix(String preferredName) {
        SQLiteDatabase db = getReadableDatabase();
        int highestSuffix = 0;

        String[] columns = {COL_EMPLOYEE_PREFERRED_NAME};

        // Like is used for pattern matching
        String selection = COL_EMPLOYEE_PREFERRED_NAME + " LIKE ?";
        // % is a wildcard so we're looking for any names that start with preferred names
        // and any sequence of characters after that
        String[] selectionArgs = {preferredName + "%"};

        Cursor cursor = db.query(
                TABLE_EMPLOYEE, // Table name
                columns,        // columns to get
                selection,      // where caluse
                selectionArgs,  // values for the where clause placeholders in our case ?
                null,           // Group by
                null,           // Having
                COL_EMPLOYEE_PREFERRED_NAME + " ASC" // order by Ascending e.g apple, bee, car
        );

        if (cursor != null && cursor.moveToLast()) {
            @SuppressLint("Range") String lastPreferredName = cursor.getString(cursor.getColumnIndex(COL_EMPLOYEE_PREFERRED_NAME));

            String suffixStr = lastPreferredName.substring(preferredName.length());
            try {
                highestSuffix = Integer.parseInt(suffixStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                highestSuffix = 0;
            }
            cursor.close();
        }
        return highestSuffix;
    }
    /*
    Checks to see if the a name exists within the TABLE_EMPLOYEE database
    Uses: in add_employee used to determine if we should increment or add a suffix
    to a preferred name or first_name that is used as the preferred name
     */
    public boolean isPreferredNameExists(String preferredName) {
        SQLiteDatabase db = getReadableDatabase();
        boolean preferredNameExists = false;

        String[] columns = {COL_EMPLOYEE_ID};

        String selection = COL_EMPLOYEE_PREFERRED_NAME + " =?";
        String[] selectionArgs = {preferredName};

        Cursor cursor = db.query(
                TABLE_EMPLOYEE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            preferredNameExists = true;
            cursor.close();
        }
        return preferredNameExists;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIFT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_ASSIGNMENTS);
        onCreate(db);
    }

    public List<String> getEmployeeInformation(String employeePreferredName) {
        SQLiteDatabase db = getReadableDatabase();
        List<String> employeeInformation = new ArrayList<>();

        // Define the columns to be fetched
        String[] projection = {
                COL_EMPLOYEE_FIRST_NAME,
                COL_EMPLOYEE_LAST_NAME,
                COL_EMPLOYEE_PREFERRED_NAME,
                COL_EMPLOYEE_PHONE,
                COL_EMPLOYEE_EMAIL,
                COL_EMPLOYEE_START_DATE,
                COL_EMPLOYEE_MONDAY_MORNING,
                COL_EMPLOYEE_MONDAY_AFTERNOON,
                COL_EMPLOYEE_TUESDAY_MORNING,
                COL_EMPLOYEE_TUESDAY_AFTERNOON,
                COL_EMPLOYEE_WEDNESDAY_MORNING,
                COL_EMPLOYEE_WEDNESDAY_AFTERNOON,
                COL_EMPLOYEE_THURSDAY_MORNING,
                COL_EMPLOYEE_THURSDAY_AFTERNOON,
                COL_EMPLOYEE_FRIDAY_MORNING,
                COL_EMPLOYEE_FRIDAY_AFTERNOON,
                COL_EMPLOYEE_SATURDAY,
                COL_EMPLOYEE_SUNDAY,
                COL_EMPLOYEE_TRAINED
        };

        // Specify the criteria for selection
        String selection = COL_EMPLOYEE_PREFERRED_NAME + " = ?";

        String[] selectionArgs = { employeePreferredName };

        // Perform the query
        Cursor cursor = db.query(
                TABLE_EMPLOYEE,   // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // don't group the rows
                null,             // don't filter by row groups
                null              // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get the employee information and add it to the employeeInformation list
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_FIRST_NAME)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_LAST_NAME)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_PREFERRED_NAME)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_PHONE)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_EMAIL)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_START_DATE)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_MONDAY_MORNING)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_MONDAY_AFTERNOON)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_TUESDAY_MORNING)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_TUESDAY_AFTERNOON)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_WEDNESDAY_MORNING)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_WEDNESDAY_AFTERNOON)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_THURSDAY_MORNING)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_THURSDAY_AFTERNOON)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_FRIDAY_MORNING)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_FRIDAY_AFTERNOON)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_SATURDAY)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_SUNDAY)));
                employeeInformation.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMPLOYEE_TRAINED)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close(); // Close the database connection
        return employeeInformation;

    }

    public long insertDate(String date) {
        SQLiteDatabase db = getWritableDatabase();

        if (dateExists(db, date)) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(COL_DAILY_ASSIGNMENT_DATE, date);

        try {
            long rowId = db.insert(TABLE_DAILY_ASSIGNMENTS, null, values);
            Log.d("InsertDate", "Row inserted successfully, ID: " + rowId);
            return rowId;
        } catch (SQLException e) {
            Log.e("InsertDate", "Error inserting date", e);
            return -1;
        }

    }

    private boolean dateExists(SQLiteDatabase db, String date) {
        String selection = COL_DAILY_ASSIGNMENT_DATE + " = ?";
        String[] selectionArgs = { date };

        Cursor cursor = db.query(TABLE_DAILY_ASSIGNMENTS, null, selection, selectionArgs, null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();

        return exists;
    }

    public void removeAllDailyAssignments() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_DAILY_ASSIGNMENTS, null, null);

        db.close();
    }

    @SuppressLint("Range")
    public String getShiftValues(String columnName, String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COL_DAILY_ASSIGNMENT_DATE + " = ?";
        String[] selectionArgs = {date};

        Cursor cursor = db.query(
                TABLE_DAILY_ASSIGNMENTS,
                new String[]{columnName},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String result = "";

        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(columnName));
            cursor.close();
        }

        return result;

    }

    public long insertOrUpdateDailyAssignments(String date,
                                               String dayshift1_employee,
                                               String dayshift2_employee,
                                               String dayshift3_employee,
                                               String nightshift1_employee,
                                               String nightshift2_employee,
                                               String nightshift3_employee,
                                               String fulldayshift1_employee,
                                               String fulldayshift2_employee) {
        SQLiteDatabase db = getWritableDatabase();


        Cursor cursor = db.query(
                TABLE_DAILY_ASSIGNMENTS,
                null,
                COL_DAILY_ASSIGNMENT_DATE + " =?",
                new String[]{date},
                null,
                null,
                null);
        long rowId;

        ContentValues values = new ContentValues();
        values.put(COL_DAILY_ASSIGNMENT_DAYSHIFT1_PREFERRED_NAME, dayshift1_employee);
        values.put(COL_DAILY_ASSIGNMENT_DAYSHIFT2_PREFERRED_NAME, dayshift2_employee);
        values.put(COL_DAILY_ASSIGNMENT_DAYSHIFT3_PREFERRED_NAME, dayshift3_employee);
        values.put(COL_DAILY_ASSIGNMENT_NIGHTSHIFT1_PREFERRED_NAME, nightshift1_employee);
        values.put(COL_DAILY_ASSIGNMENT_NIGHTSHIFT2_PREFERRED_NAME, nightshift2_employee);
        values.put(COL_DAILY_ASSIGNMENT_NIGHTSHIFT3_PREFERRED_NAME, nightshift3_employee);
        values.put(COL_DAILY_ASSIGNMENT_FULLDAY1_PREFERRED_NAME, fulldayshift1_employee);
        values.put(COL_DAILY_ASSIGNMENT_FULLDAY2_PREFERRED_NAME, fulldayshift2_employee);
       if (cursor != null && cursor.moveToFirst()) {

           rowId = db.update(
                   TABLE_DAILY_ASSIGNMENTS,
                   values,
                   COL_DAILY_ASSIGNMENT_DATE + " =?",
                   new String[]{date});
       } else {
           values.put(COL_DAILY_ASSIGNMENT_DATE, date);
           rowId = db.insert(TABLE_DAILY_ASSIGNMENTS, null, values);
       }
       if (cursor != null) {
           cursor.close();
       }
       return rowId;
    }


    public int updateEmployeeInformation(String employeePreferredName,
                                         String firstName, String lastName,
                                         String preferredName, String phone, String email,
                                         boolean mondayMorning, boolean mondayAfternoon,
                                         boolean tuesdayMorning, boolean tuesdayAfternoon,
                                         boolean wednesdayMorning, boolean wednesdayAfternoon,
                                         boolean thursdayMorning, boolean thursdayAfternoon,
                                         boolean fridayMorning, boolean fridayAfternoon,
                                         boolean saturday, boolean sunday,
                                         boolean trained) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_EMPLOYEE_FIRST_NAME, firstName);
        values.put(COL_EMPLOYEE_LAST_NAME, lastName);
        values.put(COL_EMPLOYEE_PREFERRED_NAME, preferredName);
        values.put(COL_EMPLOYEE_PHONE, phone);
        values.put(COL_EMPLOYEE_EMAIL, email);
        values.put(COL_EMPLOYEE_MONDAY_MORNING, mondayMorning);
        values.put(COL_EMPLOYEE_MONDAY_AFTERNOON, mondayAfternoon);
        values.put(COL_EMPLOYEE_TUESDAY_MORNING, tuesdayMorning);
        values.put(COL_EMPLOYEE_TUESDAY_AFTERNOON, tuesdayAfternoon);
        values.put(COL_EMPLOYEE_WEDNESDAY_MORNING, wednesdayMorning);
        values.put(COL_EMPLOYEE_WEDNESDAY_AFTERNOON, wednesdayAfternoon);
        values.put(COL_EMPLOYEE_THURSDAY_MORNING, thursdayMorning);
        values.put(COL_EMPLOYEE_THURSDAY_AFTERNOON, thursdayAfternoon);
        values.put(COL_EMPLOYEE_FRIDAY_MORNING, fridayMorning);
        values.put(COL_EMPLOYEE_FRIDAY_AFTERNOON, fridayAfternoon);
        values.put(COL_EMPLOYEE_SATURDAY, saturday);
        values.put(COL_EMPLOYEE_SUNDAY, sunday);
        values.put(COL_EMPLOYEE_TRAINED, trained);

        // Define the criteria for selecting the correct record to update
        String selection = COL_EMPLOYEE_PREFERRED_NAME + " = ?";
        String[] selectionArgs = { employeePreferredName };

        // Perform the update on the database
        int count = db.update(
                TABLE_EMPLOYEE,
                values,
                selection,
                selectionArgs);

        db.close(); // Close the database connection
        return count; // Return the count of updated rows
    }


}
