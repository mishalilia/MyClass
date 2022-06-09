package com.norez.myclass.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;
import com.norez.myclass.adapters.MessagesAdapter;
import com.norez.myclass.models.Chat;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {
    ListView chatsListView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);

        if (isAdded()) {
            chatsListView = v.findViewById(R.id.chatsListView);
            progressBar = v.findViewById(R.id.progressBar);
            chatsListView.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }

    void setAdapter() {
        ArrayList<Chat> chatArrayList = new ArrayList<>();
        SharedPreferences preferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").whereArrayContains("members", preferences.getString("id", "")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document: task.getResult()) {
                    Chat chat = new Chat("", document.getString("last_message"));
                    ArrayList<String> members = (ArrayList<String>) document.get("members");
                    for (int i = 0; i < members.size(); i++) {
                        if (!members.get(i).equals(preferences.getString("id", ""))) {
                            db.collection("users").document(members.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    chat.setName(task.getResult().getString("name") + " " + task.getResult().getString("surname"));
                                    chat.setId(task.getResult().getId());
                                    chatArrayList.add(chat);
                                    if (isAdded()) {
                                        MessagesAdapter messagesAdapter = new MessagesAdapter(getContext(), chatArrayList);
                                        chatsListView.setAdapter(messagesAdapter);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    chatsListView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}