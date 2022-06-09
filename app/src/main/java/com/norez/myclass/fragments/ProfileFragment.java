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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;
import com.norez.myclass.activities.LoginActivity;

public class ProfileFragment extends Fragment {
    TextView nameProfile, emailProfile;
    Button logoutButton;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        if (isAdded()) {
            nameProfile = v.findViewById(R.id.nameProfile);
            emailProfile = v.findViewById(R.id.emailProfile);
            progressBar = v.findViewById(R.id.progressBar);
            linearLayout = v.findViewById(R.id.linearLayout);
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            SharedPreferences preferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .whereEqualTo("email", preferences.getString("email", ""))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nameProfile.setText(document.get("name").toString() + " " + document.get("surname").toString());
                                emailProfile.setText(preferences.getString("email", ""));
                                progressBar.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
            logoutButton = v.findViewById(R.id.logoutButton);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.putString("login", "");
                    editor.putString("password", "");
                    editor.putString("email", "");
                    editor.putString("id", "");
                    editor.apply();
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            });
        }

        return v;
    }
}