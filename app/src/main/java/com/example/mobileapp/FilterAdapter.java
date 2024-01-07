package com.example.mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FilterAdapter extends BaseAdapter {

    private Context context;
    private List<FilterItem> filterItems;

    public FilterAdapter(Context context, List<FilterItem> filterItems) {
        this.context = context;
        this.filterItems = filterItems;
    }

    @Override
    public int getCount() {
        return filterItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filterItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate the layout for each list item
            convertView = LayoutInflater.from(context).inflate(R.layout.item_filter_grid, parent, false);

            // Create a ViewHolder and associate it with the view
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled; retrieve the ViewHolder from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FilterItem filterItem = filterItems.get(position);

        // Set icon
        viewHolder.iconImageView.setImageResource(filterItem.getIconResourceId());

        // Set filter name
        viewHolder.filterNameTextView.setText(filterItem.getFilterName());

        // Set checkbox image based on the selection state
        if (filterItem.isSelected()) {
            viewHolder.checkboxImageView.setImageResource(R.drawable.baseline_check_circle_24);
        } else {
            viewHolder.checkboxImageView.setImageResource(R.drawable.baseline_radio_button_unchecked_24);
        }

        // Set click listener for the whole item
        convertView.setOnClickListener(v -> {
            // Toggle the selection state when clicked
            filterItem.setSelected(!filterItem.isSelected());

            // Notify the adapter that the dataset has changed
            notifyDataSetChanged();
        });

        return convertView;
    }

    // ViewHolder pattern to improve ListView performance
    private static class ViewHolder {
        ImageView iconImageView;
        TextView filterNameTextView;
        ImageView checkboxImageView;

        ViewHolder(View view) {
            // Initialize the views within the ViewHolder
            iconImageView = view.findViewById(R.id.filterIcon);
            filterNameTextView = view.findViewById(R.id.filterName);
            checkboxImageView = view.findViewById(R.id.filterCheckbox);
        }
    }
}
