package com.example.mobileapp;

import android.content.Intent;
import android.location.Address;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

public class InfoFragment extends Fragment {

    private Marker associatedMarker;
    public int fragmentId;

    public static InfoFragment newInstance(String address, String wasteType) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        args.putString("wasteType", wasteType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentId = getArguments().getInt("fragmentId", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        TextView addressTextView = view.findViewById(R.id.txtAddress);
        TextView wasteTypeTxtView = view.findViewById(R.id.wasteTypeTxtView);
        ImageButton btnDataDisplay = view.findViewById(R.id.btnMoreInfo);

        btnDataDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Bundle args = getArguments();
        if (args != null) {
            String address = args.getString("address");
            addressTextView.setText(address);

            String wasteType = args.getString("wasteType");
            wasteTypeTxtView.setText(wasteType);
        }
        return view;
    }

    public interface OnMarkerSelectedListener {
        void onMarkerSelected(int markerIndex);
    }
    private OnMarkerSelectedListener markerSelectedListener;


}