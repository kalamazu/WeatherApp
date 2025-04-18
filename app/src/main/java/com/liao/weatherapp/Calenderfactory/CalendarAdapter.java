package com.liao.weatherapp.Calenderfactory;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<String> days;
    private String currentDay;

    public CalendarAdapter(Context context, List<String> days, String currentDay) {
        this.context = context;
        this.days = days;
        this.currentDay = currentDay;
    }

    @Override
    public int getCount() {
        return days.size();
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
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        String day = days.get(position);
        textView.setText(day);


        if (!day.isEmpty() && day.equals(currentDay)) {
            textView.setBackgroundColor(Color.YELLOW);
        } else {
            textView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }
}