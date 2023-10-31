package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

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

        // Parse JSON file on a String
        String parsedJson = JSONFileReader.readJSONFromRawResource(this, R.raw.dataset_test);
        // Look if the String is not empty
        try {

            JSONObject jsonObject = new JSONObject(parsedJson);

            // Create new JSON object by parsing the precedent
            JSONArray feature = jsonObject.getJSONArray("features");

            if (feature.length() > 0){
                // Index 0 is the first feature (see like a root)
                JSONObject firstFeature = feature.getJSONObject(2);

                JSONObject attributes = firstFeature.getJSONObject("attributes");

                String tid = attributes.getString("tid");
                String binCategory = attributes.getString("komodita_odpad_separovany");
                Log.d("tag", "value : " + binCategory);

                switch(binCategory){
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
            }
        } catch (JSONException e) {
            Log.d("tag", "error : " + e);
            throw new RuntimeException(e);
        }

    }

}


