package com.norez.myclass.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.norez.myclass.R;

public class ChatActivity extends AppCompatActivity {
    ImageView back;
    TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        back = findViewById(R.id.back);
        name = findViewById(R.id.person_name);
        
        name.setText(getIntent().getStringExtra("name"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
