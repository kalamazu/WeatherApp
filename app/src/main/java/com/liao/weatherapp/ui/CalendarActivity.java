package com.liao.weatherapp.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.liao.weatherapp.Calenderfactory.CalendarAdapter;
import com.liao.weatherapp.Calenderfactory.Schedule;
import com.liao.weatherapp.Calenderfactory.ScheduleAdapter;
import com.liao.weatherapp.R;

public class CalendarActivity extends AppCompatActivity {
    private GridView gridCalendar;
    private ListView listSchedules;
    private TextView tvDate;
    private Button btnAdd;

    private CalendarAdapter calendarAdapter;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList = new ArrayList<>();

    private Calendar currentCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);


        gridCalendar = findViewById(R.id.grid_calendar);
        listSchedules = findViewById(R.id.list_schedules);
        tvDate = findViewById(R.id.tv_date);
        btnAdd = findViewById(R.id.btn_add);


        updateDateDisplay();
        updateCalendarGrid();
        updateScheduleList();


        findViewById(R.id.btn_day).setOnClickListener(v -> showDayView());
        findViewById(R.id.btn_week).setOnClickListener(v -> showWeekView());
        findViewById(R.id.btn_month).setOnClickListener(v -> showMonthView());

        btnAdd.setOnClickListener(v -> showAddScheduleDialog());


        gridCalendar.setOnItemClickListener((parent, view, position, id) -> {
            String day = (String) calendarAdapter.getItem(position);
            if (!day.isEmpty()) {
                currentCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                updateDateDisplay();
                updateScheduleList();
            }
        });


        listSchedules.setOnItemClickListener((parent, view, position, id) -> {
            Schedule schedule = scheduleList.get(position);
            showEditScheduleDialog(schedule);
        });
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.getDefault());
        tvDate.setText(sdf.format(currentCalendar.getTime()));
    }

    private void updateCalendarGrid() {
        List<String> days = new ArrayList<>();
        Calendar calendar = (Calendar) currentCalendar.clone();


        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        calendar.add(Calendar.DAY_OF_MONTH, -firstDayOfWeek + 1);
        for (int i = 1; i < firstDayOfWeek; i++) {
            days.add("");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDay; i++) {
            days.add(String.valueOf(i));
        }


        int remaining = 42 - days.size();
        for (int i = 1; i <= remaining; i++) {
            days.add("");
        }


        String currentDay = String.valueOf(currentCalendar.get(Calendar.DAY_OF_MONTH));
        calendarAdapter = new CalendarAdapter(this, days, currentDay);
        gridCalendar.setAdapter(calendarAdapter);
    }

    private void updateScheduleList() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentCalendar.getTime());




        scheduleAdapter = new ScheduleAdapter(this, scheduleList);
        listSchedules.setAdapter(scheduleAdapter);
    }

    private void showDayView() {

        Toast.makeText(this, "日视图", Toast.LENGTH_SHORT).show();
    }

    private void showWeekView() {

        Toast.makeText(this, "周视图", Toast.LENGTH_SHORT).show();
    }

    private void showMonthView() {

        Toast.makeText(this, "月视图", Toast.LENGTH_SHORT).show();
    }

    private void showAddScheduleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加日程");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_schedule, null);
        final EditText etTitle = view.findViewById(R.id.et_title);
        final EditText etTime = view.findViewById(R.id.et_time);

        builder.setView(view);

        builder.setPositiveButton("保存", (dialog, which) -> {
            String title = etTitle.getText().toString();
            String time = etTime.getText().toString();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentCalendar.getTime());

            if (!title.isEmpty() && !time.isEmpty()) {
                Schedule schedule = new Schedule(title, date, time);
                scheduleList.add(schedule);
                scheduleAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showEditScheduleDialog(Schedule schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑日程");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_schedule, null);
        final EditText etTitle = view.findViewById(R.id.et_title);
        final EditText etTime = view.findViewById(R.id.et_time);

        etTitle.setText(schedule.getTitle());
        etTime.setText(schedule.getTime());

        builder.setView(view);

        builder.setPositiveButton("保存", (dialog, which) -> {
            schedule.setTitle(etTitle.getText().toString());
            schedule.setTime(etTime.getText().toString());
            scheduleAdapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("删除", (dialog, which) -> {
            scheduleList.remove(schedule);
            scheduleAdapter.notifyDataSetChanged();
        });

        builder.setNeutralButton("取消", null);
        builder.show();
    }
}