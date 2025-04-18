package com.liao.weatherapp.weatherfactory;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherApiService {
    @GET("api/weather/city/{cityCode}")
    Call<WeatherData> getWeatherByCity(@Path("cityCode") String cityCode);
}
