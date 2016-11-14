package com.example.rsavage.parkingapp;

import android.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements //OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private Location mLastLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                //.findFragmentById(R.id.map);
       //mapFragment.getMapAsync(this);

        //mLatitudeText=  (TextView) findViewById(R.id.latitudeTextView);
        //mLongitudeText = (TextView) findViewById(R.id.longitudeTextView);


        // Create an instance of GoogleAPIClient.

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
   // @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



       /////// // Add a marker in  current location (Sydney) and move the camera
        LatLng currentLocation = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude() );           //LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));                 // Marker in sydney
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

    }

    /////////////////////////////////////////////
    //Location code
    ////////////////////
        @Override
        public void onConnected(Bundle connectionHint) {

            // Check Permissions!!
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                } else {


                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},23
                    );
                }
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
            if (mLastLocation != null) {
             mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
             mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            }
    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int arg0) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
