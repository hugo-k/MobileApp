package com.example.mobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends BottomSheetDialogFragment {
    private FilterListener filterListener;
    private List<String> selectedFilters = new ArrayList<>();
    private List<FilterItem> filterItems = createFilterItems();
    private OnFilterInteractionListener listener;
    private CheckBox checkBoxClothes, checkBoxElectronicDevices, checkBoxPaper, checkBoxOrganic, checkBoxGlass, checkBoxPlastic;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        // Create the FilterAdapter and associate it with the GridView
        FilterAdapter filterAdapter = new FilterAdapter(requireContext(), filterItems);
        GridView gridView = view.findViewById(R.id.gridView);
        ImageButton btnClose = view.findViewById(R.id.btnClose);
        Button btnApply = view.findViewById(R.id.btnApply);
        Button btnAddAll = view.findViewById(R.id.btnAddAll);
        Button btnRemoveAll = view.findViewById(R.id.btnClearAll);

        // Load the previously selected filters
        loadSelectedFiltersState();

        // Set click listeners for buttons
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeOverlay();
                dismiss();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Apply selected filters and notify the listener
                selectedFilters = getSelectedFilters();
                filterListener.onFiltersApplied(selectedFilters);

                // Save the state of selected filters
                saveSelectedFiltersState();

                // Close the overlay
                closeOverlay();
                dismiss();
            }
        });

        btnAddAll.setOnClickListener(v -> {
            // Add all filters and update the adapter
            addAllFilters();
            filterAdapter.notifyDataSetChanged();
        });

        btnRemoveAll.setOnClickListener(v -> {
            // Remove all filters and update the adapter
            removeAllFilters();
            filterAdapter.notifyDataSetChanged();
        });

        // Set the adapter to the GridView
        gridView.setAdapter(filterAdapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Check if the context implements FilterListener
        if (context instanceof FilterListener) {
            filterListener = (FilterListener) context;
            listener = (OnFilterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FilterListener");
        }
    }

    private List<String> getSelectedFilters() {
        // Get the list of selected filters
        List<String> selectedFilters = new ArrayList<>();
        for (FilterItem filterItem : filterItems) {
            if (filterItem.isSelected()) {
                selectedFilters.add("WASTE_" + filterItem.getWasteName().toUpperCase());
            }
        }
        return selectedFilters;
    }

    private void saveSelectedFiltersState() {
        // Save the state of each filter item in SharedPreferences
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < filterItems.size(); i++) {
            FilterItem filterItem = filterItems.get(i);
            editor.putBoolean("checkBox" + i, filterItem.isSelected());
        }

        editor.apply();
    }

    private void loadSelectedFiltersState() {
        // Load the state of each filter item from SharedPreferences
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        for (int i = 0; i < filterItems.size(); i++) {
            boolean isChecked = preferences.getBoolean("checkBox" + i, false);
            filterItems.get(i).setSelected(isChecked);
        }
    }

    private List<FilterItem> createFilterItems() {
        // Create a list of FilterItems with default values
        List<FilterItem> filterItems = new ArrayList<>();

        filterItems.add(new FilterItem(R.drawable.clothes, "Textile", true));
        filterItems.add(new FilterItem(R.drawable.coloredglass, "Glass", true));
        filterItems.add(new FilterItem(R.drawable.organic, "Biological", true));
        filterItems.add(new FilterItem(R.drawable.plastic, "Plastic", true));
        filterItems.add(new FilterItem(R.drawable.paper, "Paper", true));
        filterItems.add(new FilterItem(R.drawable.battery, "Electronics", true));

        return filterItems;
    }

    private void addAllFilters() {
        // Set all filters to selected
        for (FilterItem filterItem : filterItems) {
            filterItem.setSelected(true);
        }
    }

    private void removeAllFilters() {
        // Set all filters to unselected
        for (FilterItem filterItem : filterItems) {
            filterItem.setSelected(false);
        }
    }

    public interface OnFilterInteractionListener {
        // Define method for filter overlay closure
        void onFilterOverlayClosed();
    }

    private void closeOverlay() {
        // Notify the listener when the overlay is closed
        if (listener != null) {
            listener.onFilterOverlayClosed();
        }
    }
}
