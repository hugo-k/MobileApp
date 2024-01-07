package com.example.mobileapp;

import static java.lang.String.valueOf;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class JSONFileReader {
    // Method to create a list of WasteContainer objects from a JSON file in the raw resources
    public static List<WasteContainer> createWasteContainersFromJson(Context context) {
        // Read JSON data from the raw resource file
        String parsedJson = readJSONFromRawResource(context, R.raw.dataset_test);

        try {
            // Convert the JSON string to a JSONObject
            JSONObject jsonObject = new JSONObject(parsedJson);

            // Extract the array of WasteContainers from the JSON object
            JSONArray wasteContainersArray = jsonObject.getJSONArray("WasteContainers");

            // Use Gson library to deserialize the JSON array into a list of WasteContainer objects
            Type listType = new TypeToken<List<WasteContainer>>(){}.getType();
            Gson gson = new Gson();
            return gson.fromJson(wasteContainersArray.toString(), listType);

        } catch (JSONException e) {
            // Handle JSON parsing exception
            throw new RuntimeException(e);
        }
    }

    // Method to read JSON data from a raw resource file and return it as a string
    public static String readJSONFromRawResource(Context context, int resourceId) {
        String json = null;
        try {
            // Open the raw resource file as an InputStream
            InputStream inputStream = context.getResources().openRawResource(resourceId);

            // Read the contents of the InputStream into a byte array
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            // Close the InputStream
            inputStream.close();

            // Convert the byte array to a string using UTF-8 encoding
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            // Handle IO exceptions
            e.printStackTrace();
        }
        return json;
    }

    // Method to extract specific data from the JSON file based on given parameters
    public static String dataToSearch(Context context, int index, String object, String valueName){

        String value = null;

        // Read JSON data from the raw resource file
        String parsedJson = JSONFileReader.readJSONFromRawResource(context, R.raw.dataset_test);

        try {
            // Convert the JSON string to a JSONObject
            JSONObject jsonObject = new JSONObject(parsedJson);

            // Extract the array of features from the JSON object
            JSONArray features = jsonObject.getJSONArray("features");

            // Check if the search is for the total count of features
            if ((index == -1) && (object == null) && (value == null)){
                return valueOf(features.length());
            }

            // Get the specific feature at the given index
            JSONObject feature = features.getJSONObject(index);

            // Check if the feature is not empty
            if (feature.length() > 0) {
                // Extract the specified object from the feature
                JSONObject objectToSearch = feature.getJSONObject(object);
                // Get the value associated with the given name
                value = objectToSearch.getString(valueName);
            }

        } catch (JSONException e) {
            // Handle JSON parsing exception
            throw new RuntimeException(e);
        }

        return value;
    }
}
