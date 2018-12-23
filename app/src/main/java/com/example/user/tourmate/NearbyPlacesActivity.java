package com.example.user.tourmate;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.tourmate.Nearby.NearbyResponse;
import com.example.user.tourmate.Nearby.Result;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyPlacesActivity extends AppCompatActivity {
    private Button moreResult;
    private Api_service service;
    private int radious=1000;
    private RecyclerView recyclerView;
    private NearbyAdapter adapter;
    private Spinner spinner;
    private String pageToken;
    List<Result> results;
    private double latn,lonn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        moreResult =findViewById(R.id.moreresultBtn);
        latn = getIntent().getDoubleExtra("latitude",0);
        lonn = getIntent().getDoubleExtra("longitude",0);

        recyclerView = findViewById(R.id.recyclerviewID);
        spinner = findViewById(R.id.spinnerID);
        List<String> placeList= new ArrayList<>();
        placeList.add("bank");
        placeList.add("restaurant");
        placeList.add("police");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,placeList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String place_type= parent.getItemAtPosition(position).toString();
                getNearby(place_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void getNearby(String place_type) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        String url = String.format("place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s",latn,lonn,radious,place_type,getResources().getString(R.string.place_key));
        service =Api_response.getRetrofit().create(Api_service.class);
        Call<NearbyResponse> nearbyResponseCall = service.getNearby(url);
        nearbyResponseCall.enqueue(new Callback<NearbyResponse>() {
            @Override
            public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                if (response.code()==200){
                    NearbyResponse nearbyResponse = response.body();
                    progressDialog.dismiss();
                    if (nearbyResponse.getNextPageToken()!=null){
                        pageToken= nearbyResponse.getNextPageToken();
                    }
                    results = nearbyResponse.getResults();
                    adapter = new NearbyAdapter(results);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NearbyPlacesActivity.this));
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<NearbyResponse> call, Throwable t) {

            }
        });
    }

    public void moreResult(View view) {

        if (!pageToken.isEmpty()){
            service = Api_response.getRetrofit().create(Api_service.class);
            String url= String.format("place/nearbysearch/json?pagetoken=%s&key=%s",pageToken,getResources().getString(R.string.place_key));
            Call<NearbyResponse> nearbyResponseCall = service.getNextPageToken(url);
            nearbyResponseCall.enqueue(new Callback<NearbyResponse>() {
                @Override
                public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                    if(response.code()==200){

                        NearbyResponse nearbyResponse = response.body();

                        List<Result> newResult = nearbyResponse.getResults();
                        results.addAll(newResult);
                        adapter.NewList(results);

                    }
                }

                @Override
                public void onFailure(Call<NearbyResponse> call, Throwable t) {

                }
            });
        }
    }
}
