package com.example.labthreecalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity {
    int DIALOG_TIME, DIALOG_DATE;
    Calendar calendar;
    TimePickerDialog.OnTimeSetListener myCallBackTime;
    DatePickerDialog.OnDateSetListener myCallBackDate;
    TextView time, date, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Toolbar toolbar = findViewById(R.id.toolbarAddEvent);
        toolbar.setTitle("New Event");
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_ios_24);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        calendar = Calendar.getInstance();
        DIALOG_TIME = 1;
        DIALOG_DATE = 2;

        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        title = findViewById(R.id.fieldTitle);

        time.setText(String.valueOf(String.format(Locale.ENGLISH, "%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))));
        date.setText(String.format(Locale.ENGLISH, "%02d.%02d.%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));

        myCallBackTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                time.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minuteOfHour));
            }
        };

        myCallBackDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int numberOfYear, int numberOfMonth, int numberOfDay) {
                date.setText(String.format(Locale.ENGLISH, "%02d.%02d.%4d", numberOfDay, numberOfMonth + 1, numberOfYear));
            }
        };

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            return new DatePickerDialog(
                    this,
                    AlertDialog.THEME_HOLO_DARK,
                    myCallBackDate,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        } else if (id == DIALOG_TIME) {
            return new TimePickerDialog(this,
                    AlertDialog.THEME_HOLO_DARK,
                    myCallBackTime,
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    true
            );
        }
        return super.onCreateDialog(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addeventmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent(AddEventActivity.this, MonthActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ok: {
                if (!MonthActivity.allEvents.isEmpty()) {
                    if (!MonthActivity.allEvents.containsKey(date.getText().toString())) {
                        ArrayList<Pair<String, String>> allDayEvents = new ArrayList<>();
                        MonthActivity.allEvents.put(date.getText().toString(), allDayEvents);
                    }
                } else {
                    ArrayList<Pair<String, String>> allDayEvents = new ArrayList<>();
                    MonthActivity.allEvents.put(date.getText().toString(), allDayEvents);
                }
                MonthActivity.allEvents.get(date.getText().toString()).add(new Pair<>(time.getText().toString(), title.getText().toString()));
                Collections.sort(MonthActivity.allEvents.get(date.getText().toString()), new Comparator<Pair<String, String>>() {
                    @Override
                    public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                        return o1.first.compareTo(o2.first);
                    }
                });
                Intent intent = new Intent(AddEventActivity.this, MonthActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }
}
