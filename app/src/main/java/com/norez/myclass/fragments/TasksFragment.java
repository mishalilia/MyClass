package com.norez.myclass.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.norez.myclass.R;
import com.norez.myclass.activities.LessonActivity;

public class TasksFragment extends Fragment {
    private TextView homeworkTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        homeworkTV = v.findViewById(R.id.homework);
        homeworkTV.setText(LessonActivity.homework);
        return v;
    }
}