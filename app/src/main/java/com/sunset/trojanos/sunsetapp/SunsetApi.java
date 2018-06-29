package com.sunset.trojanos.sunsetapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SunsetApi {

    @GET("json")
    Call<Response2> getResult(
            @Query("lat") Double lat,
            @Query("lng") Double lng,
            @Query("date") String date
    );
}

