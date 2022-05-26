package com.norez.myclass.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class CalendarUtils {
    public static LocalDate selectedDate;

    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("kk:mm");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL yyyy");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 2; i <= 43; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
        return daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = mondayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate mondayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.MONDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }

    public static String translate(String string) {
        HashMap<String, String> translator = new HashMap<>();
        translator.put("MONDAY", "Понедельник");
        translator.put("TUESDAY", "Вторник");
        translator.put("WEDNESDAY", "Среда");
        translator.put("THURSDAY", "Четверг");
        translator.put("FRIDAY", "Пятница");
        translator.put("SATURDAY", "Суббота");
        translator.put("SUNDAY", "Воскресение");
        translator.put("JANUARY", "января");
        translator.put("FEBRUARY", "февраля");
        translator.put("MARCH", "марта");
        translator.put("APRIL", "апреля");
        translator.put("MAY", "мая");
        translator.put("JUNE", "июня");
        translator.put("JULY", "июля");
        translator.put("AUGUST", "августа");
        translator.put("SEPTEMBER", "сентября");
        translator.put("OCTOBER", "октября");
        translator.put("NOVEMBER", "ноября");
        translator.put("DECEMBER", "декабря");
        return translator.get(string);
    }
}