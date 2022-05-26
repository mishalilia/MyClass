package com.norez.myclass.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddGroupActivity extends AppCompatActivity {
    ImageView back;
    EditText groupName, groupPassword;
    Button createGroup, joinGroup;
    TextView errorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_layout);

        groupName = findViewById(R.id.group_name);
        groupPassword = findViewById(R.id.group_password);
        errorTV = findViewById(R.id.errorTextView);
        createGroup = findViewById(R.id.createGroup);
        joinGroup = findViewById(R.id.joinGroup);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupName.getText().toString().equals("") && groupPassword.getText().toString().equals("")) {
                    errorTV.setText("Не все поля заполнены.");
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("groups")
                            .whereEqualTo("name", groupName.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.getResult().size() == 0) {
                                        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                                        HashMap<String, Object> group = new HashMap<>();
                                        group.put("name", groupName.getText().toString());
                                        group.put("password", groupPassword.getText().toString());
                                        ArrayList<String> members = new ArrayList<>();
                                        members.add(preferences.getString("id", ""));
                                        group.put("members", members);
                                        group.put("organizer", preferences.getString("id", ""));
                                        db.collection("groups").add(group);
                                        finish();
                                    } else {
                                        errorTV.setText("Группа с таким названием уже существует.");
                                    }
                                }
                            });
                }
            }
        });
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupName.getText().toString().equals("") && groupPassword.getText().toString().equals("")) {
                    errorTV.setText("Не все поля заполнены.");
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("groups")
                            .whereEqualTo("name", groupName.getText().toString())
                            .whereEqualTo("password", groupPassword.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.getResult().size() > 0) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                                            ArrayList<String> members = (ArrayList<String>) document.get("members");
                                            if (members.contains(preferences.getString("id", ""))) {
                                                errorTV.setText("Вы уже есть в этой группе.");
                                            } else {
                                                members.add(preferences.getString("id", ""));
                                                db.collection("groups").document(document.getId()).update("members", members);
                                                finish();
                                            }
                                        }
                                    } else {
                                        errorTV.setText("Название или пароль введены неверно.");
                                    }
                                }
                            });
                }
            }
        });
    }
}
