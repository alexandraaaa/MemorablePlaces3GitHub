package com.example.alexandra.memorableplaces3;

import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    int location = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        Intent intent = getIntent();

        location = intent.getIntExtra("locationInfo", -1);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);

        if(location != -1 && location != 0) {

            mMap.moveCamera(CameraUpdateFactory.newLatLng(MainActivity.locations.get(location)));
            mMap.addMarker(new MarkerOptions().position(MainActivity.locations.get(location)).title(MainActivity.places.get(location)));

            mMap.setOnMapLongClickListener(this);
        }
        else {

            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            mMap.setOnMapLongClickListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                //this.finish();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapLongClick(LatLng point) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String label = new Date().toString();

        try {
            List<Address> listAdddress = geocoder.getFromLocation(point.latitude, point.longitude, 1);

            if(listAdddress != null && listAdddress.size() > 0)
                label = listAdddress.get(0).getAddressLine(0);


        } catch (IOException e) {

            e.printStackTrace();
        }

        MainActivity.places.add(label);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        MainActivity.locations.add(point);

        Log.i("placesTest", MainActivity.places.toString());

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(label)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    public void onLocationChanged(Location userLocation) {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
