package com.sunset.trojanos.sunsetapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static final String BASE_URL = "https://api.sunrise-sunset.org/";
    private SunsetApi sunsetApi;

    public SunsetApi getSunsetApi() {

        if (sunsetApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            sunsetApi = retrofit.create(SunsetApi.class);
        }


        return sunsetApi;
    }
}
