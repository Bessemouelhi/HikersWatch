package com.appdevloop.bessem.hikerswatch;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {

    static final Integer LOCATION = 0x1;
    public static final String TAG = "MainActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    LocationManager locationManager;
    LocationListener locationListener;
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

        this.configureToolbar();

        //https://source.unsplash.com/category/nature/1920x1080

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        tv_alt = findViewById(R.id.tv_altitude);
        tv_lat = findViewById(R.id.tv_latitude);
        tv_lng = findViewById(R.id.tv_longitude);
        tv_speed = findViewById(R.id.tv_speed);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_address = findViewById(R.id.tv_address);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged: " + location.toString());
                updateLocationInfo(location);
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
        };

        updateRequest();
    }

    private void updateRequest() {
        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateRequest();

                /*// define the animation for rotation
                Animation animation = new RotateAnimation(0.0f, 360.0f,
                                                          Animation.RELATIVE_TO_SELF, 0.5f,
                                                          Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1000);
                //animRotate = AnimationUtils.loadAnimation(this, R.anim.rotation);

                animation.setRepeatCount(3);

                ImageView imageView = new ImageView(this);
                imageView.setImageDrawable(item.getIcon());

                imageView.startAnimation(animation);
                item.setActionView(imageView);*/

                Toast.makeText(this, "On Update...", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }

    }

    public void startListening() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

    }

    public void updateLocationInfo(Location location) {

        Log.i("LocationInfo", location.toString());

        tv_lat.setText("Latitude: " + String.format("%.5f", location.getLatitude()));
        tv_lng.setText("Longitude: " + String.format("%.5f", location.getLongitude()));
        tv_alt.setText("Altitude: " + String.format("%.0f", location.getAltitude()) + "m");
        tv_accuracy.setText("Accuracy: " + location.getAccuracy());
        tv_speed.setText("Speed: " + String.format("%.1f", location.getSpeed()) + "m/s");
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses != null && listAddresses.size() > 0 ) {
                Log.i("PlaceInfo", listAddresses.get(0).toString());
                address = "Address: ";

                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }

                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }

                if (listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + " ";
                }

                if (listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + " ";
                }

                if (listAddresses.get(0).getCountryName() != null) {
                    address += listAddresses.get(0).getCountryName() + "\n";
                }
            }
            TextView addressTextView = findViewById(R.id.tv_address);
            addressTextView.setText(address);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void configureToolbar(){
        //Get the toolbar (Serialise)
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the toolbar
        setSupportActionBar(toolbar);

        toolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View item = toolbar.findViewById(R.id.action_refresh);
                if (item != null) {
                    toolbar.removeOnLayoutChangeListener(this);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ObjectAnimator animator = ObjectAnimator
                                    .ofFloat(v, "rotation", v.getRotation() + 1080);
                            animator.setDuration(3000).start();
                        }
                    });
                }
            }
        });
    }

    /*@Override
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

        Log.i(TAG, "Altitude " + alt + "");
        Log.i(TAG, "Latitude " + lat + "");
        Log.i(TAG, "Longitude " + lng + "");
        Log.i(TAG, "Accuracy " + acc + "");
        Log.i(TAG, "Speed " + speed + "");
        Log.i(TAG, "Bearing " + bearing + "");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/
}
