package com.nguyen.nytimessearch;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by My on 2/10/2016.
 */
// this class represents a Date object, which is composed of year, month, and day. this class is a
// replacement for Java.util.Calendar which contains a lot more fields and is therefore wasteful in
// data sending between Activities and Fragments.
public class Date implements Serializable {
    public int year;
    public int month;
    public int day;

    public Date() {
        Calendar date = new GregorianCalendar();
        initialize(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
    }

    public Date(int year, int month, int day) {
        initialize(year, month, day);
    }

    @Override
    public String toString() {
        return year + String.format("%02d", (month+1)) + String.format("%02d", day);
    }

    private void initialize(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
