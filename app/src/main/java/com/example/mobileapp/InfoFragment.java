package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    private TextView addressTextView, wasteTypeTxtView;
    private Data_display dataDisplay;
    private WasteContainer wasteContainer;
    private ImageView imageWasteType;
    int imageIconResourceId;

    // Factory method to create a new instance of InfoFragment
    public static InfoFragment newInstance(String address, double latitude, double longitude, String index) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);
        args.putString("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // Initialize UI elements
        addressTextView = view.findViewById(R.id.txtAddress);
        wasteTypeTxtView = view.findViewById(R.id.wasteTypeTxtView);
        imageWasteType = view.findViewById(R.id.imageWasteType);
        dataDisplay = new Data_display();

        // Retrieve arguments passed to the fragment
        Bundle args = getArguments();
        if (args != null) {
            // Extract WasteContainer from arguments
            wasteContainer = (WasteContainer) args.getSerializable("wasteContainer");

            if (wasteContainer != null) {
                // Populate UI elements with WasteContainer data
                wasteTypeTxtView.setText(wasteContainer.getBinTypeName());
                addressTextView.setText(wasteContainer.getAddressText());

                // Set the image resource based on the WasteContainer's icon
                imageIconResourceId = wasteContainer.getImageIconResourceId();
                imageWasteType.setImageResource(imageIconResourceId);
            }
        }

        // Set a click listener to open the detailed view when the fragment is clicked
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Data_display activity with WasteContainer details
                Intent intent = new Intent(getActivity(), Data_display.class);
                intent.putExtra("wasteContainer", wasteContainer);
                startActivity(intent);
            }
        });

        return view;
    }
}
