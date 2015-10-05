package com.sase.justin.saseumn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Justin on 8/20/2015.
 */
public class DateOperations {

    public DateOperations()
    {
    }

    public String getCurrentDate()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        return date;
    }

    // This obtains the current date and time when the list is being updated with new information.
    // Only called if the app can connect to the SASE website.
    public String getUpdateText()
    {
        String dateRes = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(new Date());
        String timeRes = new SimpleDateFormat("hh:mma", Locale.US).format(new Date());
        String outputDate = "Last Updated on: " + dateRes + " at " + timeRes;

        return outputDate;
    }

    // Converts a date in string to a "date ID" in INT.
    // This code changes if the implementation for the website changes. The processDate function, as of 2015-16 School Year,
    // always handles dates in the form of (e.g. January 1, 2015 & March 3, 2016)
    public int processDate(String date)
    {
        String integerData = "";
        int output;

        // We take the input string date and get rid of punctuations. By default, it's the comma.
        // After doing that, we split the string into an array so we get the month, day, and year in string.
        date = date.replace(",", "");
        String[] dateArray = date.split(" ");
        String dayModifier = dateArray[1];

        // Using our initialized string integerData, we concatenate the string with year first, then month, then day.
        // If the day is in the single-digit, we append a "0" in front to keep it consistent.
        integerData = integerData.concat(dateArray[2]);
        integerData = integerData.concat(getMonthNumberID(dateArray[0]));
        if (dayModifier.length() <= 1)
        {
            dayModifier = "0" + dayModifier;
        }
        integerData = integerData.concat(dayModifier);

        // Then convert this to an integer for easy sorting.
        output = Integer.parseInt(integerData);
        return output;
    }

    private String getMonthNumberID(String month)
    {
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

    public int getMonthNumber(String month)
    {
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

    // Used to identify if the event is tomorrow or TODAY! in the Events Calendar.
    public String ifTomorrowEventDate(String date)
    {
        String cleanDate = date.replace(",", "");
        String[] dateArray = cleanDate.split(" ");

        int dateYear  = Integer.parseInt(dateArray[2]);
        int dateMonth = getMonthNumber(dateArray[0].toLowerCase());
        int dateDay   = Integer.parseInt(dateArray[1]);

        String year = new SimpleDateFormat("yyyy", Locale.US).format(new Date());
        int iYear = Integer.parseInt(year);
        String month = new SimpleDateFormat("MM", Locale.US).format(new Date());
        int iMonth = Integer.parseInt(month);
        String day = new SimpleDateFormat("dd", Locale.US).format(new Date());
        int iDay = Integer.parseInt(day);

        if (dateYear - iYear == 0)
        {
            if (dateMonth - iMonth == 0)
            {
                if (dateDay - iDay == 0)
                {
                    return "Today!";
                }
                else if (dateDay - iDay == 1)
                {
                    return "Tomorrow!";
                }
            }
        }
        return date;
    }

    // Converts 24-Hour Time to 12-Hour time.
    public String convertTime(String time)
    {
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
