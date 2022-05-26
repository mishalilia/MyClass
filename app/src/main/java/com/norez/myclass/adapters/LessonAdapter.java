package com.norez.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.norez.myclass.models.Lesson;
import com.norez.myclass.R;
import com.norez.myclass.activities.LessonActivity;
import com.norez.myclass.utils.CalendarUtils;

import java.util.List;

public class LessonAdapter extends ArrayAdapter<Lesson>
{
    public LessonAdapter(@NonNull Context context, List<Lesson> lessons)
    {
        super(context, 0, lessons);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Lesson lesson = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lesson_cell, parent, false);

        TextView titleTV = convertView.findViewById(R.id.titleTV);
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        TextView homeworkTV = convertView.findViewById(R.id.homeworkTV);

        String lessonTitle = lesson.getName();
        String lessonTime = lesson.getTime();
        String lessonHomework;
        if (lesson.getHomework().equals("")) lessonHomework = "Задания нет";
        else lessonHomework = lesson.getHomework();
        titleTV.setText(lessonTitle);
        timeTV.setText(lessonTime);
        homeworkTV.setText(lessonHomework);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LessonActivity.class);
                i.putExtra("title", lessonTitle);
                i.putExtra("time", lessonTime);
                i.putExtra("homework", lessonHomework);
                getContext().startActivity(i);
            }
        });
        return convertView;
    }
}