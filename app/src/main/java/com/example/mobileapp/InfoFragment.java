package com.example.mobileapp;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

public class InfoFragment extends Fragment {

    private Marker associatedMarker;
    public int fragmentId;

    public static InfoFragment newInstance(String address) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentId = getArguments().getInt("fragmentId", -1);
        Log.d("fragmentId", ""+ fragmentId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        TextView addressTextView = view.findViewById(R.id.txtAddress);

        Bundle args = getArguments();
        if (args != null) {
            String address = args.getString("address");
            addressTextView.setText(address);
        }
        return view;
    }

    public interface OnMarkerSelectedListener {
        void onMarkerSelected(int markerIndex);
    }
    private OnMarkerSelectedListener markerSelectedListener;


}