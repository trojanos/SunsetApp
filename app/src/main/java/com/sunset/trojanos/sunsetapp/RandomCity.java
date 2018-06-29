package com.sunset.trojanos.sunsetapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RandomCity extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    protected GoogleApiClient mGoogleApiClient;
    ImageView delete;
    private EditText mAutocompleteView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RetrofitBuilder mRetrofitBuilder;

    public static RandomCity newInstance() {
        return new RandomCity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.randoncity, container, false);
        configView(v);
        return v;
    }

    private void configView(View v) {
        buildGoogleApiClient();

        mAutocompleteView = v.findViewById(R.id.autocomplete_places);

        delete = v.findViewById(R.id.cross);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.searchview_adapter,
                mGoogleApiClient, LAT_LNG_BOUNDS, null);

        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        delete.setOnClickListener(this);
        mAutocompleteView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    Toast.makeText(getActivity(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e(Constants.PlacesTag, Constants.API_NOT_CONNECTED);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        mRetrofitBuilder = new RetrofitBuilder();

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                if (places.getCount() == 1) {

                                    double lat = places.get(0).getLatLng().latitude;
                                    double lng = places.get(0).getLatLng().longitude;
                                    String date = null;
                                    Call<Response2> listresult = mRetrofitBuilder.getSunsetApi().getResult(lat, lng, date);
                                    listresult.enqueue(new Callback<Response2>() {
                                        @Override
                                        public void onResponse(Call<Response2> call, @NonNull Response<Response2> response) {
                                            if (response.body() != null) {
                                                Response2 result = response.body();
                                                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
                                                alertdialog.setTitle("Result:")
                                                        .setMessage("Sunset: " + result.getResults().getSunset() + "\n" + "Sunrise: " + result.getResults().getSunrise())

                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.cancel();
                                                            }
                                                        });
                                                AlertDialog alert = alertdialog.create();
                                                alert.show();
                                            }
                                        }


                                        @Override
                                        public void onFailure(@NonNull Call<Response2> call, @NonNull Throwable t) {
                                            Toast.makeText(getActivity(), "Error get data", Toast.LENGTH_LONG).show();
                                        }
                                    });


                                } else {
                                    Toast.makeText(getActivity(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onClick(View view) {
        if (view == delete) {
            mAutocompleteView.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
