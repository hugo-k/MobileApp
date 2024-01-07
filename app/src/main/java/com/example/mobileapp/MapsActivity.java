package com.example.mobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        ClusterManager.OnClusterClickListener<WasteContainer>,
        ClusterManager.OnClusterInfoWindowClickListener<WasteContainer>,
        ClusterManager.OnClusterItemClickListener<WasteContainer>,
        ClusterManager.OnClusterItemInfoWindowClickListener<WasteContainer>,
        FilterListener,
        FilterFragment.OnFilterInteractionListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private InfoFragment infoFragment;
    private FilterFragment filterFragment;
    private List<InfoFragment> infoFragments = new ArrayList<>();
    private List<String> markerAddresses = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private ClusterManager<WasteContainer> mClusterManager;
    List<WasteContainer> wasteContainers;
    ImageButton btnFilter;

    private View backgroundOverlay;

    // onCreate method executed when the app is open and only one time: used to create instances
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        infoFragment = new InfoFragment();
        mapFragment.getMapAsync(this);
        filterFragment = new FilterFragment();

        wasteContainers = JSONFileReader.createWasteContainersFromJson(this);

        backgroundOverlay = findViewById(R.id.backgroundOverlay);
        btnFilter = findViewById(R.id.btnFilter);
        ImageButton btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        ImageButton btnZoomIn = findViewById(R.id.btnZoomIn);
        ImageButton btnZoomOut = findViewById(R.id.btnZoomOut);

        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermission();
            }
        });
        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate zoomIn = CameraUpdateFactory.zoomIn();
                mMap.animateCamera(zoomIn);
            }
        });
        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate zoomOut = CameraUpdateFactory.zoomOut();
                mMap.animateCamera(zoomOut);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragmentTransaction transactionRemoveInfoFragment = getSupportFragmentManager().beginTransaction();

                FragmentManager fragmentManager = getSupportFragmentManager();
                InfoFragment infoFragment = (InfoFragment) fragmentManager.findFragmentById(R.id.fragmentContainer);

                if (infoFragment != null) {
                    transactionRemoveInfoFragment.remove(infoFragment);
                    transactionRemoveInfoFragment.commit();
                }

                backgroundOverlay.setVisibility(View.VISIBLE);
                backgroundOverlay.animate().alpha(1.0f).setDuration(300).start();
                btnFilter.setVisibility(View.GONE);

                transaction.replace(R.id.fragmentFilterContainer, filterFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        backgroundOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOverlayFilterFragment();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate locationUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 19);
                        mMap.moveCamera(locationUpdate);
                    } else {
                        Toast.makeText(MapsActivity.this, "Cannot find location", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            requestLocationPermission();
        }

        // Cluster: Group of markers, avoid displaying 1000 markers
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);

        CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        displayMarkers();

        updateVisibleMarkers(mMap.getProjection().getVisibleRegion().latLngBounds);
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
                        CameraUpdate locationUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 17);
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
        // Ask for localization permission
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

    private void displayMarkers() {
        mClusterManager.clearItems();

        for (WasteContainer wasteContainer : wasteContainers) {
            LatLng markerPosition = new LatLng(wasteContainer.getYloc(), wasteContainer.getXloc());
            WasteContainer offsetItem = new WasteContainer(
                    markerPosition,
                    wasteContainer.getLocId(),
                    wasteContainer.getName(),
                    wasteContainer.getWasteCategories(),
                    wasteContainer.getLocationType(),
                    wasteContainer.getOwner(),
                    wasteContainer.getWasteCollectionFrequency(),
                    wasteContainer.getWasteCollectionDays(),
                    wasteContainer.getXloc(),
                    wasteContainer.getYloc()
            );
            // Call setImageResource to set the correct image resource for the waste category
            offsetItem.setImageResource(wasteContainer.getWasteCategories().get(0));
            mClusterManager.addItem(offsetItem);
        }
        mClusterManager.cluster();
    }

    private Address getAddress(double longitude, double latitude) {
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
            String addressText = addressLine;
            return addressText;
        }
        return "null";
    }

    // Method to zoom on the map, with Lat, Long and zoom value
    private void zoomOnMap(double longitude, double latitude, int zoom) {
        LatLng currentLocation = new LatLng(latitude, longitude);
        CameraUpdate locationUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, zoom);
        mMap.animateCamera(locationUpdate);
    }

    private void updateMarkersList(List<Marker> visibleMarkers) {
        markers.clear();
        markers.addAll(visibleMarkers);
    }

    private List<Marker> getVisibleMarkers(LatLngBounds bounds, float zoomLevel) {
        List<Marker> visibleMarkers = new ArrayList<>();

        int maxPointsToShow = calculateMaxPointsToShow(zoomLevel);
        int pointsShown = 0;

        for (Marker marker : markers) {
            if (bounds.contains(marker.getPosition())) {
                visibleMarkers.add(marker);
                pointsShown++;

                if (maxPointsToShow >= 0 && pointsShown >= maxPointsToShow) {
                    break;
                }
            }
        }
        return visibleMarkers;
    }

    private int calculateMaxPointsToShow(float zoomLevel) {
        if (zoomLevel > 15) {
            return -1;
        } else if (zoomLevel >= 10) {
            return 50;
        } else if (zoomLevel >= 8) {
            return 30;
        } else {
            return 10;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<WasteContainer> cluster) {
        List<WasteContainer> clusterItems = new ArrayList<>(cluster.getItems());
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (WasteContainer clusterItem : clusterItems) {
            builder.include(clusterItem.getPosition());
        }
        LatLngBounds clusterBounds = builder.build();

        LatLng center = clusterBounds.getCenter();

        float zoomLevel = mMap.getCameraPosition().zoom + 1;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<WasteContainer> cluster) {
    }

    @Override
    public boolean onClusterItemClick(WasteContainer wasteContainer) {
        double latitude = wasteContainer.getXloc();
        double longitude = wasteContainer.getYloc();

        wasteContainer.setXloc(latitude);
        wasteContainer.setYloc(longitude);
        wasteContainer.setAddressText(getAddressText(getAddress(latitude, longitude)));
        wasteContainer.setBinTypeName(simplifyWasteTypeName(wasteContainer));

        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("wasteContainer", wasteContainer);
        infoFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, infoFragment)
                .addToBackStack(null)
                .commit();

        zoomOnMap(wasteContainer.getXloc(), wasteContainer.getYloc(), 18);
        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(WasteContainer wasteContainer) {

    }

    @Override
    public void onCameraIdle() {
        LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        updateVisibleMarkers(visibleBounds);
    }

    private void updateVisibleMarkers(LatLngBounds bounds) {
        List<Marker> visibleMarkers = getVisibleMarkers(bounds, mMap.getCameraPosition().zoom);
        updateMarkersList(visibleMarkers);
    }

    private String simplifyWasteTypeName(WasteContainer wasteContainer) {
        List<String> wasteCategories = wasteContainer.getWasteCategories();
        if (wasteCategories.contains("WASTE_GLASS")) {
            return "GLASS";
        } else if (wasteCategories.contains("WASTE_PAPER")) {
            return "PAPER";
        } else if (wasteCategories.contains("WASTE_PLASTIC") || wasteCategories.contains("WASTE_METAL_FOOD_PACKAGING")) {
            return "PLASTIC, METAL";
        } else if (wasteCategories.contains("WASTE_ELECTRONICS")) {
            return "ELECTRONIC WASTES";
        } else if (wasteCategories.contains("WASTE_TEXTILE")) {
            return "TEXTILES, ACCESSORIES";
        } else if (wasteCategories.contains("WASTE_BIOLOGICAL")) {
            return "BIOLOGICAL, ORGANIC";
        } else {
            return "Other wastes";
        }
    }

    @Override
    public void onFiltersApplied(List<String> selectedFilters) {
        filterWasteContainers(selectedFilters);
    }

    @Override
    public void onFilterOverlayClosed() {
        hideOverlayFilterFragment();
    }

    private void filterWasteContainers(List<String> selectedFilters) {
        List<WasteContainer> filteredWasteContainers = new ArrayList<>();
        for (WasteContainer container : wasteContainers) {
            for (String filter : selectedFilters) {
                if (container.getWasteCategories().contains(filter)) {
                    filteredWasteContainers.add(container);
                    break;
                }
            }
        }
        updateMapWithFilteredContainers(filteredWasteContainers);
    }

    private void updateMapWithFilteredContainers(List<WasteContainer> filteredWasteContainers) {
        mClusterManager.clearItems();

        for (WasteContainer wasteContainer : filteredWasteContainers) {
            LatLng markerPosition = new LatLng(wasteContainer.getYloc(), wasteContainer.getXloc());
            WasteContainer offsetItem = new WasteContainer(
                    markerPosition,
                    wasteContainer.getLocId(),
                    wasteContainer.getName(),
                    wasteContainer.getWasteCategories(),
                    wasteContainer.getLocationType(),
                    wasteContainer.getOwner(),
                    wasteContainer.getWasteCollectionFrequency(),
                    wasteContainer.getWasteCollectionDays(),
                    wasteContainer.getXloc(),
                    wasteContainer.getYloc()
            );
            offsetItem.setImageResource(wasteContainer.getWasteCategories().get(0));
            mClusterManager.addItem(offsetItem);
        }
        mClusterManager.cluster();
    }

    private void hideOverlayFilterFragment() {
        getSupportFragmentManager().popBackStack();
        backgroundOverlay.animate().alpha(0.0f).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                backgroundOverlay.setVisibility(View.GONE);
            }
        }).start();

        btnFilter.setVisibility(View.VISIBLE);
    }
}


