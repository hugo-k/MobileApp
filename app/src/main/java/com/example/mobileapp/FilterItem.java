package com.example.mobileapp;

// FilterItem.java
public class FilterItem {
    private int iconResourceId;
    private String filterName;
    private boolean isSelected;

    public FilterItem(int iconResourceId, String filterName, boolean isSelected) {
        this.iconResourceId = iconResourceId;
        this.filterName = filterName;
        this.isSelected = isSelected;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public String getFilterName() {
        return filterName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getWasteName() {
        return filterName;
    }
}
