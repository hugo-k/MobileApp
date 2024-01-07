package com.example.mobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.List;

public class CustomClusterRenderer extends DefaultClusterRenderer<WasteContainer> {
    private final Context context;

    // Constructor for CustomClusterRenderer
    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<WasteContainer> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    // Override to customize the rendering of individual cluster items
    @Override
    protected void onBeforeClusterItemRendered(WasteContainer item, MarkerOptions markerOptions) {
        float markerColor = determineMarkerColor(item);
        BitmapDescriptor markerIcon = getCustomMarker(markerColor);
        markerOptions.icon(markerIcon);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    // Create a custom marker icon based on the color
    private BitmapDescriptor getCustomMarker(float color) {
        int diameter = 100;
        Bitmap bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.HSVToColor(new float[]{color, 1.0f, 1.0f}));
        circlePaint.setAntiAlias(true);

        canvas.drawCircle(diameter / 3, diameter / 3, diameter / 4, circlePaint);

        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);

        canvas.drawCircle(diameter /3, diameter / 3, diameter / 4, borderPaint);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Determine the marker color based on waste categories
    private float determineMarkerColor(WasteContainer wasteContainer) {
        float HUE_GREEN_PERSO = 80.0F;
        float HUE_RED_PERSO = 25.0F;
        float HUE_BLUE_PERSO = 220.0F;
        List<String> wasteCategories = wasteContainer.getWasteCategories();

        // Check waste categories and assign color accordingly
        if (wasteCategories.contains("WASTE_GLASS") || wasteCategories.contains("WASTE_COLORED_GLASS")) {
            return HUE_GREEN_PERSO;
        } else if (wasteCategories.contains("WASTE_PAPER")) {
            return HUE_BLUE_PERSO;
        } else if (wasteCategories.contains("WASTE_PLASTIC") || wasteCategories.contains("WASTE_METAL_FOOD_PACKAGING")) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else if (wasteCategories.contains("WASTE_ELECTRONICS")) {
            return HUE_RED_PERSO;
        } else if (wasteCategories.contains("WASTE_TEXTILE")) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if (wasteCategories.contains("WASTE_BIOLOGICAL")) {
            return 45.0f; // Brown
        } else {
            return BitmapDescriptorFactory.HUE_MAGENTA;
        }
    }
}
