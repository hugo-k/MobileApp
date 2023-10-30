package com.example.mobileapp;

import android.content.Context;
import android.content.res.Resources;
import java.io.InputStream;

public class JSONFileReader {
    public static String readJSONFromRawResource(Context context, int resourceId) {
        String json = null;
        try {
            Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource(resourceId);

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
