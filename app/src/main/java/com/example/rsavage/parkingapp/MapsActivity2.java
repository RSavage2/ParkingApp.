package com.example.rsavage.parkingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.example.rsavage.parkingapp.R.id.map;

public class MapsActivity2 extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;

    private TextView mPlaceDetailsText;

    private TextView mPlaceAttribution;
    private Button bookBtn;
    final Context context = this;
    Location mLastLocation;


    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);


        //bookBtn = (Button) findViewById(R.id.button3);

// Settings for Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //  TextView toolbarTitle = (TextView) findViewById(R.id.main_toolbar_title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // getSupportActionBar().setHomeButtonEnabled(false);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 23
                );
            }
        }

        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }

        mGoogleApiClient.connect();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        /////////////////////
        //Place auto complete
        //////////////////////

        //     PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
        //              getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        //autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
        // @Override
        // public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        //  Log.i(TAG, "Place: " + place.getName());
        // }

        /// @Override
        // public void onError(Status status) {
        // TODO: Handle the error.
        // Log.i(TAG, "An error occurred: " + status);
        // }
        //});


    }

    // Sets and controls functions for items in the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        /*else if (id == R.id.search)
        {

            return true;
        }*/

        else if (id == R.id.navigation) {


            Log.i("Navigation", "clicked");
            getLastLocation();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void getLastLocation()
    {
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));





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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        // Add a marker in Atlanta and move the camera
        LatLng atlanta = new LatLng(33.7489954, -84.3879824);
        mMap.addMarker(new MarkerOptions().position(atlanta).title("Marker in Atlanta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atlanta, 15));


        LatLng MOREHOUSE = new LatLng(33.745784, -84.413711);
        Marker morehouse = mMap.addMarker(new MarkerOptions().position(MOREHOUSE)
                .title("Morehouse Parking").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .snippet("Spaces Availible: 37\n" + "1 - 20 minutes: free\n" + "\n" + "21 minutes to 1 hour: $1.00\n" +
                        "\n" + "1 - 2hours: $2.00\n" + "\n" + "2 hours – 24 hours: $3.00\n" + "\n" +
                        "After hours & weekend rates: $2.00\n" + "\n" + "Lost ticket: $3.00"));

        LatLng GASTATE = new LatLng(33.753778, -84.383984);
        Marker gastate = mMap.addMarker(new MarkerOptions().position(GASTATE)
                .title("Ga state M Deck").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("6:30am-10:00pm.  No overnight parking\n" + "$7.00 Monday - Friday\n" + "Free: Weekends"));

        LatLng GASTATE2 = new LatLng(33.753014, -84.384716);
        Marker gastate2 = mMap.addMarker(new MarkerOptions().position(GASTATE2)
                .title("Ga state K Deck").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("6:30am-10:00pm.  No overnight parking\n" + "$7.00 Monday - Friday\n" + "Free: Weekends"));

        LatLng GASTATE3 = new LatLng(33.752234, -84.386976);
        Marker gastate3 = mMap.addMarker(new MarkerOptions().position(GASTATE3)
                .title("Ga state G Deck").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Visitor Parking: $7.00\n" + "Monday- Friday 6:30 am-10:00 pm\n" + "Saturday 7:00 am – 9:30 pm (Collins St. Entrance only)\n" +
                        "Sunday 11:00 am – 9:30 pm (Collins St. Entrance only)"));

        LatLng GASTATE4 = new LatLng(33.757886, -84.382243);
        Marker gastate4 = mMap.addMarker(new MarkerOptions().position(GASTATE4)
                .title("Ga state N Deck").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("6:30am-10:00pm.  No overnight parking\n" + "$7.00 Monday - Friday\n" + "Free: Weekends"));

        LatLng GASTATE5 = new LatLng(33.753274, -84.384040);
        Marker gastate5 = mMap.addMarker(new MarkerOptions().position(GASTATE5)
                .title("Ga state S Deck").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("6:30am-10:00pm.  No overnight parking\n" + "$7.00 Monday - Friday\n" + "Free: Weekends"));

        LatLng GASTATE6 = new LatLng(33.755049, -84.386732);
        Marker gastate6 = mMap.addMarker(new MarkerOptions().position(GASTATE6)
                .title("Ga state T Deck").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Visitor:\n" +
                        "Monday – Friday: 6:30 am-10:00 pm (Regular Hours)\n" + "Saturday – Sunday, and Monday – Friday After Hours: Permit Access Only\n" +
                        "\n" + "Entrances/Exits:\n" + "Auburn Avenue: Monday – Friday (Regular Hours)\n" + "Equitable Place: Saturday – Sunday and Monday – Friday After hours: Permit Access Only\n" +
                        "\n" + "No overnight parking."));

        LatLng GTECH = new LatLng(33.753014, -84.384716);
        Marker gtech = mMap.addMarker(new MarkerOptions().position(GTECH)
                .title("GTech Visitors area 1").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet("0-1 hours: $1.50\n" + "1-2 hours: $3.00\n" + "2-3 hours: $4.50\n" + "3-4 hours: $6.00\n" +
                        "4-5 hours: $9.00\n" + "5-6 hours: $12.00\n" + "6-24 hours: $15.00\n" + "Lost Ticket Fee: $20.00"));

        LatLng GTECH2 = new LatLng(33.773295, -84.39862);
        Marker gtech2 = mMap.addMarker(new MarkerOptions().position(GTECH2)
                .title("GTech Visitors area 2").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet("$2/hr: 6 a.m.-5 p.m.\n" +
                        "$5 (flat rate): 5 p.m.-6 a.m."));

        LatLng GTECH3 = new LatLng(33.776841, -84.388802);
        Marker gtech3 = mMap.addMarker(new MarkerOptions().position(GTECH3)
                .title("GTech Visitors area 3").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet("0-1 hours: $1.50\n" + "\" + \"1-2 hours: $3.00\\n\" + \"2-3 hours: $4.50\\n\" + \"3-4 hours: $6.00\\n\" +\n" +
                        "\"4-5 hours: $9.00\\n\" + \"5-6 hours: $12.00\\n\" + \"6-24 hours: $15.00\\n\" + \"Lost Ticket Fee: $20.00"));

        LatLng GTECH4 = new LatLng(33.776466, -84.387469);
        Marker gtech4 = mMap.addMarker(new MarkerOptions().position(GTECH4)
                .title("GTech Visitors area 4").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet("0-1 hours: $1.50\n" +
                        "\" + \"\\\" + \\\"1-2 hours: $3.00\n" + "2-3 hours: $4.50\n\\\" + \\\"3-4 hours: $6.00\\\\n\\\" +\\n\" +\n" +
                        "\"\"4-5 hours: $9.00\\n\" + \"5-6 hours: $12.00\\n\" + \"6-24 hours: $15.00\\n\" + \"Lost Ticket Fee: $20.00"));

        LatLng ATLU = new LatLng(33.751356, -84.390832);
        Marker atlu = mMap.addMarker(new MarkerOptions().position(ATLU)
                .title("Underground Atlanta Deck B").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .snippet("Cost: $3.50"));

        LatLng ATL2 = new LatLng(33.751356, -84.390832);
        Marker atl2 = mMap.addMarker(new MarkerOptions().position(ATL2)
                .title("45 Decatur Street").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .snippet("Cost: $7.00"));

        LatLng ATL3 = new LatLng(33.762719, -84.385836);
        Marker atl3 = mMap.addMarker(new MarkerOptions().position(ATL3)
                .title("SunTrust Plaza Garage").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .snippet("Cost: $20.00"));



    }


    /////////////////////////////////////
    //sets up map if needed
    ////////////////////////////////////

    @Override
    protected void onResume() {

        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map);
            mapFragment.getMapAsync(this);
        }
        if (mMap != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        LatLng atlanta = new LatLng(33.7489954, -84.3879824);
        mMap.addMarker(new MarkerOptions().position(atlanta).title("Marker in Atlanta").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(atlanta));


    }

    ///////////////////////////////
//Location serveices
/////////////////////////
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        // Check Permissions!!
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 23
                );
            }
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            //handleNewLocation(location);
        }
        ;

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }


    @Override
    public void onLocationChanged(Location location) {
        //handleNewLocation(location);
    }


    //public void onSearch(View view) {
        //EditText location_tf = (EditText) findViewById(R.id.searchView);

   // }


    //////////////////////
    //location geocoder
    ////////////////////////
    public void geoLocate(View view) throws IOException {
        EditText et = (EditText) findViewById(R.id.place_autocomplete_search_input);
        String location = et.getText().toString();
        List<Address> addressList = null;

        Geocoder gc = new Geocoder(this);
        try {
            addressList = gc.getFromLocationName(location, 1);
            // Address address = addressList.get(0);
            //String locality = address.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlng).title("Searched Location").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapsActivity2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.rsavage.parkingapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapsActivity2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.rsavage.parkingapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }

    public void buttonOnClick(View v){

        Intent intent = new Intent(this, CreditInfoActivity.class);
        startActivity(intent);
    }

    public void buttonOnClick2(View v){

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                LatLng latlng = place.getLatLng();
                mMap.addMarker(new MarkerOptions().position(latlng).title(place.getName().toString()).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));



            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }




}