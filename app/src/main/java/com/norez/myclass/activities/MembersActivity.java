package com.norez.myclass.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;
import com.norez.myclass.adapters.MemberAdapter;
import com.norez.myclass.models.Group;

public class MembersActivity extends AppCompatActivity {
    ImageView back, delete;
    TextView groupTitle, groupOrganizer;
    ListView memberListView;
    Button conferenceCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_layout);

        groupTitle = findViewById(R.id.groupTitle);
        groupOrganizer = findViewById(R.id.groupOrganizer);
        groupTitle.setText(Group.current_group.getName());
        groupOrganizer.setText(Group.current_group.getOrganizer());
        memberListView = findViewById(R.id.membersListView);
        delete = findViewById(R.id.delete);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        db.collection("users").document(preferences.getString("id", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if ((document.get("name").toString() + " " + document.get("surname").toString()).equals(Group.current_group.getOrganizer()))
                    delete.setVisibility(View.VISIBLE);
                else
                    delete.setVisibility(View.GONE);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("groups").whereEqualTo("name", Group.current_group.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            db.collection("groups").document(document.getId()).delete();
                            setResult(RESULT_OK, new Intent());
                            finish();
                        }
                    }
                });
            }
        });
        conferenceCreate = findViewById(R.id.conferenceCreate);
        conferenceCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MembersActivity.this, CalendarActivity.class);
                CalendarActivity.creatingConf = true;
                startActivity(i);
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db.collection("users").document(preferences.getString("id", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (Group.current_group.getOrganizer().equals(task.getResult().get("name").toString() + " " + task.getResult().get("surname").toString()))
                    conferenceCreate.setVisibility(View.VISIBLE);
                else
                    conferenceCreate.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMemberAdapter();
    }

    private void setMemberAdapter() {
        MemberAdapter memberAdapter = new MemberAdapter(this, Group.current_group.getMembers());
        memberListView.setAdapter(memberAdapter);
    }
}
