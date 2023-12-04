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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        FilterAdapter filterAdapter = new FilterAdapter(requireContext(), filterItems);

        GridView gridView = view.findViewById(R.id.gridView);
        ImageButton btnClose = view.findViewById(R.id.btnClose);
        Button btnApply = view.findViewById(R.id.btnApply);
        Button btnAddAll = view.findViewById(R.id.btnAddAll);
        Button btnRemoveAll = view.findViewById(R.id.btnClearAll);

        // Keep the checked checkboxes from the last filter use
        loadSelectedFiltersState();

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
                selectedFilters = getSelectedFilters();
                filterListener.onFiltersApplied(selectedFilters);
                saveSelectedFiltersState();
                closeOverlay();
                dismiss();
            }
        });

        btnAddAll.setOnClickListener(v -> {
            addAllFilters();
            filterAdapter.notifyDataSetChanged();
        });

        btnRemoveAll.setOnClickListener(v -> {
            removeAllFilters();
            filterAdapter.notifyDataSetChanged();
        });

        gridView.setAdapter(filterAdapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FilterListener) {
            filterListener = (FilterListener) context;
            listener = (OnFilterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FilterListener");
        }
    }

    private List<String> getSelectedFilters() {
        List<String> selectedFilters = new ArrayList<>();
        for (FilterItem filterItem : filterItems) {
            if (filterItem.isSelected()) {
                selectedFilters.add("WASTE_" + filterItem.getWasteName().toUpperCase());
            }
        }
        return selectedFilters;
    }

    private void saveSelectedFiltersState() {
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Save the state of each filter item
        for (int i = 0; i < filterItems.size(); i++) {
            FilterItem filterItem = filterItems.get(i);
            editor.putBoolean("checkBox" + i, filterItem.isSelected());
        }

        editor.apply();
    }

    private void loadSelectedFiltersState() {
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        // Load the state of each filter item
        for (int i = 0; i < filterItems.size(); i++) {
            boolean isChecked = preferences.getBoolean("checkBox" + i, false);
            filterItems.get(i).setSelected(isChecked);
        }
    }

    private List<FilterItem> createFilterItems() {
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
        for (FilterItem filterItem : filterItems) {
            filterItem.setSelected(true);
        }
    }

    private void removeAllFilters() {
        for (FilterItem filterItem : filterItems) {
            filterItem.setSelected(false);
        }
    }

    public interface OnFilterInteractionListener {
        void onFilterOverlayClosed();
    }

    private void closeOverlay() {
        if (listener != null) {
            listener.onFilterOverlayClosed();
        }
    }
}
