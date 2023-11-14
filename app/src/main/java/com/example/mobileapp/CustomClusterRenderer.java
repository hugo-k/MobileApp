package com.example.mobileapp;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.List;

public class CustomClusterRenderer extends DefaultClusterRenderer<WasteContainer> {
    private final Context context;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<WasteContainer> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(WasteContainer item, MarkerOptions markerOptions) {
        float markerColor = determineMarkerColor(item);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.defaultMarker(markerColor);
        markerOptions.icon(markerIcon);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    private float determineMarkerColor(WasteContainer wasteContainer) {
        List<String> wasteCategories = wasteContainer.getWasteCategories();
        if (wasteCategories.contains("WASTE_WHITE_GLASS") || wasteCategories.contains("WASTE_COLORED_GLASS")) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (wasteCategories.contains("WASTE_PAPER")) {
            return BitmapDescriptorFactory.HUE_BLUE;
        } else if (wasteCategories.contains("WASTE_PLASTIC") || wasteCategories.contains("WASTE_METAL_FOOD_PACKAGING")) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else if (wasteCategories.contains("WASTE_ELECTRONICS")) {
            return BitmapDescriptorFactory.HUE_RED;
        } else if (wasteCategories.contains("WASTE_TEXTILE")) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if (wasteCategories.contains("WASTE_BIOLOGICAL")) {
            return 45.0f; //Brown
        } else {
            return BitmapDescriptorFactory.HUE_MAGENTA;
        }
    }
}
