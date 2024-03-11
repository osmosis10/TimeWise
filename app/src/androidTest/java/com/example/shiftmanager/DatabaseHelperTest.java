package com.example.shiftmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import com.example.shiftmanager.ui.database.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {
    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbHelper = new DatabaseHelper(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }
    @Test
    public void testInsertEmployee() {
        long rowId = dbHelper.insertEmployee("John","Doe","Jim", "123456789", "john@example.com",
                "2022-01-01", false, false, false, false, false,
                false, false, false, false, false, false, false,false);

        assertNotEquals(-1, rowId);
        /*
        Log.d("DatabaseTest", "Inserted employee data:");
        Cursor cursor = dbHelper.getAllEmployees();
        if (cursor.moveToFirst()) { //Checks if the cursor is not empty and moves the cursor to the first row of the result set
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                String availabilityTime = cursor.getString(cursor.getColumnIndex("availability_time"));
                String availabilityDay = cursor.getString(cursor.getColumnIndex("availability_day"));
                int newHire = cursor.getInt(cursor.getColumnIndex("new_hire"));
                int canOpen = cursor.getInt(cursor.getColumnIndex("can_open"));
                int canClose = cursor.getInt(cursor.getColumnIndex("can_close"));

                Log.d("DatabaseTest", "Name: " + name +
                        ", Phone: " + phone +
                        ", Email: " + email +
                        ", Start Date: " + startDate +
                        ", Availability Time: " + availabilityTime +
                        ", Availability Day: " + availabilityDay +
                        ", New Hire: " + newHire +
                        ", Can Open: " + canOpen +
                        ", Can Close: " + canClose);
            } while (cursor.moveToNext()); // checks whether there is a next row in the result set
        }

        cursor.close();
        */ //Fix the values
    }

    @Test
    public void testInsertShifts() {
        long rowId = dbHelper.insertShift("Don Juan",
                                "Afternoon",
                                "Monday,Tuesday,Wednesday,Saturday",
                                0);

        assertNotEquals(-1, rowId);
        /*
        Log.d("DatabaseTestShifts", "Inserted shifts data:");
        Cursor cursor = dbHelper.getAllEmployees();
        if (cursor.moveToFirst()) { //Checks if the cursor is not empty and moves the cursor to the first row of the result set
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String availabilityTime = cursor.getString(cursor.getColumnIndex("availability_time"));
                String availabilityDay = cursor.getString(cursor.getColumnIndex("availability_day"));
                int newHire = cursor.getInt(cursor.getColumnIndex("new_hire"));
                int canOpen = cursor.getInt(cursor.getColumnIndex("can_open"));
                int canClose = cursor.getInt(cursor.getColumnIndex("can_close"));

                Log.d("DatabaseTest", "Name: " + name +
                        ", Availability Time: " + availabilityTime +
                        ", Availability Day: " + availabilityDay +
                        ", New Hire: " + newHire +
                        ", Can Open: " + canOpen +
                        ", Can Close: " + canClose);
            } while (cursor.moveToNext()); // checks whether there is a next row in the result set
        }

        cursor.close();
    */
    }

    @Test
    public void testGetAllEmployees() {
        dbHelper.insertEmployee("John","Doe","Jim", "123456789", "john@example.com",
                "2022-01-01", false, false, false, false, false,
                false, false, false, false, false, false, false,false);

        dbHelper.insertEmployee("Jane","Mary","Janet", "987654321", "jane@example.com",
                "2022-02-01", false, false, false, false, false,
                false, false, false, false, false, false, false,true);
        Cursor cursor = dbHelper.getAllEmployees();

        assertEquals(2, cursor.getCount());
    }

}

