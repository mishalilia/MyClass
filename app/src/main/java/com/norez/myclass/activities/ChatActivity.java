package com.norez.myclass.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;
import com.norez.myclass.adapters.ChatAdapter;
import com.norez.myclass.models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    ImageView back, sendButton;
    TextView name;
    ProgressBar progressBar;
    ListView chatRV;
    EditText messageET;
    ArrayList<Message> chatMessages;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        back = findViewById(R.id.back);
        name = findViewById(R.id.person_name);
        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);
        chatRV = findViewById(R.id.chatRV);
        messageET = findViewById(R.id.messageET);

        progressBar.setVisibility(View.VISIBLE);
        chatRV.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        updateChat();

        name.setText(getIntent().getStringExtra("name"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageET.getText().toString().equals("")) {
                    HashMap<String, Object> message = new HashMap<>();
                    message.put("sender", preferences.getString("id", ""));
                    message.put("message", messageET.getText().toString());
                    message.put("timestamp", new Date());

                    HashMap<String, Object> chat = new HashMap<>();

                    ArrayList<String> members = new ArrayList<>();
                    members.add(getIntent().getStringExtra("id"));
                    members.add(preferences.getString("id", ""));
                    members.sort(Comparator.naturalOrder());

                    chat.put("members", members);
                    chat.put("last_message", message.get("message"));
                    db.collection("chats").whereEqualTo("members", members).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().size() == 0) {
                                db.collection("chats").add(chat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        task.getResult().collection("messages").add(message);
                                        updateChat();
                                    }
                                });
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().collection("messages").add(message);
                                    document.getReference().update("last_message", chat.get("last_message"));
                                    updateChat();
                                }
                            }
                        }
                    });
                    messageET.setText("");
                }
            }
        });
    }

    public void updateChat() {
        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        chatMessages = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list.add(preferences.getString("id", ""));
        list.add(getIntent().getStringExtra("id"));
        list.sort(Comparator.naturalOrder());
        db.collection("chats")
                .whereEqualTo("members", list)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                for (QueryDocumentSnapshot chat: task1.getResult()) {
                    chat.getReference().collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            for (QueryDocumentSnapshot mdocument: task2.getResult()) {
                                Message message = new Message();
                                message.sender = mdocument.getString("sender");
                                message.dateObject = mdocument.getDate("timestamp");
                                message.date = new SimpleDateFormat("dd MMMM, yyyy - hh:mm a", Locale.getDefault()).format(mdocument.getDate("timestamp"));
                                message.message = mdocument.getString("message");
                                chatMessages.add(message);
                            }
                            chatMessages.sort((obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                            chatAdapter = new ChatAdapter(getApplicationContext(), chatMessages);
                            chatRV.setAdapter(chatAdapter);
                            chatRV.smoothScrollToPosition(chatMessages.size() - 1);
                            progressBar.setVisibility(View.GONE);
                            chatRV.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}
