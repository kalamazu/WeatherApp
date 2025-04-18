package com.liao.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.liao.weatherapp.R;
import com.liao.weatherapp.weatherfactory.LocateData;
import com.liao.weatherapp.weatherfactory.LocationApiService;
import com.liao.weatherapp.weatherfactory.RetrofitClient;
import com.liao.weatherapp.weatherfactory.RetrofitClient2;
import com.liao.weatherapp.weatherfactory.WeatherApiService;
import com.liao.weatherapp.weatherfactory.WeatherData;
import com.zaaach.citypicker.model.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherActivity extends AppCompatActivity {
    private ImageView weatherview;
private TextView cityname;
private TextView weather;
private TextView shidu;
private TextView daytemp;
private TextView temp;
private TextView air;
private TextView weekday;
private TextView wind;
private TextView sun;
private ImageButton updatebutton;
private TextView date;
private TextView day1;
private TextView day2;
private TextView day3;
private TextView day4;
private TextView day5;
private TextView day6;
private TextView day7;
private WeatherData weatherData;
private LocateData locateData;
private Button btncitypicker;
private SharedPreferences sharedPreferences;

private String citycode="101070101";


private final static String key="6LDBZ-BF4HK-G2ZJA-ABCNH-3A44S-7OFZI";


private Button btnlocate;
private ImageButton calenbtn;

private ImageButton playerbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cityname=findViewById(R.id.city);
        weather=findViewById(R.id.weather);
        air=findViewById(R.id.air);
        wind=findViewById(R.id.wind);
        sun=findViewById(R.id.sun);
        temp=findViewById(R.id.temp);
        weekday=findViewById(R.id.weekday);
        daytemp=findViewById(R.id.daytemp);
        shidu=findViewById(R.id.shidu);
        updatebutton=findViewById(R.id.updatebtn);
        date=findViewById(R.id.date);
        day1=findViewById(R.id.day1);
        day2=findViewById(R.id.day2);
        day3=findViewById(R.id.day3);
        day4=findViewById(R.id.day4);
        day5=findViewById(R.id.day5);
        day6=findViewById(R.id.day6);
        day7=findViewById(R.id.day7);
        btnlocate=findViewById(R.id.locabtn);
        btncitypicker=findViewById(R.id.citychange);
        sharedPreferences=getPreferences(Context.MODE_PRIVATE);
        playerbtn=findViewById(R.id.Playerbtn);
        calenbtn=findViewById(R.id.calenbtn);


        Intent intent=getIntent();
        if(intent.getStringExtra("adcode")!=null){

            this.citycode=intent.getStringExtra("adcode");
        }

        getWeatherInfo(this.citycode);

        playerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WeatherActivity.this, PlayerActivity.class);
                startActivity(intent);

            }
        });

        calenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WeatherActivity.this, CalendarActivity.class);
                startActivity(intent);

            }
        });

        btnlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLocate(key);
            }
        });

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getWeatherInfo(citycode);

            }
        });


        btncitypicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WeatherActivity.this, CitychooseActivity.class);

                startActivity(intent);

                citycode=intent.getStringExtra("adcode");
                finish();

            }
        });


    }
    public void getLocate(String key){
        LocationApiService service= RetrofitClient2.getClient().create(LocationApiService.class);
        service.getLocation(key).enqueue(new Callback<LocateData>() {

            @Override
            public void onResponse(Call<LocateData> call, Response<LocateData> response) {
                if(response.isSuccessful()){
                    locateData=response.body();

                    Toast.makeText(WeatherActivity.this,locateData.getResult().getAdInfo().getCity(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LocateData> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "API_Locate加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getWeatherInfo(String citycode){
        WeatherApiService service= RetrofitClient.getClient().create(WeatherApiService.class);
        service.getWeatherByCity(citycode).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.isSuccessful()){
                    weatherData = response.body();
                    updateUI(weatherData);
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "API加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(WeatherData weatherData){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        List<WeatherData.Forecast> forecasts = weatherData.getData().getForecast();
        WeatherData.Forecast thisDay = forecasts.get(0);
        WeatherData.Data data=weatherData.getData();
        cityname.setText(weatherData.getCityInfo().getCity());
        editor.putString("cityname",weatherData.getCityInfo().getCity());
        weather.setText(thisDay.getType());
        editor.putString("weather",thisDay.getType());

        switch(thisDay.getType()){
            case "晴":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.sun_foreground);
                break;
            case "多云":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weathercloudy_foreground);
                break;
            case "阴":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatheryintian_foreground);
                break;
            case "小雨":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatherrain_foreground);
                break;
            case "中雨":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatherrain_foreground);
                break;
            case "大雨":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatherrain_foreground);
                break;
        }
        daytemp.setText(thisDay.getLow()+"~"+thisDay.getHigh());
        editor.putString("daytime",thisDay.getLow()+"~"+thisDay.getHigh());
        shidu.setText(data.getShidu());
        editor.putString("shidu",data.getShidu());
        weekday.setText(thisDay.getWeek());
        editor.putString("weekday",thisDay.getWeek());
        wind.setText(thisDay.getFx()+thisDay.getFl());
        editor.putString("wind",thisDay.getFx()+thisDay.getFl());
        air.setText(data.getQuality()+"  pm25:"+data.getPm25()+"  pm10:"+data.getPm10());
        editor.putString("air","  pm25:"+data.getPm25()+"  pm10:"+data.getPm10());
        temp.setText(data.getWendu()+"℃");
        editor.putString("temp",data.getWendu()+"℃");
        sun.setText(thisDay.getSunrise()+"~~"+thisDay.getSunset());
        editor.putString("sun",thisDay.getSunrise()+"~~"+thisDay.getSunset());
        date.setText(weatherData.getTime());
        editor.putString("date",weatherData.getTime());
        editor.putString("viewtype",thisDay.getType());
        editor.apply();

        for (int i = 1; i < 8; i++) {
            WeatherData.Forecast dayForecast = forecasts.get(i);
            String weatherInfo =
                    dayForecast.getWeek() + "\n" +
                            dayForecast.getLow() +"\n" + dayForecast.getHigh() + "\n" +
                            dayForecast.getFx() + " " + dayForecast.getFl() +"\n"+ dayForecast.getType();

            switch (i) {
                case 1:
                    day1.setText(weatherInfo);
                    break;
                case 2:
                    day2.setText(weatherInfo);
                    break;
                case 3:
                    day3.setText(weatherInfo);
                    break;
                case 4:
                    day4.setText(weatherInfo);
                    break;
                case 5:
                    day5.setText(weatherInfo);
                    break;
                case 6:
                    day6.setText(weatherInfo);
                    break;
                case 7:
                    day7.setText(weatherInfo);
                    break;
            }
        }

        Toast.makeText(WeatherActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
    }

    private void updateUI_local(){
        cityname.setText(sharedPreferences.getString("cityname","None"));
        weather.setText(sharedPreferences.getString("weather","None"));
        switch(sharedPreferences.getString("viewtype","None")){
            case "晴":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.sun_foreground);
                break;
            case "多云":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weathercloudy_foreground);
                break;
            case "阴":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatheryintian_foreground);
                break;
            case "小雨":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatherrain_foreground);
                break;
            case "中雨":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatherrain_foreground);
                break;
            case "大雨":
                weatherview=findViewById(R.id.weaterhview);
                weatherview.setImageResource(R.mipmap.weatherrain_foreground);
                break;
        }
        daytemp.setText(sharedPreferences.getString("daytemp","None"));
        shidu.setText(sharedPreferences.getString("shidu","None"));
        weekday.setText(sharedPreferences.getString("weekday","None"));
        wind.setText(sharedPreferences.getString("wind","None"));
        air.setText(sharedPreferences.getString("air","None"));
        temp.setText(sharedPreferences.getString("temp","None"));
        sun.setText(sharedPreferences.getString("sun","None"));
        date.setText(sharedPreferences.getString("date","None"));


    }



}