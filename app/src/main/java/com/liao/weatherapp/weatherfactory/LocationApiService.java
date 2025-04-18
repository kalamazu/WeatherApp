package com.liao.weatherapp.weatherfactory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationApiService {
    @GET("ws/location/v1/ip")  // 不要在 URL 里写 ?key={key}
    Call<LocateData> getLocation(@Query("key") String key);  // 使用 @Query 注解
}