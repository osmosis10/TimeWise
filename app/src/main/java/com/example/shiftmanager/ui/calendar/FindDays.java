package com.example.shiftmanager.ui.calendar;

import java.util.*;

class FindDay {
    static int dayofweek(int d, int m, int y)
    {
        int t[] = { 0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4 };
        if (m < 3)
            y--;
        return (y + y / 4 - y / 100 + y / 400 + t[m - 1]
                + d)
                % 7;
    }

}