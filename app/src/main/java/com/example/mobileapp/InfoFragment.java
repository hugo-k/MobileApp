package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    private TextView addressTextView;

    public static InfoFragment newInstance(String address, String wasteType, double latitude, double longitude) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        args.putString("wasteType", wasteType);
        args.putString("latitude", String.valueOf(latitude));
        args.putString("longitude", String.valueOf(longitude));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        addressTextView = view.findViewById(R.id.txtAddress);
        ImageButton displayData = view.findViewById(R.id.btnMoreInfo);
        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String addressFromActivity = getArguments().getString("address");
                String tagFromActivity = getArguments().getString("index");

                Intent i = new Intent(getActivity(), Data_display.class);
                i.putExtra("address", addressFromActivity);
                i.putExtra("index", tagFromActivity);

                startActivity(i);
            }
        });

        return view;
    }

    public void updatePostalAddress(String addressText) {
        if (addressTextView != null) {
            addressTextView.setText(addressText);
        }
    }
}