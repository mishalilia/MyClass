package com.norez.myclass.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;
import com.norez.myclass.activities.AddGroupActivity;
import com.norez.myclass.adapters.GroupAdapter;
import com.norez.myclass.adapters.LessonAdapter;
import com.norez.myclass.models.Group;
import com.norez.myclass.models.User;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {
    private ImageView addGroupButton;
    private ListView groupsListView;
    private TextView emptyGroupsTV;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        if (isAdded()) {
            addGroupButton = v.findViewById(R.id.addGroupButton);
            groupsListView = v.findViewById(R.id.groupsListView);
            emptyGroupsTV = v.findViewById(R.id.emptyGroupsTV);
            linearLayout = v.findViewById(R.id.linearLayout);
            progressBar = v.findViewById(R.id.progressBar);
            linearLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            addGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), AddGroupActivity.class);
                    startActivity(i);
                }
            });
        }

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setGroupsAdapter();
    }

    private void setGroupsAdapter() {
        SharedPreferences preferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("groups")
                .whereArrayContains("members", preferences.getString("id", ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Group> groups = new ArrayList<>();
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            String name = document.get("name").toString();
                            ArrayList<String> members = (ArrayList<String>) document.get("members");
                            db.collection("users").document(document.get("organizer").toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot document = task.getResult();
                                            Group group = new Group(name,
                                                    document.get("name").toString() + " " + document.get("surname").toString(),
                                                    members);
                                            groups.add(group);
                                            if (groups.size() == 0)
                                                emptyGroupsTV.setVisibility(View.VISIBLE);
                                            else
                                                emptyGroupsTV.setVisibility(View.GONE);
                                            if (isAdded()) {
                                                GroupAdapter groupAdapter = new GroupAdapter(getContext(), groups);
                                                groupsListView.setAdapter(groupAdapter);
                                            }
                                            linearLayout.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    }
                });
    }
}