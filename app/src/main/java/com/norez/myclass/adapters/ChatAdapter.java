package com.norez.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.norez.myclass.models.Message;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<Message> {
    public ChatAdapter(@NonNull Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = getItem(position);

        SharedPreferences preferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);

        if (convertView == null) {
            if (message.sender.equals(preferences.getString("id", "")))
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.sent_message, parent, false);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.received_message, parent, false);
        }

        convertView.setClickable(false);

        TextView textMessage = convertView.findViewById(R.id.textMessage);
        TextView textTime = convertView.findViewById(R.id.textTime);

        textMessage.setText(message.message);
        textTime.setText(message.date);

        return convertView;
    }
}
