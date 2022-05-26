package com.norez.myclass.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Lesson {
    public static ArrayList<Lesson> lessonsList = new ArrayList<>();

    public static ArrayList<Lesson> lessonsForDate(LocalDate date) {
        ArrayList<Lesson> lessons = new ArrayList<>();

        for(Lesson lesson : lessonsList) {
            if (lesson.getDate().equals(date))
                lessons.add(lesson);
        }

        return lessons;
    }

    private String name;
    private LocalDate date;
    private String time;
    private String homework;

    public Lesson(String name, LocalDate date, String time, String homework) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.homework = homework;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHomework()
    {
        return homework;
    }

    public void setHomework(String homework)
    {
        this.homework = homework;
    }
}