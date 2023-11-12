package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Data_display extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private ImageView headerImage;
    private NestedScrollView nestedScrollView;
    private GoogleMap mMap;
    private int initialImageHeight;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        ImageView imageCategoryBanner = findViewById(R.id.imageViewCategoryBanner);
        ImageView imageCategoryIcon = findViewById(R.id.imageViewCategoryIcon);
        ImageButton goToMapsApp = findViewById(R.id.goToMapsApp);

        ImageButton goToMapsActivity = (ImageButton) findViewById(R.id.goToMapsActivity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        goToMapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: See if it's better to use stack or finish activity
                finish();
            }
        });

        goToMapsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + longitude + "," + latitude + "?q=" + longitude + "," + latitude);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Check if Google Maps is installed
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        String tid = JSONFileReader.dataToSearch(this, 0, "attributes", "tid");
        String binCategory = JSONFileReader.dataToSearch(this, 0, "attributes", "komodita_odpad_separovany");


        switch (binCategory) {
            case "Plasty, nápojové kartony a hliníkové plechovky od nápojů":
                imageCategoryIcon.setImageResource(R.drawable.plastic);
                binCategory = "Plastic";
                break;
            case "Papír":
                imageCategoryIcon.setImageResource(R.drawable.paper);
                binCategory = "Paper";
                break;
            case "Biologický odpad":
                imageCategoryIcon.setImageResource(R.drawable.organic);
                binCategory = "Organic";
                break;
            case "Sklo barevné":
                imageCategoryIcon.setImageResource(R.drawable.coloredglass);
                binCategory = "Colored glasses";
                break;
            case "Sklo bílé":
                imageCategoryIcon.setImageResource(R.drawable.whiteglass);
                binCategory = "White glasses";
                break;
            default:
                binCategory = "Nothing";
        }

        // Take datas send by InfoFragment from MapsActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String addressFromActivity = extras.getString("address");
            String tagFromActivity = extras.getString("tid");
            String latitudeString = extras.getString("latitude");
            String longitudeString = extras.getString("longitude");

            latitude = Double.parseDouble(latitudeString);
            longitude = Double.parseDouble(longitudeString);

            TextView textViewLocation = findViewById(R.id.textView02);
            textViewLocation.setText(addressFromActivity);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng markerPosition = new LatLng(latitude, longitude); // Latitude and longitude of collect point
        Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 19);
        mMap.animateCamera(cameraUpdate);
        Log.d("mappp", "" + latitude + "  " + longitude);
    }

}


