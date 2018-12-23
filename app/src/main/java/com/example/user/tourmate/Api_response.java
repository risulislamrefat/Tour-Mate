package com.example.user.tourmate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api_response {
    private static Retrofit retrofit;
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static String base_url1="https://maps.googleapis.com/maps/api/";

    public static Retrofit getUser() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }

    public static Retrofit getRetrofit(){

        retrofit = new Retrofit.Builder().baseUrl(base_url1).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }

}
