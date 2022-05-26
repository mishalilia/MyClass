package com.norez.myclass.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

import static com.norez.myclass.utils.CalendarUtils.daysInMonthArray;
import static com.norez.myclass.utils.CalendarUtils.monthYearFromDate;

import com.google.firebase.firestore.FirebaseFirestore;
import com.norez.myclass.adapters.CalendarAdapter;
import com.norez.myclass.models.Group;
import com.norez.myclass.models.Lesson;
import com.norez.myclass.utils.CalendarUtils;
import com.norez.myclass.R;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    public static boolean creatingConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

        setMonthView();
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate).substring(0, 1).toUpperCase() +
                monthYearFromDate(CalendarUtils.selectedDate).substring(1));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            if (creatingConf) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CalendarActivity.this);
                alertDialog.setTitle("Введите время");
                EditText time = new EditText(this);
                alertDialog.setView(time);
                alertDialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                HashMap<String, Object> lesson = new HashMap<>();
                                lesson.put("name", Group.current_group.getName());
                                lesson.put("dateYear", date.getYear());
                                lesson.put("dateMonth", date.getMonthValue());
                                lesson.put("dateDay", date.getDayOfMonth());
                                lesson.put("time", time.getText().toString());
                                lesson.put("homework", "");
                                db.collection("lessons").add(lesson);
                                Toast.makeText(getApplicationContext(),"Конференция назначена", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            } else {
                CalendarUtils.selectedDate = date;
                setMonthView();
                finish();
            }
        }
    }

    public void weeklyAction(View view) {
        finish();
        creatingConf = false;
    }
}







