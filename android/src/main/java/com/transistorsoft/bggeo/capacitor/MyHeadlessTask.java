package com.transistorsoft.capacitor.backgroundgeolocation;

import android.util.Log;

import com.transistorsoft.locationmanager.event.HeadlessEvent;
import com.transistorsoft.locationmanager.event.LocationEvent;
import com.transistorsoft.locationmanager.headless.HeadlessTask;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHeadlessTask implements HeadlessTask {
    @Override
    public void onHeadlessEvent(HeadlessEvent event) {
        Log.i("MyHeadlessTask", "[HeadlessEvent] " + event.getName());

        if (event.getName().equals(HeadlessEvent.LOCATION)) {
            LocationEvent loc = (LocationEvent) event.getEvent();

            double lat = loc.getLocation().getCoords().getLatitude();
            double lng = loc.getLocation().getCoords().getLongitude();

            try {
                URL url = new URL("https://your.api/endpoint");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String json = "{ \"lat\": " + lat + ", \"lng\": " + lng + " }";

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();

                int code = conn.getResponseCode();
                Log.i("MyHeadlessTask", "API response: " + code);

            } catch (Exception e) {
                Log.e("MyHeadlessTask", "Error posting location", e);
            }
        }

        // Important: mark task finished
        event.finish();
    }
}