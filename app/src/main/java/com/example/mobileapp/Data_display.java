package com.example.mobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Data_display extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int imageBannerResourceId;
    private double latitude, longitude;
    private WasteContainer wasteContainer;
    private List<String> collectDays;

    private boolean extendImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        Intent intent = getIntent();
        wasteContainer = (WasteContainer) intent.getSerializableExtra("wasteContainer");

        ImageView imageCategoryBanner = findViewById(R.id.imageViewCategoryBanner);
        ImageView imageCategoryIcon = findViewById(R.id.imageViewCategoryIcon);
        ImageButton goToMapsApp = findViewById(R.id.goToMapsApp);
        TextView txtWasteTypeName = findViewById(R.id.txtWasteTypeName);

        /* CARD VIEW */
        txtWasteTypeName.setText(wasteContainer.getBinTypeName());
        imageCategoryIcon.setImageResource(wasteContainer.getImageIconResourceId());

        imageBannerResourceId = wasteContainer.getImageBannerResourceId();
        imageCategoryBanner.setImageResource(imageBannerResourceId);

        /* STATIC MAP */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);
        ImageButton goToMapsActivity = (ImageButton) findViewById(R.id.goToMapsActivity);

        latitude = wasteContainer.getXloc();
        longitude = wasteContainer.getYloc();

        /* Days of collect */
        collectDays = wasteContainer.getWasteCollectionDays();
        displayDaysOfCollect(collectDays);

        /* WASTE LIST DESCRIPTION */
        ImageView imageWasteListe = findViewById(R.id.imageViewWastList);
        imageWasteListe.setImageResource(wasteContainer.getWasteList());

        goToMapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                } else {
                    // Google Maps is not available, show an error message
                    Toast.makeText(Data_display.this, "Google Maps is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textViewLocation = findViewById(R.id.textView02);
        textViewLocation.setText(wasteContainer.getAddressText());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng markerPosition = new LatLng(longitude, latitude); // Latitude and longitude of collect point
        Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 15);
        mMap.animateCamera(cameraUpdate);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void displayDaysOfCollect(List<String> days) {
        try {
            TextView noCollectDaysText = findViewById(R.id.textViewInfoCollectionDays);
            if (days == null || days.isEmpty()) {
                // No collection day mentioned
                noCollectDaysText.setText("Unknown collection days");
            } else {
                noCollectDaysText.setText("Garbage collector will empty this container \n each green day");
                for (String i : days) {
                    if (i == null) {
                        break;
                    }
                    String textViewId = i;
                    int resId = getResources().getIdentifier(textViewId, "id", getPackageName());

                    TextView dayTxtView = findViewById(resId);
                    dayTxtView.setBackground(getResources().getDrawable(R.drawable.rounded_txtbackground));
                }
            }
        } catch (Exception e) {
            Log.e("day", "Error: ", e);
        }
    }
}
