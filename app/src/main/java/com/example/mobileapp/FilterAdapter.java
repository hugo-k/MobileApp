package com.example.mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileapp.FilterItem;
import com.example.mobileapp.R;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_filter_grid, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
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
            filterItem.setSelected(!filterItem.isSelected());
            notifyDataSetChanged();
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView filterNameTextView;
        ImageView checkboxImageView;

        ViewHolder(View view) {
            iconImageView = view.findViewById(R.id.filterIcon);
            filterNameTextView = view.findViewById(R.id.filterName);
            checkboxImageView = view.findViewById(R.id.filterCheckbox);
        }
    }
}
