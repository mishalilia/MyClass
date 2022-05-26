package com.norez.myclass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    public EditText name, surname, email, password;
    public Button registrationButton;
    public TextView haveaccTV, errorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        name = findViewById(R.id.name_r);
        surname = findViewById(R.id.surname_r);
        email = findViewById(R.id.email_r);
        password = findViewById(R.id.password_r);
        registrationButton = findViewById(R.id.registrationButton);
        haveaccTV = findViewById(R.id.haveaccountTextView);
        errorTV = findViewById(R.id.errorTextView_r);

        haveaccTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                finish();
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")
                        || surname.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || password.getText().toString().equals("")) {
                    errorTV.setText("Не все поля заполнены.");
                }
                else if (password.getText().toString().length() < 8) {
                    errorTV.setText("Длина пароля должна быть не менее 8 символов.");
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users")
                            .whereEqualTo("email", email.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.getResult().size() != 0) {
                                        errorTV.setText("Пользователь с такой почтой уже существует");
                                    } else {
                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put("name", name.getText().toString());
                                        user.put("surname", surname.getText().toString());
                                        user.put("email", email.getText().toString());
                                        user.put("password", password.getText().toString());
                                        db.collection("users").add(user);
                                        Intent i = new Intent();
                                        i.putExtra("email", email.getText().toString());
                                        setResult(RESULT_OK, i);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}