package com.example.user.tourmate;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tourmate.forecast.ForcastResponse;
import com.example.user.tourmate.weather.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView weather,forecast;
    private Api_service service;
    private String unit = "metric";
    private double latn,lonn;
    private String address;
    private List<com.example.user.tourmate.forecast.List> forcastResponseList;
    private RecyclerView recyclerView;
    private ForecastAdapter forecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weather = findViewById(R.id.weatherTv);
        forcastResponseList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewIDForecast);

        latn = getIntent().getDoubleExtra("latitude",0);
        lonn = getIntent().getDoubleExtra("longitude",0);
        address = getIntent().getStringExtra("Address");
        String a = String.valueOf(latn);
        //Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

        String url = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s",latn,lonn,unit,getResources().getString(R.string.weather_key));

        service = Api_response.getUser().create(Api_service.class);
        Call<UserResponse> weatherResponseCall = service.getAllUser(url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        weatherResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call <UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    UserResponse userResponse = response.body();
                    progressDialog.dismiss();
                    weather.setText(userResponse.getSys().getCountry().toString()+" "+"Temp: "+userResponse.getMain().getTempMin().toString()+"°C");
                }
            }

            @Override
            public void onFailure(Call <UserResponse> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        String forecastUrl = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",latn,lonn,unit,getResources().getString(R.string.weather_key));

        Call<ForcastResponse> forcastResponseCall = service.getForecastResults(forecastUrl);
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Please wait...");
        progressDialog1.show();
        progressDialog1.setCanceledOnTouchOutside(false);

        forcastResponseCall.enqueue(new Callback<ForcastResponse>() {
            @Override
            public void onResponse(Call<ForcastResponse> call, Response<ForcastResponse> response) {
                if(response.code() == 200) {
                    ForcastResponse forcastResponse = response.body();
                    progressDialog1.dismiss();
                    Toast.makeText(WeatherActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    /*forecast.setText(forcastResponse.getList().get(0).getMain().getTempMin().toString());*/
                    forcastResponseList = forcastResponse.getList();
                    forecastAdapter = new ForecastAdapter(forcastResponseList,WeatherActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(WeatherActivity.this));
                    recyclerView.setAdapter(forecastAdapter);

                }
            }

            @Override
            public void onFailure(Call<ForcastResponse> call, Throwable t) {

            }
        });

    }
}
