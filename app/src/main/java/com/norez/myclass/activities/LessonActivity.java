package com.norez.myclass.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.fragments.ScheduleFragment;
import com.norez.myclass.models.Group;
import com.norez.myclass.utils.CalendarUtils;
import com.norez.myclass.fragments.ConferenceFragment;
import com.norez.myclass.R;
import com.norez.myclass.fragments.TasksFragment;

public class LessonActivity extends AppCompatActivity {
    TextView lessonDate, lessonTime, lessonTitle, lessonTV, tasksTV;
    TasksFragment tasksFragment = new TasksFragment();
    ConferenceFragment conferenceFragment = new ConferenceFragment();
    public static String title, time, homework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_layout);

        lessonDate = findViewById(R.id.lessonDate);
        lessonTime = findViewById(R.id.lessonTime);
        lessonTitle = findViewById(R.id.lessonTitle);
        lessonTV = findViewById(R.id.lessonTV);
        tasksTV = findViewById(R.id.tasksTV);

        Intent i = getIntent();
        title = i.getStringExtra("title");
        time = i.getStringExtra("time");
        homework = i.getStringExtra("homework");
        lessonTitle.setText(title);
        lessonTime.setText(time);
        lessonDate.setText(CalendarUtils.translate(CalendarUtils.selectedDate.getDayOfWeek().toString()) + ", " +
                String.valueOf(CalendarUtils.selectedDate.getDayOfMonth()) + " " +
                CalendarUtils.translate(CalendarUtils.selectedDate.getMonth().toString()));

        View.OnClickListener lessonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonTV.setBackgroundColor(Color.rgb(102, 121, 255));
                tasksTV.setBackgroundColor(Color.rgb(72, 91, 255));
                setScheduleFragment(conferenceFragment);
            }
        };

        lessonClickListener.onClick(lessonTV);
        lessonTV.setOnClickListener(lessonClickListener);
        tasksTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksTV.setBackgroundColor(Color.rgb(102, 121, 255));
                lessonTV.setBackgroundColor(Color.rgb(72, 91, 255));
                setScheduleFragment(tasksFragment);
            }
        });
    }

    public void weeklyAction(View view)
    {
        finish();
    }

    private void setScheduleFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.lessonFrame, fragment);
        ft.commit();
    }
}
