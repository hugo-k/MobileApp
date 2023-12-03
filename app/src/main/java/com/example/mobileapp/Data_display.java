package com.example.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.List;
import java.lang.String;
import java.util.Objects;

public class Data_display extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int imageBannerResourceId;
    private double latitude, longitude;
    private WasteContainer wasteContainer;
    private List<String> collectDays;

    private boolean extandImage = false;

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
    public void displayDaysOfCollect(List<String> days){

        try {
            for (String i : days) {

                if (i == null) {
                    break;
                }
                String textViewId = i;
                int resId = getResources().getIdentifier(textViewId, "id", getPackageName());

                TextView dayTxtView = findViewById(resId);
                dayTxtView.setBackground(getResources().getDrawable(R.drawable.rounded_txtbackground));
            }
        }catch (Exception e) {
            // Capturer l'exception et l'afficher dans les logs avec un tag
            Log.e("day", "Erreur : ", e);
        }
    }
}


