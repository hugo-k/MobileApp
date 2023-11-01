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
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private InfoFragment infoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        infoFragment = new InfoFragment();
        mapFragment.getMapAsync(this);

        ImageButton requestPermissionLocation = findViewById(R.id.requestPermissionLocation);
        requestPermissionLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermission();
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
        Address address = getAddress(markerPosition.latitude, markerPosition.longitude);
        String addressText = getAddressText(address);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 20);
        mMap.animateCamera(cameraUpdate);

        if (infoFragment != null) {
            infoFragment.updatePostalAddress(addressText);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, infoFragment)
                .addToBackStack(null)
                .commit();

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
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate locationUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 17); // Ajustez la valeur du zoom (15 est un exemple)
                        mMap.animateCamera(locationUpdate);
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
        // Demander la permission
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
        // Parse JSON file on a String
        String parsedJson = JSONFileReader.readJSONFromRawResource(this, R.raw.dataset_test);
        // Look if the String is not empty
        try {
            JSONObject jsonObject = new JSONObject(parsedJson);

            // Create new JSON object by parsing the precedent
            JSONArray features = jsonObject.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                String x = geometry.getString("x");
                String y = geometry.getString("y");

                LatLng markerPosition = new LatLng(Double.parseDouble(y), Double.parseDouble(x)); // Latitude and longitude
                Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition));
                marker.showInfoWindow();
            }
        } catch (JSONException e) {
            Log.d("tag", "Error: " + e);
            throw new RuntimeException(e);
        }
    }

    private Address getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        Log.d("GetAddress : ", "Try");
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                Log.d("GetAddress : ", "" + address);
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

}
