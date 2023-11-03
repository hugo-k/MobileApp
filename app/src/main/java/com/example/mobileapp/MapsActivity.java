package com.example.mobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private InfoFragment infoFragment;
    private ViewPager viewPager;
    private InfoFragmentPagerAdapter pagerAdapter;
    private List<InfoFragment> infoFragments;
    private List<String> markerAddresses;
    private List<Marker> markers = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        infoFragment = new InfoFragment();
        markerAddresses = new ArrayList<>();
        infoFragments = new ArrayList<>();
        pagerAdapter = new InfoFragmentPagerAdapter(getSupportFragmentManager(), infoFragments);

        mapFragment.getMapAsync(this);

        viewPager = findViewById(R.id.fragmentContainer);

        ImageButton requestPermissionLocation = findViewById(R.id.requestPermissionLocation);
        requestPermissionLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermission();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Marker marker = markers.get(position);
                marker.showInfoWindow();
                zoomToMarker(marker.getPosition().latitude, marker.getPosition().longitude, 17);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        displayMarkers();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng markerPosition = marker.getPosition();
        int markerIndex = markers.indexOf(marker);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 15);
        mMap.animateCamera(cameraUpdate);

        if (markerIndex != -1) {
            viewPager.setCurrentItem(markerIndex);
        }

        return false;
    }

    private void getMyLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        zoomToMarker(location.getLatitude(), location.getLongitude(), 17);
                    } else {
                        Toast.makeText(MapsActivity.this, "Impossible to find location", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Location permission is required for this app.", Toast.LENGTH_LONG).show();
        }
        // Ask for localisation permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getMyLocation();
            } else {
                Toast.makeText(this, "Location permission denied. You can enable it in the app settings.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayMarkers(){

        String featuresLength = JSONFileReader.dataToSearch(this, -1, null, null);

        for (int i = 0; i < Integer.parseInt(featuresLength); i++) {
            String x = JSONFileReader.dataToSearch(this, i, "geometry", "x");
            String y = JSONFileReader.dataToSearch(this, i, "geometry", "y");


            LatLng markerPosition = new LatLng(Double.parseDouble(y), Double.parseDouble(x)); // Latitude and longitude
            Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition));

            createCollectPointsList(getAddressText(getAddress(markerPosition.latitude, markerPosition.longitude)), marker);

            // Search and give TID identification as TAG to the marker
            String tid = JSONFileReader.dataToSearch(this, i, "attributes", "tid");
            marker.setTag(Integer.parseInt(tid));

            marker.showInfoWindow();
        }
    }

    private Address getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address;

            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAddressText(Address address) {
        if (address != null) {
            String addressLine = address.getAddressLine(0);
            String city = address.getLocality();
            String state = address.getAdminArea();
            String country = address.getCountryName();
            String postalCode = address.getPostalCode();
            String knownName = address.getFeatureName();

            String addressText = addressLine;
            return addressText;
        }
        return "null";
    }

    private void createCollectPointsList(String addressText, Marker marker) {
        InfoFragment newInfoFragment = InfoFragment.newInstance(addressText);
        infoFragments.add(newInfoFragment);

        markers.add(marker);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(infoFragments.size() - 1);
    }

    private void zoomToMarker(double latitude, double longitude, int zoom){
        LatLng currentLocation = new LatLng(latitude, longitude);
        CameraUpdate locationUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, zoom);
        mMap.animateCamera(locationUpdate);
    }
}
