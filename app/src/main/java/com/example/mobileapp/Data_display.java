package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Data_display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        ImageView imageCategory = findViewById(R.id.imageViewCategory);

        ImageButton goToMapsActivity = (ImageButton)findViewById(R.id.goToMapsActivity);
        goToMapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: See if it's better to use stack or finish activity
                finish();
            }
        });

        String tid = JSONFileReader.dataToSearch(this, 0, "attributes", "tid");
        String binCategory = JSONFileReader.dataToSearch(this, 0, "attributes", "komodita_odpad_separovany");


        switch (binCategory) {
            case "Plasty, nápojové kartony a hliníkové plechovky od nápojů":
                imageCategory.setImageResource(R.drawable.plastic);
                binCategory = "Plastic";
                break;
            case "Papír":
                imageCategory.setImageResource(R.drawable.paper);
                binCategory = "Paper";
                break;
            case "Biologický odpad":
                imageCategory.setImageResource(R.drawable.organic);
                binCategory = "Organic";
                break;
            case "Sklo barevné":
                imageCategory.setImageResource(R.drawable.coloredglass);
                binCategory = "Colored glasses";
                break;
            case "Sklo bílé":
                imageCategory.setImageResource(R.drawable.whiteglass);
                binCategory = "White glasses";
                break;
            default:
                binCategory = "Nothing";
        }

        //Print in text view
        TextView TestTextView = findViewById(R.id.textView01);
        TestTextView.setText(binCategory);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String addressFromActivity = extras.getString("address");
            Log.d("GetAd", "act2 : " + addressFromActivity);

            TextView textViewLocation = findViewById(R.id.textView02);
            textViewLocation.setText(addressFromActivity);
        }

    }

}


