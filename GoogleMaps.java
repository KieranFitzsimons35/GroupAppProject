package com.example.kieranfitzsimons.groupproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by kieranfitzsimons on 24/03/2017.
 */

public class GoogleMaps extends AppCompatActivity implements OnMapReadyCallback , OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    //Construct Google Map
    private GoogleMap googleMap;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private int MY_LOCATION_REQUEST_CODE;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        try {
            //Loading Map
            initializeMap();
        } catch (Exception e) {//return errors
            e.printStackTrace();
        }

    }

    public void initializeMap() {
        if (googleMap == null) {
            MapFragment googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            googleMap.getMapAsync(this);
        }

        //check if map created successfully or not
        // if(googleMap==null){
        // Toast.makeText(getApplicationContext(), "Sorry! unable to create maps",Toast.LENGTH_SHORT).show();
        // }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();

        //if myLocation is not available the map will go to the default location set below

        //set the Latitude and Longitude and refer to it as belfast in this case.
        LatLng belfast = new LatLng(54.581827, -5.9396539);
        //LatLng downpatrick = new LatLng(54.32774152, -5.71613522);
        //LatLng carryduff = new LatLng(54.529400, -5.869875);
        //LatLng saintfield = new LatLng(54.475075, -5.818377);
        //LatLng crossgar = new LatLng(54.393945, -5.7661484);

        //adding a marker at the belfast latlng with a title, snippet and customizing colour to blue
        googleMap.addMarker(new MarkerOptions().position(belfast)
                .title("Queen's University, Belfast")
                 .snippet("I am usually sitting around here!!!")
               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //googleMap.addMarker(new MarkerOptions().position(downpatrick).title("Downpatrick").snippet("Start here")
        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //googleMap.addMarker(new MarkerOptions().position(carryduff).title("Carryduff").snippet("Nearly there")
        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //googleMap.addMarker(new MarkerOptions().position(saintfield).title("Saintfield").snippet("Halfway there")
        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        //googleMap.addMarker(new MarkerOptions().position(crossgar).title("Crossgar").snippet("Long way to go")
        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));


        //You can set the zoom, in this case to 15 as well as LatLong using the static CameraUpdate
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(belfast, 15);
        map.moveCamera(cameraPosition);


        // set the zoom controls so they are enabled and visible
        googleMap.getUiSettings().setZoomControlsEnabled(true);


        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.getUiSettings().setCompassEnabled(true);

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        initializeMap();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }
    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


}
