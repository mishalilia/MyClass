package com.norez.myclass.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.norez.myclass.R;
import com.norez.myclass.activities.VideoCallActivity;

public class ConferenceFragment extends Fragment {
    private Button connectButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_conference, container, false);
        connectButton = v.findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), VideoCallActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
}