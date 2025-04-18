package com.liao.weatherapp.Calenderfactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Schedule {
    private int id;
    private String title;
    private String description;
    private String date; // 格式: yyyy-MM-dd
    private String time; // 格式: HH:mm

    // 空构造函数
    public Schedule() {}

    // 基本构造函数
    public Schedule(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    // 完整构造函数
    public Schedule(int id, String title, String description, String date, String time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // 获取完整日期时间字符串
    public String getDateTime() {
        return date + " " + time;
    }

    // 获取格式化的日期显示 (如：2023年6月15日)
    public String getFormattedDate() {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
            Date d = sdfInput.parse(date);
            return sdfOutput.format(d);
        } catch (ParseException e) {
            return date; // 如果解析失败，返回原始字符串
        }
    }

    // 获取格式化的时间显示 (如：下午 02:30)
    public String getFormattedTime() {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("a hh:mm", Locale.getDefault());
            Date t = sdfInput.parse(time);
            return sdfOutput.format(t);
        } catch (ParseException e) {
            return time; // 如果解析失败，返回原始字符串
        }
    }

    // 获取时间戳 (毫秒)
    public long getTimestamp() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date dateTime = sdf.parse(date + " " + time);
            return dateTime != null ? dateTime.getTime() : 0;
        } catch (ParseException e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    // 比较两个日程是否相同
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return id == schedule.id &&
                title.equals(schedule.title) &&
                date.equals(schedule.date) &&
                time.equals(schedule.time);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }
}