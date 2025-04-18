package com.liao.weatherapp.User;

        import android.content.SharedPreferences;

        import com.liao.weatherapp.weatherfactory.WeatherData;

public class UserData {
    private  String UserName;
    private String ID;
    private String mail;

    private String saved_city;


    private String language;

    private boolean login_status;

    private WeatherData last_weatherdata;
    private SharedPreferences sharedPreferences;
    public void setUsername(String username){
        this.UserName=username;
    }
}
