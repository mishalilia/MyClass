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
import com.norez.myclass.activities.ChatActivity;
import com.norez.myclass.models.Chat;

import java.util.ArrayList;

public class MessagesAdapter extends ArrayAdapter<Chat> {

    public MessagesAdapter(@NonNull Context context, ArrayList<Chat> chats) {
        super(context, 0, chats);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Chat chat = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_cell, parent, false);

        TextView name = convertView.findViewById(R.id.name);
        TextView last_message = convertView.findViewById(R.id.last_message);

        name.setText(chat.getName());
        last_message.setText(chat.getLast_message());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                i.putExtra("name", chat.getName());
                i.putExtra("id", chat.getId());
                getContext().startActivity(i);
            }
        });

        return convertView;
    }
}
