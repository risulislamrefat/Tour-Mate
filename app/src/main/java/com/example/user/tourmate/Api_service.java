package com.example.user.tourmate;

import com.example.user.tourmate.Nearby.NearbyResponse;
import com.example.user.tourmate.forecast.ForcastResponse;
import com.example.user.tourmate.weather.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api_service {
    @GET()
    Call<UserResponse> getAllUser(@Url String url);
    @GET()
    Call<NearbyResponse> getNearby(@Url String url);
    @GET
    Call<NearbyResponse> getNextPageToken(@Url String url);
    @GET
    Call<ForcastResponse> getForecastResults(@Url String url);



}
