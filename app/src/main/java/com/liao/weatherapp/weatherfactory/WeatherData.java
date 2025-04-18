package com.liao.weatherapp.weatherfactory;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherData {

    private String message;
    private int status;
    private String date;
    private String time;
    private CityInfo cityInfo;
    private Data data;

    // Getters and Setters
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public CityInfo getCityInfo() { return cityInfo; }
    public Data getData() { return data; }

    public static class CityInfo {
        private String city;
        private String citykey;
        private String parent;
        @SerializedName("updateTime")
        private String updateTime;

        public String getCity() { return city; }
        public String getCityKey() { return citykey; }
        public String getParent() { return parent; }
        public String getUpdateTime() { return updateTime; }
    }

    public static class Data {
        private String shidu;
        private double pm25;
        private double pm10;
        private String quality;
        private String wendu;
        private String ganmao;
        private List<Forecast> forecast;  // 关键点：forecast 是列表
        private Yesterday yesterday;

        public String getShidu() { return shidu; }
        public double getPm25() { return pm25; }
        public double getPm10() { return pm10; }
        public String getQuality() { return quality; }
        public String getWendu() { return wendu; }
        public String getGanmao() { return ganmao; }
        public List<Forecast> getForecast() { return forecast; } // 返回列表
        public Yesterday getYesterday() { return yesterday; }
    }

    public static class Forecast {
        private String date;
        private String high;
        private String low;
        private String ymd;
        private String week;
        private String sunrise;
        private String sunset;
        private int aqi;
        private String fx;
        private String fl;
        private String type;
        private String notice;

        // Getters
        public String getDate() { return date; }
        public String getHigh() { return high; }
        public String getLow() { return low; }
        public String getYmd() { return ymd; }
        public String getWeek() { return week; }
        public String getSunrise() { return sunrise; }
        public String getSunset() { return sunset; }
        public int getAqi() { return aqi; }
        public String getFx() { return fx; }
        public String getFl() { return fl; }
        public String getType() { return type; }
        public String getNotice() { return notice; }
    }

    public static class Yesterday {
        private String date;
        private String high;
        private String low;
        private String ymd;
        private String week;
        private String sunrise;
        private String sunset;
        private int aqi;
        private String fx;
        private String fl;
        private String type;
        private String notice;

        // Getters
        public String getDate() { return date; }
        public String getHigh() { return high; }
        public String getLow() { return low; }
        public String getYmd() { return ymd; }
        public String getWeek() { return week; }
        public String getSunrise() { return sunrise; }
        public String getSunset() { return sunset; }
        public int getAqi() { return aqi; }
        public String getFx() { return fx; }
        public String getFl() { return fl; }
        public String getType() { return type; }
        public String getNotice() { return notice; }
    }
}