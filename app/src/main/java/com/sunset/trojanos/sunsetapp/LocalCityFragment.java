package com.sunset.trojanos.sunsetapp;


import android.Manifest;
import android.app.Fragment;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class LocalCityFragment extends Fragment {
    public static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private static final String TAG = "LOCAL DATA ";
    private static final int REQUEST_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public FusedLocationProviderClient mFusedLocationClient;
    public LatLng Lat_Lng = new LatLng(0.0, 0.0);
    Button searchLocalPosButton;
    TextView showLocalSunset, showLocalSunrise, showLocalCity, you_city_is;
    private RetrofitBuilder mRetrofitBilder;
    private LocationRequest mLocationRequest;

    public static LocalCityFragment newInstance() {
        return new LocalCityFragment();

    }

    protected void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();


        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.v(TAG, "Location settings satisfied");
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        Log.w(TAG, "Location settings not satisfied, attempting resolution intent");
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(getActivity(), REQUEST_CODE_LOCATION_SETTINGS);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            Log.e(TAG, "Unable to start resolution intent");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.w(TAG, "Location settings not satisfied and can't be changed");
                        break;
                }
            }
        });

        try {
            getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {


                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public void getLastLocation() {
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(getActivity());
        mFusedLocationClient = getFusedLocationProviderClient(getActivity());
        try {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {

                                onLocationChanged(location);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("LocalCityActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String city = list.get(0).getAddressLine(0);
            showLocalCity.setText(city);
        } catch (IOException io) {
            io.printStackTrace();
        }
        Lat_Lng = new LatLng(location.getLatitude(), location.getLongitude());

        getLocalInfo(Lat_Lng);
    }


    public void getLocalInfo(LatLng LatLng) {

        double lat = LatLng.latitude;
        double lng = LatLng.longitude;
        String date = null;


        mRetrofitBilder = new RetrofitBuilder();
        Call<Response2> listresult = mRetrofitBilder.getSunsetApi().getResult(lat, lng, date);
        listresult.enqueue(new Callback<Response2>() {
            @Override
            public void onResponse(@NonNull Call<Response2> call, @NonNull Response<Response2> response) {
                if (response.body() != null) {
                    Response2 result = response.body();
                    showLocalSunrise.setText("Sunrise: " + result.getResults().getSunrise());
                    showLocalSunset.setText("Sunset: " + result.getResults().getSunset());
                }

            }


            @Override
            public void onFailure(@NonNull Call<Response2> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error get data", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void configView(View v) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        searchLocalPosButton = v.findViewById(R.id.get_localcity_button);
        showLocalSunrise = v.findViewById(R.id.show_local_sunrise);
        showLocalCity = v.findViewById(R.id.show_local_city);
        showLocalSunset = v.findViewById(R.id.show_local_sunset);
        you_city_is = v.findViewById(R.id.you_city_is);
        you_city_is.setVisibility(View.INVISIBLE);
        searchLocalPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermissions();
                requestPermissions();
                startLocationUpdates();
                getLastLocation();
                you_city_is.setVisibility(View.VISIBLE);
                getLocalInfo(Lat_Lng);


            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.localcity, container, false);
        configView(v);
        return v;
    }
}
