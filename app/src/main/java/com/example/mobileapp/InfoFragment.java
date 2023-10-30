package com.example.mobileapp;

import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class InfoFragment extends Fragment{

    private TextView addressTextView;

    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        addressTextView = view.findViewById(R.id.txtAddress);

        return view;
    }

    public void updatePostalAddress(String addressText) {
        if (addressTextView != null) {
            addressTextView.setText(addressText);
            Log.d("address", addressText);
        }
    }

}