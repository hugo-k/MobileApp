package com.example.mobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

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
        BitmapDescriptor markerIcon = getCustomMarker(markerColor);
        markerOptions.icon(markerIcon);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    private BitmapDescriptor getCustomMarker(float color) {
        int diameter = 50;
        Bitmap bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.HSVToColor(new float[]{color, 1.0f, 1.0f}));
        circlePaint.setAntiAlias(true);

        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, circlePaint);

        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);

        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, borderPaint);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private float determineMarkerColor(WasteContainer wasteContainer) {
        List<String> wasteCategories = wasteContainer.getWasteCategories();
        if (wasteCategories.contains("WASTE_GLASS") || wasteCategories.contains("WASTE_COLORED_GLASS")) {
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
