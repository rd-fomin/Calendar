package com.example.labthreecalendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private String day;

    EventAdapter(String day) {
        this.day = day;
    }

    @NotNull
    @Override
    public EventHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event, viewGroup, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(EventHolder viewHolder, int position) {
        viewHolder.timeEvent.setText(MonthActivity.allEvents.get(day).get(position).first);
        viewHolder.titleEvent.setText(MonthActivity.allEvents.get(day).get(position).second);
    }

    @Override
    public int getItemCount() {
        return MonthActivity.allEvents.get(day).size();
    }

    static class EventHolder extends RecyclerView.ViewHolder {
        TextView timeEvent;
        TextView titleEvent;


        EventHolder(View itemView) {
            super(itemView);
            timeEvent = itemView.findViewById(R.id.timeEvent);
            titleEvent = itemView.findViewById(R.id.titleEvent);
        }
    }
}
