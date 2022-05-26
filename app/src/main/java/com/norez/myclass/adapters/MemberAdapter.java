package com.norez.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.norez.myclass.R;
import com.norez.myclass.activities.ChatActivity;
import com.norez.myclass.models.Group;
import com.norez.myclass.models.User;

import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter<String> {
    public MemberAdapter(@NonNull Context context, ArrayList<String> members) {
        super(context, 0, members);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.member_cell, parent, false);

        TextView memberName = convertView.findViewById(R.id.memberName);
        TextView memberEmail = convertView.findViewById(R.id.memberEmail);
        ImageView dialogButton = convertView.findViewById(R.id.dialogButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(getItem(position)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                User member = new User(document.get("name").toString() + " " + document.get("surname").toString(),
                        document.get("email").toString());
                memberName.setText(member.getName());
                memberEmail.setText(member.getEmail());
                SharedPreferences preferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
                if (preferences.getString("email", "").equals(member.getEmail()))
                    dialogButton.setVisibility(View.GONE);
                else
                    dialogButton.setVisibility(View.VISIBLE);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        i.putExtra("name", member.getName());
                        getContext().startActivity(i);
                    }
                });
            }
        });

        return convertView;
    }
}
