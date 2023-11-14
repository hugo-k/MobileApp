package com.example.brnowaste;

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
    public static List<WasteContainer> createWasteContainersFromJson(Context context) {
        String parsedJson = readJSONFromRawResource(context, R.raw.dataset_test);

        try {
            JSONObject jsonObject = new JSONObject(parsedJson);
            JSONArray wasteContainersArray = jsonObject.getJSONArray("WasteContainers");

            Type listType = new TypeToken<List<WasteContainer>>(){}.getType();
            Gson gson = new Gson();
            return gson.fromJson(wasteContainersArray.toString(), listType);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readJSONFromRawResource(Context context, int resourceId) {
        String json = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String dataToSearch(Context context, int index, String object, String valueName){

        String value = null;

        String parsedJson = JSONFileReader.readJSONFromRawResource(context, R.raw.dataset_test);

        try {
            JSONObject jsonObject = new JSONObject(parsedJson);
            JSONArray features = jsonObject.getJSONArray("features");

            if ((index == -1) && (object == null) && (value == null)){
                return valueOf(features.length());
            }

            JSONObject feature = features.getJSONObject(index);

            if (feature.length() > 0) {
                JSONObject objectToSearch = feature.getJSONObject(object);
                value = objectToSearch.getString(valueName);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return value;
    }
}
