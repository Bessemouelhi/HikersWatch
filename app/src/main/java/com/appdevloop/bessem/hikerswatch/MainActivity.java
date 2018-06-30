package com.appdevloop.bessem.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    static final Integer LOCATION = 0x1;

    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    LocationManager locationManager;
    String provider;

    TextView tv_alt;
    TextView tv_lat;
    TextView tv_lng;
    TextView tv_speed;
    TextView tv_accuracy;
    TextView tv_bearing;
    TextView tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://source.unsplash.com/category/nature/1920x1080

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        tv_alt = (TextView) findViewById(R.id.tv_altitude);
        tv_lat = (TextView) findViewById(R.id.tv_latitude);
        tv_lng = (TextView) findViewById(R.id.tv_longitude);
        tv_speed = (TextView) findViewById(R.id.tv_speed);
        tv_accuracy = (TextView) findViewById(R.id.tv_accuracy);
        tv_address = (TextView) findViewById(R.id.tv_address);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        //Location location = locationManager.getLastKnownLocation(provider);
        //onLocationChanged(location);
    }

    public static boolean isConnecting(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                //return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }

        //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        onRestart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        provider = locationManager.getBestProvider(new Criteria(), false);
        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        Double alt = location.getAltitude();
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        float acc = location.getAccuracy();
        float speed = location.getSpeed();
        float bearing = location.getBearing();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);

            if (addressList != null && addressList.size() > 0) {
                Log.i("Address info", addressList.get(0).toString());

                String address = "";

                for (int i = 0; i < addressList.get(0).getMaxAddressLineIndex(); i++) {
                    address += addressList.get(0).getAddressLine(i) + " ";
                }

                if (isConnecting(this)) {
                    tv_address.setText(getString(R.string.address) + address);
                } else {
                    tv_address.setText(getString(R.string.address) + "Not connected");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tv_alt.setText(getString(R.string.altitude) + String.format("%.0f", alt) + "m");
        tv_lat.setText(getString(R.string.lat) + String.format("%.6f", lat));
        tv_lng.setText(getString(R.string.lng) + String.format("%.6f", lng));
        tv_speed.setText(getString(R.string.speed) + String.format("%.1f", speed) + "m/s");
        tv_accuracy.setText(getString(R.string.acc) + acc + "m");

        Log.i("Altitude", alt + "");
        Log.i("Latitude", lat + "");
        Log.i("Longitude", lng + "");
        Log.i("Accuracy", acc + "");
        Log.i("Speed", speed + "");
        Log.i("Bearing", bearing + "");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
