package com.example.mobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    private TextView addressTextView;

    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView addressTextView = view.findViewById(R.id.addressTextView);

        return view;
    }

    public void updatePostalAddress(String postalAddress) {
        if (addressTextView != null) {
            addressTextView.setText(postalAddress);
        }
    }
}