package com.example.mobileapp;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.List;

public class WasteContainer implements ClusterItem, Serializable {
    private String locId;
    private String name;
    private String binTypeName;
    private List<String> wasteCategories;
    private String locationType;
    private String owner;
    private String wasteCollectionFrequency;
    private List<String> wasteCollectionDays;
    private String addressText;
    private double xloc;
    private double yloc;
    private int imageIconResourceId;
    private int imageBannerResourceId;
    private int wasteList;

    public WasteContainer(LatLng markerPosition, String locId, String name, List<String> wasteCategories, String locationType, String owner,
                          String wasteCollectionFrequency, List<String> wasteCollectionDays, double xloc, double yloc) {
        this.locId = locId;
        this.name = name;
        this.wasteCategories = wasteCategories;
        this.locationType = locationType;
        this.owner = owner;
        this.wasteCollectionFrequency = wasteCollectionFrequency;
        this.wasteCollectionDays = wasteCollectionDays;
        this.xloc = xloc;
        this.yloc = yloc;
    }


    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWasteCategories() {
        return wasteCategories;
    }

    public void setWasteCategories(List<String> wasteCategories) {
        this.wasteCategories = wasteCategories;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getWasteCollectionFrequency() {
        return wasteCollectionFrequency;
    }

    public void setWasteCollectionFrequency(String wasteCollectionFrequency) {
        this.wasteCollectionFrequency = wasteCollectionFrequency;
    }

    public List<String> getWasteCollectionDays() {
        return wasteCollectionDays;
    }

    public void setWasteCollectionDays(List<String> wasteCollectionDays) {
        this.wasteCollectionDays = wasteCollectionDays;
    }

    public double getXloc() {
        return xloc;
    }

    public void setXloc(double xloc) {
        this.xloc = xloc;
    }

    public double getYloc() {
        return yloc;
    }

    public void setYloc(double yloc) {
        this.yloc = yloc;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Nullable
    @Override
    public Float getZIndex() {
        return 0.0f;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(yloc, xloc);
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getAddressText() {
        return addressText;
    }

    public String getBinTypeName() {
        return binTypeName;
    }

    public void setBinTypeName(String binTypeName) {
        this.binTypeName = binTypeName;
    }

    public int getImageIconResourceId() {
        return imageIconResourceId;
    }

    public int getImageBannerResourceId() {
        return imageBannerResourceId;
    }

    public int getWasteList() {
        return wasteList;
    }


    public void setImageResource(String wasteCategory) {
        if (wasteCategory.equals("WASTE_GLASS") || wasteCategory.equals("WASTE_COLORED_GLASS")) {
            imageIconResourceId = R.drawable.coloredglass;
            imageBannerResourceId = R.drawable.glass_wastes;
            wasteList = R.drawable.glass_list;
        } else if (wasteCategory.equals("WASTE_PAPER")) {
            imageIconResourceId = R.drawable.paper;
            imageBannerResourceId = R.drawable.paper_wastes;
            wasteList = R.drawable.paper_list;
        } else if (wasteCategory.equals("WASTE_PLASTIC") || wasteCategory.equals("WASTE_METAL_FOOD_PACKAGING")) {
            imageIconResourceId = R.drawable.plastic;
            imageBannerResourceId = R.drawable.plastic_wastes;
            wasteList = R.drawable.plastic_list;
        } else if (wasteCategory.equals("WASTE_ELECTRONICS")) {
            imageIconResourceId = R.drawable.battery;
            wasteList = 0;
            imageBannerResourceId = R.drawable.electronics;
        } else if (wasteCategory.equals("WASTE_TEXTILE")) {
            imageIconResourceId = R.drawable.clothes;
            imageBannerResourceId = R.drawable.clothes_wastes;
            wasteList = 0;
        } else if (wasteCategory.equals("WASTE_BIOLOGICAL")) {
            imageIconResourceId = R.drawable.organic;
            imageBannerResourceId = R.drawable.organic_wastes;
            wasteList = R.drawable.bio_waste_list;
        }
    }
}
