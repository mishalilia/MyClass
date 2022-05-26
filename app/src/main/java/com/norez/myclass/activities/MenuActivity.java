package com.norez.myclass.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.norez.myclass.fragments.GroupsFragment;
import com.norez.myclass.fragments.MessagesFragment;
import com.norez.myclass.fragments.ProfileFragment;
import com.norez.myclass.R;
import com.norez.myclass.fragments.ScheduleFragment;

public class MenuActivity extends AppCompatActivity {
    LinearLayout scheduleButton, groupsButton, messagesButton, profileButton;
    ScheduleFragment scheduleFragment = new ScheduleFragment();
    GroupsFragment groupsFragment = new GroupsFragment();
    MessagesFragment messagesFragment = new MessagesFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    TextView schedule, groups, messages, profile;
    ImageView scheduleImage, groupsImage, messagesImage, profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        scheduleButton = findViewById(R.id.ScheduleButton);
        groupsButton = findViewById(R.id.MarksButton);
        messagesButton = findViewById(R.id.MessagesButton);
        profileButton = findViewById(R.id.ProfileButton);
        schedule = findViewById(R.id.schedule);
        groups = findViewById(R.id.groups);
        messages = findViewById(R.id.messages);
        profile = findViewById(R.id.profile);
        scheduleImage = findViewById(R.id.scheduleImage);
        groupsImage = findViewById(R.id.marksImage);
        messagesImage = findViewById(R.id.messagesImage);
        profileImage = findViewById(R.id.profileImage);

        View.OnClickListener scheduleClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule.setTextColor(Color.rgb(72, 91, 255));
                scheduleImage.setColorFilter(Color.rgb(72, 91, 255));
                groups.setTextColor(Color.rgb(0, 0, 0));
                messages.setTextColor(Color.rgb(0, 0, 0));
                profile.setTextColor(Color.rgb(0, 0, 0));
                groupsImage.setColorFilter(Color.rgb(0, 0, 0));
                messagesImage.setColorFilter(Color.rgb(0, 0, 0));
                profileImage.setColorFilter(Color.rgb(0, 0, 0));
                setScheduleFragment(scheduleFragment);
            }
        };

        scheduleClickListener.onClick(scheduleButton);

        scheduleButton.setOnClickListener(scheduleClickListener);
        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups.setTextColor(Color.rgb(72, 91, 255));
                groupsImage.setColorFilter(Color.rgb(72, 91, 255));
                schedule.setTextColor(Color.rgb(0, 0, 0));
                messages.setTextColor(Color.rgb(0, 0, 0));
                profile.setTextColor(Color.rgb(0, 0, 0));
                scheduleImage.setColorFilter(Color.rgb(0, 0, 0));
                messagesImage.setColorFilter(Color.rgb(0, 0, 0));
                profileImage.setColorFilter(Color.rgb(0, 0, 0));
                setScheduleFragment(groupsFragment);
            }
        });
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.setTextColor(Color.rgb(72, 91, 255));
                messagesImage.setColorFilter(Color.rgb(72, 91, 255));
                schedule.setTextColor(Color.rgb(0, 0, 0));
                groups.setTextColor(Color.rgb(0, 0, 0));
                profile.setTextColor(Color.rgb(0, 0, 0));
                scheduleImage.setColorFilter(Color.rgb(0, 0, 0));
                groupsImage.setColorFilter(Color.rgb(0, 0, 0));
                profileImage.setColorFilter(Color.rgb(0, 0, 0));
                setScheduleFragment(messagesFragment);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setTextColor(Color.rgb(72, 91, 255));
                profileImage.setColorFilter(Color.rgb(72, 91, 255));
                schedule.setTextColor(Color.rgb(0, 0, 0));
                messages.setTextColor(Color.rgb(0, 0, 0));
                groups.setTextColor(Color.rgb(0, 0, 0));
                scheduleImage.setColorFilter(Color.rgb(0, 0, 0));
                messagesImage.setColorFilter(Color.rgb(0, 0, 0));
                groupsImage.setColorFilter(Color.rgb(0, 0, 0));
                setScheduleFragment(profileFragment);
            }
        });
    }

    private void setScheduleFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }
}