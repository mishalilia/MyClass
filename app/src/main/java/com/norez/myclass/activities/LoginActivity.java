package com.norez.myclass.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;

public class LoginActivity extends AppCompatActivity {
    public EditText email, password;
    public Button loginButton;
    public TextView dhaveaccTV, errorTV;
    public CheckBox rememberMe;
    public LinearLayout linearLayout;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        email = findViewById(R.id.email_l);
        password = findViewById(R.id.password_l);
        loginButton = findViewById(R.id.loginButton);
        dhaveaccTV = findViewById(R.id.dhaveaccountTextView);
        errorTV = findViewById(R.id.errorTextView_l);
        rememberMe = findViewById(R.id.rememberMe);
        linearLayout = findViewById(R.id.linearLayout);
        progressBar = findViewById(R.id.progressBar);

        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        dhaveaccTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(i, 1);
            }
        });

        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .whereEqualTo("email", email.getText().toString())
                        .whereEqualTo("password", password.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().size() > 0) {
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    errorTV.setText("");
                                    editor.putString("email", email.getText().toString());
                                    editor.putString("password", password.getText().toString());
                                    for (QueryDocumentSnapshot document: task.getResult()) {
                                        editor.putString("id", document.getId());
                                    }
                                    if (rememberMe.isChecked()) {
                                        editor.putString("remember", "true");
                                    }
                                    editor.apply();
                                    finish();
                                } else {
                                    errorTV.setText("Почта или пароль введены неверно.");
                                    if (preferences.getString("remember", "").equals("true")) {
                                        editor.putString("email", "");
                                        editor.putString("password", "");
                                        editor.putString("remember", "false");
                                        editor.apply();
                                    }
                                }
                            }
                        });
            }
        };

        loginButton.setOnClickListener(loginListener);

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        if (preferences.getString("remember", "").equals("true")) {
            email.setText(preferences.getString("email", ""));
            password.setText(preferences.getString("password", ""));
            rememberMe.setChecked(true);
            loginListener.onClick(loginButton);
        } else {
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                email.setText(data.getStringExtra("email"));
                break;
        }
    }
}