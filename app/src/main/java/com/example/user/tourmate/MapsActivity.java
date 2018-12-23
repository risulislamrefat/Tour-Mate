package com.example.user.tourmate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static int REQUEST_CODE_FOR_LOCATION = 1;
    private Geocoder geocoder;
    public String locationName;
    private  String locName;
    public  double latn;
    public  double longitude;
    LatLngBounds bounds;
    public double lat,lon;
    private TextView mTextMessage;
    private FirebaseAuth mAuth;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch ((menuItem.getItemId())) {
                case  R.id.homeId:
                    Toast.makeText(MapsActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    Intent intentProfile = new Intent(MapsActivity.this,ProfileActivity.class);
                    startActivity(intentProfile);

                    return  true;

                case R.id.nearbyId:
                    startActivity(new Intent(MapsActivity.this,NearbyPlacesActivity.class).putExtra("latitude",lat).putExtra("longitude",lon)
                            .putExtra("Address",locationName));
                    Toast.makeText(MapsActivity.this, "Nearby", Toast.LENGTH_SHORT).show();

                    return true;

                case R.id.eventId:
                    Toast.makeText(MapsActivity.this, "Events", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this,EventsActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.memorableId:
                    Toast.makeText(MapsActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intentSignOut = new Intent(MapsActivity.this,MainActivity.class);
                    startActivity(intentSignOut);
                    return true;

                case  R.id.weatherId:
                    Toast.makeText(MapsActivity.this, "Weather", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MapsActivity.this,WeatherActivity.class).putExtra("latitude",lat).putExtra("longitude",lon)
                            .putExtra("Address",locationName));
                    return true;
            }
            return false;
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAuth = FirebaseAuth.getInstance();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navBot);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        geocoder = new Geocoder(this);
        FusedLocationProviderClient client;
        LocationRequest request;
        LocationCallback callback;
        bounds = new LatLngBounds(new LatLng(23.720659, 90.468735), new LatLng(23.888483, 90.378766));
        client = LocationServices.getFusedLocationProviderClient(this);
        request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(30000).setFastestInterval(10000);

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location loc = new Location(locationResult.getLastLocation());

                lat = loc.getLatitude();
                lon = loc.getLongitude();
                for (Location location : locationResult.getLocations()) {
                    latn = location.getLatitude();
                    longitude = location.getLongitude();


                    try {
                        List <Address> addresses = geocoder.getFromLocation(latn, longitude, 1);
                        locationName = addresses.get(0).getFeatureName() + " , " + addresses.get(0).getLocality() + " ," + addresses.get(0).getPostalCode();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                LatLng sydney = new LatLng(lat, lon);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
                mMap.addMarker(new MarkerOptions().position(sydney).title(locationName));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_FOR_LOCATION);
            return;
        }
        client.requestLocationUpdates(request, callback, null);


        mMap = googleMap;

    }
}
