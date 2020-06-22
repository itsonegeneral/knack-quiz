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

    public static String addHourToDate(String date,int hours){
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date(date)); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, hours); // adds one hour
        return cal.getTime().toString();
    }

}
