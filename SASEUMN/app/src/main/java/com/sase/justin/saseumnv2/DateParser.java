package com.sase.justin.saseumnv2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Justin on 8/20/2015.
 */
public class DateParser {

    //TODO: Change DateParser into a Singleton. No need to create many of these objects.
    public DateParser() {
    }

    public String getUSDate(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date);
    }

    public String getDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
    }

    public String getTime(Date date) {
        return new SimpleDateFormat("HH:mm", Locale.US).format(date);
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    public String getCurrentDateAndTime() {
        return new SimpleDateFormat("yyyyMMddHHmm", Locale.US).format(new Date());
    }

    public String getUpdateText() {
        String dateRes = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(new Date());
        String timeRes = new SimpleDateFormat("hh:mma", Locale.US).format(new Date());

        return "Last Updated on: " + dateRes + " at " + timeRes;
    }

    public String concatenateDates(String beginTime, String endTime) {
        return beginTime + " - " + endTime;
    }

    /* The calls below are the result of the use of ipages as our database. The implementation
       will change overtime and are placed in the dateParser.
       From Justin: I'm most likely going to separate this further into a class dedicated to the ipagesAPI.
       However, this is low priority for 2016-17.
     */

    /* Converts a date in string to a "date ID" in long.
       This code changes if the implementation for the website changes. The processDate function, as of 2015-16 School Year,
       always handles dates in the form of (e.g. January 1, 2015 & March 3, 2016) */
    public String convertDateToID(String date) {
        String integerData = "";

        // We take the input string date and get rid of punctuations. By default, it's the comma.
        // After doing that, we split the string into an array so we get the month, day, and year in string.
        date = date.replace(",", "");
        String[] dateArray = date.split(" ");
        String dayModifier = dateArray[1];

        // Using our initialized string integerData, we concatenate the string with year first, then month, then day.
        // If the day is in the single-digit, we append a "0" in front to keep it consistent.
        integerData = integerData.concat(dateArray[2]);
        integerData = integerData.concat(getMonthNumberID(dateArray[0]));
        if (dayModifier.length() <= 1) {
            dayModifier = "0" + dayModifier;
        }
        integerData = integerData.concat(dayModifier);
        return integerData;
    }

    public long processDate(String date) {
        String dateIDString = convertDateToID(date);
        return Long.parseLong(dateIDString);
    }

    public long processDateAndTime(String date, String time) {
        String dateIDString = date.replace("-","");
        String timeIDString = time.replace(":","");
        dateIDString = dateIDString.concat(timeIDString);
        System.out.println(Long.parseLong(dateIDString));
        return Long.parseLong(dateIDString);
    }

    private String getMonthNumberID(String month) {
        month = month.toLowerCase();
        switch (month) {
            case "january":
                return "01";
            case "february":
                return "02";
            case "march":
                return "03";
            case "april":
                return "04";
            case "may":
                return "05";
            case "june":
                return "06";
            case "july":
                return "07";
            case "august":
                return "08";
            case "september":
                return "09";
            case "october":
                return "10";
            case "november":
                return "11";
            case "december":
                return "12";
        }
        // If for whatever reason, a proper month can't be found, returns 13 to sort towards the bottom.
        return "13";
    }

    public int getMonthNumber(String month) {
        month = month.toLowerCase();
        switch (month) {
            case "january":
                return 1;
            case "february":
                return 2;
            case "march":
                return 3;
            case "april":
                return 4;
            case "may":
                return 5;
            case "june":
                return 6;
            case "july":
                return 7;
            case "august":
                return 8;
            case "september":
                return 9;
            case "october":
                return 10;
            case "november":
                return 11;
            case "december":
                return 12;
        }
        return 0;
    }

    // Converts 24-Hour Time to 12-Hour time.
    public String convertTime(String time) {
        String output = "";
        String[] timeArray = time.split(":");
        int timeHour = Integer.parseInt(timeArray[0]);
        int hourDifferenceValue = timeHour - 12;
        int outputHour = timeHour % 12;

        if (outputHour == 0) {
            outputHour = 12;
        }

        output = String.valueOf(outputHour) + ":" + timeArray[1];

        if (hourDifferenceValue < 0) {
            output = output + " AM";
        }
        else
        {
            output = output + " PM";
        }
        return output;
    }
}
