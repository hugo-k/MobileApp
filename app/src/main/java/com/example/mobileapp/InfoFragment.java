package com.example.mobileapp;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InfoFragment extends Fragment {

    private TextView addressTextView;

    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        addressTextView = view.findViewById(R.id.txtAddress);

        Button openInformations = view.findViewById(R.id.btnDirection);
        openInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Data_display.class));
            }
        });

        return view;
    }

    public void updatePostalAddress(String addressText) {
        if (addressTextView != null) {
            Log.d("GetAddress", addressText);
            addressTextView.setText(addressText);
        }
    }

}