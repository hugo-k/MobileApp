package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageButton;

import java.io.InputStream;

public class Data_display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        ImageButton goToMapsActivity = (ImageButton)findViewById(R.id.goToMapsActivity);
        goToMapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: See if it's better to use stack or finish activity
                finish();
            }
        });

    }


}


