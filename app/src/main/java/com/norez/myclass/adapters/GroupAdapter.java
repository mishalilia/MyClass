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

import com.norez.myclass.R;
import com.norez.myclass.activities.MembersActivity;
import com.norez.myclass.models.Group;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {
    public GroupAdapter(@NonNull Context context, ArrayList<Group> groups) {
        super(context, 0, groups);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Group group = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_cell, parent, false);

        TextView groupNameTV = convertView.findViewById(R.id.groupNameTV);
        TextView groupOrganizerTV = convertView.findViewById(R.id.groupOrganizerTV);

        groupNameTV.setText(group.getName());
        groupOrganizerTV.setText(group.getOrganizer());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MembersActivity.class);
                Group.current_group = group;
                getContext().startActivity(i);
            }
        });
        return convertView;
    }
}
