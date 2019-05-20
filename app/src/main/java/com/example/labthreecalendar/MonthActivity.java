package com.example.labthreecalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MonthActivity extends AppCompatActivity {
    public static Map<String, ArrayList<Pair<String, String>>> allEvents;
    static {
        allEvents = new HashMap<>();
    }

    Calendar calendar;
    GridView gridMonth;
    TableRow tableRow;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        calendar = Calendar.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNameDayOfWeek();

        gridMonth = findViewById(R.id.monthGrid);
        setGridMonth(gridMonth);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final int width = display.getWidth() / 5;

        gridMonth.setOnTouchListener(new View.OnTouchListener() {
            float previouspoint = 0 ;
            float startPoint = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startPoint = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP: {
                        previouspoint = event.getX();
                        int numberOfMonth;
                        if (previouspoint - startPoint >= width) {
                            numberOfMonth = calendar.get(Calendar.MONTH) - 1;
                            if (numberOfMonth == -1) {
                                numberOfMonth = 11;
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
                            }
                            calendar.set(Calendar.MONTH, numberOfMonth);
                            setGridMonth(gridMonth);
                        } else if (startPoint - previouspoint >= width) {
                            numberOfMonth = calendar.get(Calendar.MONTH) + 1;
                            if (numberOfMonth == 12) {
                                numberOfMonth = 0;
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                            }
                            calendar.set(Calendar.MONTH, numberOfMonth);
                            setGridMonth(gridMonth);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }


    public void createNameDayOfWeek() {
        tableRow = findViewById(R.id.weekDays);
        for (int i = 1; i <= 7; i++) {
            TextView textView = new TextView(this);
            textView.setText(chooseNameOfWeek(i));
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                textView.setTextSize(15);
            else
                textView.setTextSize(10);
            if (i < 6)
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            else
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorBusy79));
            textView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(0, 0, 0, 10);
            textView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1)
            );
            tableRow.addView(textView);
        }
    }

    public String chooseNameOfWeek(int i) {
        switch (i) {
            case 1: return "M";
            case 2: return "T";
            case 3: return "W";
            case 4: return "T";
            case 5: return "F";
            case 6: return "S";
            case 7: return "S";
            default: return "";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemaddevent: {
                Intent intent = new Intent(MonthActivity.this, AddEventActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.today: {
                calendar = Calendar.getInstance();
                setGridMonth(gridMonth);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void setGridMonth(GridView gridMonth) {
        getSupportActionBar().setTitle(
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + ", " +
                        calendar.get(Calendar.YEAR)
        );
        ArrayList<Integer> days = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int startPosition = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (startPosition == 0) startPosition = 7;
        if (startPosition == 1) startPosition = 8;
        int day;
        calendar.add(Calendar.MONTH, -1);
        day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - startPosition + 2;
        for (int i = 0; i < startPosition - 1; i++) {
            days.add(calendar.get(Calendar.YEAR));
            days.add(calendar.get(Calendar.MONTH));
            days.add(day);
            day++;
        }
        calendar.add(Calendar.MONTH, 1);
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            days.add(calendar.get(Calendar.YEAR));
            days.add(calendar.get(Calendar.MONTH));
            days.add(i);
        }
        day = 1;
        for (int i = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + startPosition - 1; i < 42; i++) {
            days.add(calendar.get(Calendar.YEAR));
            days.add(calendar.get(Calendar.MONTH) + 1);
            days.add(day);
            day++;
        }
        if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
            String title = getSupportActionBar().getTitle().toString();
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
        } else if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
            String title = getSupportActionBar().getTitle().toString();
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), title.indexOf(" ") + 1, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
        }
        gridMonth.setAdapter(new MonthGridAdapter(this, calendar, days, this));
    }
}
