package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mobileapp.R;

public class InfoFragment extends Fragment {

    private TextView addressTextView, wasteTypeTxtView;
    private Data_display dataDisplay;
    private WasteContainer wasteContainer;
    private ImageView imageWasteType;
    int imageIconResourceId;


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
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        addressTextView = view.findViewById(R.id.txtAddress);
        wasteTypeTxtView = view.findViewById(R.id.wasteTypeTxtView);
        imageWasteType = view.findViewById(R.id.imageWasteType);
        dataDisplay = new Data_display();

        Bundle args = getArguments();
        if (args != null) {
            wasteContainer = (WasteContainer) args.getSerializable("wasteContainer");

            if (wasteContainer != null) {
                wasteTypeTxtView.setText(wasteContainer.getBinTypeName());
                addressTextView.setText(wasteContainer.getAddressText());

                imageIconResourceId = wasteContainer.getImageIconResourceId();
                imageWasteType.setImageResource(imageIconResourceId);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Data_display.class);
                intent.putExtra("wasteContainer", wasteContainer);
                startActivity(intent);
            }
        });

        return view;
    }
}