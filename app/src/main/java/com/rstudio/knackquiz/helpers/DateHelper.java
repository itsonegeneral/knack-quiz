package com.rstudio.knackquiz.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static String getStartDate(String startTime) {
        Date date = new Date(startTime);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        return format.format(date);
    }

    public static String getStartTime(String startTime) {
        Date date = new Date(startTime);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }

    public static String addHourToDate(String date, int hours) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date(date));
        String pattern = "EEE MMM dd HH:mm:ss yyyy"; // sets calendar time/date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = new Date(date);
        cal.add(Calendar.HOUR_OF_DAY, hours); // adds one hour
        date1 = cal.getTime();
        return simpleDateFormat.format(date1);
    }

    public static String getFormattedDate(Date date) {
        String pattern = "EEE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String getCurrentFormattedDate() {
        Date date = new Date();
        String pattern = "EEE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

}
