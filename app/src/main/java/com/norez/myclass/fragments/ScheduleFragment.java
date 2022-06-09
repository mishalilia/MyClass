package com.norez.myclass.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import static com.norez.myclass.utils.CalendarUtils.daysInWeekArray;
import static com.norez.myclass.utils.CalendarUtils.monthYearFromDate;
import static com.norez.myclass.utils.CalendarUtils.selectedDate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.adapters.CalendarAdapter;
import com.norez.myclass.utils.CalendarUtils;
import com.norez.myclass.models.Lesson;
import com.norez.myclass.adapters.LessonAdapter;
import com.norez.myclass.R;
import com.norez.myclass.activities.CalendarActivity;

public class ScheduleFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText, nolessonsTV;
    private RecyclerView calendarRecyclerView;
    private ListView lessonListView;
    private ProgressBar progressBar;
    private LinearLayout lessonsLayout;

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate).substring(0, 1).toUpperCase() +
                monthYearFromDate(CalendarUtils.selectedDate).substring(1));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setLessonAdapter();
    }

    public void previousWeekAction(View view) {
        System.out.println(view);
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setWeekView();
    }

    private void setLessonAdapter() {
        ArrayList<Lesson> dailyLessons = Lesson.lessonsForDate(CalendarUtils.selectedDate);
        if (dailyLessons.size() == 0) {
            nolessonsTV.setVisibility(View.VISIBLE);
        } else {
            nolessonsTV.setVisibility(View.GONE);
        }
        if (isAdded()) {
            LessonAdapter lessonAdapter = new LessonAdapter(getContext(), dailyLessons);
            lessonListView.setAdapter(lessonAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        if (isAdded()) {
            calendarRecyclerView = v.findViewById(R.id.calendarRecyclerView);
            monthYearText = v.findViewById(R.id.monthYearTV);
            nolessonsTV = v.findViewById(R.id.nolessonsTV);
            lessonListView = v.findViewById(R.id.lessonListView);
            progressBar = v.findViewById(R.id.progressBar);
            lessonsLayout = v.findViewById(R.id.lessonsLayout);
            ImageView arrowLeft = v.findViewById(R.id.arrowLeft_s);
            ImageView arrowRight = v.findViewById(R.id.arrowRight_s);
            ImageView calendarButton = v.findViewById(R.id.calendarButton);
            CalendarUtils.selectedDate = LocalDate.now();
            SharedPreferences preferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Lesson.lessonsList.clear();
            progressBar.setVisibility(View.VISIBLE);
            lessonsLayout.setVisibility(View.GONE);
            db.collection("groups").whereArrayContains("members", preferences.getString("id", "")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                        db.collection("lessons").whereEqualTo("name", document1.get("name").toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document2 : task.getResult()) {
                                    Lesson lesson = new Lesson(document1.get("name").toString(),
                                            LocalDate.of(Math.toIntExact((long) document2.get("dateYear")), Math.toIntExact((long) document2.get("dateMonth")), Math.toIntExact((long) document2.get("dateDay"))),
                                            document2.get("time").toString(), document2.get("homework").toString());
                                    Lesson.lessonsList.add(lesson);
                                }
                                progressBar.setVisibility(View.GONE);
                                lessonsLayout.setVisibility(View.VISIBLE);
                                setWeekView();
                            }
                        });
                    }
                }
            });

            arrowLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousWeekAction(v);
                }
            });
            arrowRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextWeekAction(v);
                }
            });
            calendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), CalendarActivity.class);
                    startActivity(i);
                }
            });
        }
        return v;
    }
}