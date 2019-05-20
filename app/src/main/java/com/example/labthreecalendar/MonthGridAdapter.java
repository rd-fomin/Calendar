package com.example.labthreecalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MonthGridAdapter extends BaseAdapter {
    private Context context;
    private Calendar calendar;
    private ArrayList<Integer> days;
    private RecyclerView eventsView;
    private ColorDrawable background;
    private Drawable icon;

    MonthGridAdapter(Context context, Calendar calendar, ArrayList<Integer> days, MonthActivity monthActivity) {
        this.context = context;
        this.calendar = calendar;
        this.days = days;
        eventsView = monthActivity.findViewById(R.id.eventsView);
        eventsView.setHasFixedSize(true);
        background = new ColorDrawable(ContextCompat.getColor(context, R.color.colorAccent));
        icon = ContextCompat.getDrawable(context, R.drawable.round_delete_outline_24);
    }

    @Override
    public int getCount() {
        return days.size() / 3;
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.button, parent, false);
            DayButton dayButton = convertView.findViewById(R.id.dayButton);
            dayButton.setNumberOfYear(days.get(3 * position));
            dayButton.setNumberOfMonth(days.get(3 * position + 1));
            dayButton.setNumberOfDay(days.get(3 * position + 2));
            if (days.get(3 * position + 1) >= calendar.get(Calendar.MONTH) && days.get(3 * position + 1) <= calendar.get(Calendar.MONTH)) {
                dayButton.setCurrentMonth(true);
            } else {
                dayButton.setCurrentMonth(false);
            }
            dayButton.setText(String.valueOf(days.get(3 * position + 2)));
            dayButton.setId(position);
            setDefaultColor(dayButton);
            String day = String.format(Locale.ENGLISH,
                    "%02d.%02d.%4d", dayButton.getNumberOfDay(), dayButton.getNumberOfMonth() + 1, dayButton.getNumberOfYear());
            if (MonthActivity.allEvents.containsKey(day)) {
                if (!MonthActivity.allEvents.get(day).isEmpty()) {
                    int count = MonthActivity.allEvents.get(day).size();
                    if (count != 0) {
                        dayButton.setBackgroundEvents(count);
                    }
                }
            }

            dayButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                String currentDay;
                EventAdapter eventAdapter;

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    DayButton dayButton = (DayButton) v;
                    if (hasFocus) {
                        setHighlightColor(dayButton);
                        currentDay = String.format(Locale.ENGLISH,
                                "%02d.%02d.%4d", dayButton.getNumberOfDay(), dayButton.getNumberOfMonth() + 1, dayButton.getNumberOfYear());
                        if (MonthActivity.allEvents.containsKey(currentDay)) {
                            if (!MonthActivity.allEvents.get(currentDay).isEmpty()) {
                                eventAdapter = new EventAdapter(currentDay);
                                eventsView.setAdapter(eventAdapter);
                                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                                    @Override
                                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                                        return false;
                                    }

                                    @Override
                                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                                        int position = viewHolder.getAdapterPosition();
                                        MonthActivity.allEvents.get(currentDay).remove(position);
                                        eventAdapter.notifyItemRemoved(position);
                                        eventAdapter.notifyItemRangeChanged(position, MonthActivity.allEvents.get(currentDay).size());
                                    }

                                    @Override
                                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                                        View itemView = viewHolder.itemView;
                                        int backgroundCornerOffset = 20;
                                        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                                        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                                        if (dX > 0) { // Swiping to the right
                                            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                                            int iconRight = itemView.getLeft() + iconMargin;
                                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                                            background.setBounds(itemView.getLeft(), itemView.getTop(),
                                                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                                                    itemView.getBottom());
                                        } else if (dX < 0) { // Swiping to the left
                                            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                                            int iconRight = itemView.getRight() - iconMargin;
                                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                                            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                                                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
                                        } else { // view is unSwiped
                                            background.setBounds(0, 0, 0, 0);
                                        }

                                        background.draw(c);
                                        icon.draw(c);
                                    }
                                });
                                itemTouchHelper.attachToRecyclerView(eventsView);
                            }
                        }
                    } else {
                        dayButton.setBackgroundResource(R.drawable.day);
                        setDefaultColor(dayButton);
                        eventsView.setAdapter(null);
                        if (MonthActivity.allEvents.containsKey(currentDay)) {
                            if (!MonthActivity.allEvents.get(currentDay).isEmpty()) {
                                int count = MonthActivity.allEvents.get(currentDay).size();
                                if (count != 0) {
                                    dayButton.setBackgroundEvents(count);
                                }
                            }
                        }
                    }
                }
            });

            if (days.get(3 * position + 2) == 1 && days.get(3 * position + 1) == calendar.get(Calendar.MONTH)) {
                dayButton.requestFocus();
            }
            if (dayButton.isToday()) {
                dayButton.requestFocus();
            }
        }
        return convertView;
    }

    private void setDefaultColor(DayButton dayButton) {
        if (dayButton.isCurrentMonth()) {
            if (!dayButton.isWeekend()) {
                dayButton.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            } else {
                dayButton.setTextColor(ContextCompat.getColor(context, R.color.colorBusy79));
            }
        } else {
            dayButton.setTextColor(ContextCompat.getColor(context, R.color.colorBlackLight));
            if (dayButton.isWeekend())
                dayButton.setTextColor(ContextCompat.getColor(context, R.color.colorBusy79Light));
        }
        if (dayButton.isToday()) {
            dayButton.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }

    private void setHighlightColor(DayButton dayButton) {
        if (dayButton.isCurrentMonth()) {
            if (!dayButton.isWeekend()) {
                dayButton.setBackgroundResource(R.drawable.dayhighlight);
            } else {
                dayButton.setBackgroundResource(R.drawable.dayhighlightweekend);
            }
        } else {
            if (!dayButton.isWeekend()) {
                dayButton.setBackgroundResource(R.drawable.dayhighlightlight);
            } else {
                dayButton.setBackgroundResource(R.drawable.dayhighlightweekendlight);
            }
        }
        if (dayButton.isToday()) {
            dayButton.setBackgroundResource(R.drawable.dayhighlightcurrent);
        }
        dayButton.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
    }

}
