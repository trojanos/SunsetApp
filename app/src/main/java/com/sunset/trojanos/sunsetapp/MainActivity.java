package com.sunset.trojanos.sunsetapp;

import android.app.FragmentTransaction;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private BottomNavigationView navigation;

    private void setBotNav() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        setLocalFragment();
                        navigation.setSelected(true);
                        return true;
                    case R.id.navigation_any_city:
                }
                setRandomFragment();
                navigation.setSelected(true);
                return false;
            }
        });

    }


    private void setLocalFragment() {
        LocalCityFragment fragment = LocalCityFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragments, fragment);
        ft.commit();
    }

    private void setRandomFragment() {
        RandomCity fragment = RandomCity.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragments, fragment);
        ft.commit();
    }

    public final boolean isInternetOn() {


        ConnectivityManager connect =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


        if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, " No internet connection ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBotNav();
        if (savedInstanceState == null) {
            setLocalFragment();
        }
        isInternetOn();

    }

}
