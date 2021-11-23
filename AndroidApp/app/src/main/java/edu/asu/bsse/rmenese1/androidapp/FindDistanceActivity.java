package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Find Distance Activity (FindDistanceActivity.java)
 * This activity allows the user to find the Great Circle Distance and initial
 * bearing between two places.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 21, 2021
 */
public class FindDistanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private JSONObject places;
    private String key;
    private String fromPlace;
    private String toPlace;

    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onCreate");
        
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("FindDistanceActivity");

        this.key = extras.getString("key");
        String places = extras.getString("places");

        try {
            if (places != null) {
                this.places = new JSONObject(places);
            }

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not parse JSON.");
        }

        // Create a Set the Spinner with an array of Places
        Spinner fromPlaceSpinner = findViewById(R.id.fromPlaceSpinner);
        fromPlaceSpinner.setOnItemSelectedListener(this);
        setSpinnerElements(fromPlaceSpinner);

        // Create a Set the Spinner with an array of Places
        Spinner toPlaceSpinner = findViewById(R.id.toPlaceSpinner);
        toPlaceSpinner.setOnItemSelectedListener(this);
        setSpinnerElements(toPlaceSpinner);
    }

    /**
     * Implement spinner element select listener.
     *
     * @param parent AdapterView
     * @param view View
     * @param position int
     * @param id long
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        android.util.Log.d("Spinner", this.getClass().getSimpleName()
                + ": " + "onItemSelected "
                + "-> " + parent.getItemAtPosition(position));

        // Extract app:id of spinner from AdapterView
        String appId  = parent.toString().split("app:id/")[1].replace("}", "");

        if (appId.equals("fromPlaceSpinner")) {
            this.fromPlace = parent.getItemAtPosition(position).toString();

        } else if (appId.equals("toPlaceSpinner")) {
            this.toPlace = parent.getItemAtPosition(position).toString();

        } else {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not get valid spinner id.");
        }

        try {
            JSONObject fromPlace;
            JSONObject toPlace;
            if (places != null) {
                fromPlace = this.places.getJSONObject(this.fromPlace);
                toPlace = this.places.getJSONObject(this.toPlace);

            } else {
                MethodInformation mi = new MethodInformation(null, getString(R.string.url),"get", new Object[] {this.fromPlace});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject jsonFromPlace = new JSONObject(ac.get().resultAsJson);
                fromPlace = jsonFromPlace.getJSONObject("result");

                mi = new MethodInformation(null, getString(R.string.url),"get", new Object[] {this.toPlace});
                ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject jsonToPlace = new JSONObject(ac.get().resultAsJson);
                toPlace = jsonToPlace.getJSONObject("result");
            }

            calcDistance(fromPlace, toPlace);
        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not find place from provided key.");

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Exception: "+ ex.getMessage());
        }
    }

    /**
     * Implement spinner element no select listener.
     *
     * @param parent AdapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d("Spinner", this.getClass().getSimpleName() + ": " + "onNothingSelected");
    }


    /**
     * Set the keys of places.json as the elements to display in spinner.
     * @param spinner Spinner
     */
    private void setSpinnerElements(Spinner spinner) {
        android.util.Log.d("Spinner", this.getClass().getSimpleName() + ": " + "setSpinnerElements");

        ArrayList<String> elements = new ArrayList<>();

        if (places != null) {
            for (Iterator<String> it = places.keys(); it.hasNext(); ) {
                String place = it.next();
                elements.add(place);
            }
        } else {
            try {
                MethodInformation mi = new MethodInformation(null, getString(R.string.url), "getNames", new Object[]{});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject places = new JSONObject(ac.get().resultAsJson);
                JSONArray names = (JSONArray) places.get("result");

                for (int i = 0; i < names.length(); i++) {
                    elements.add(names.getString(i));
                }

            } catch (Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),
                        "Exception: "+ ex.getMessage());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // The fromSpinner should start with the place presented on the main view
        if (spinner.getId() == R.id.fromPlaceSpinner) {
            int spinnerPosition = adapter.getPosition(this.key);
            spinner.setSelection(spinnerPosition);
        }
    }

    /**
     * Calculate the Great Circle distance and Initial Bearing.
     *
     * @param fromPlace Place
     * @param toPlace Place
     */
    public void calcDistance(JSONObject fromPlace, JSONObject toPlace) {
        android.util.Log.d("calcDistance", this.getClass().getSimpleName() + ": "
                + "Calculating distance between " + this.fromPlace + " and " + this.toPlace);

        double distance = 0.00;
        double bearing = 0.00;
        try {
            // Set fromPlace = (lat1, lon1)
            double lat1 = fromPlace.getDouble("latitude");
            double lon1 = fromPlace.getDouble("longitude");

            // Set toPlace = (lat2, lon2)
            double lat2 = toPlace.getDouble("latitude");
            double lon2 = toPlace.getDouble("longitude");

            if((lon2 - lon1 != 0) && (lat2 - lat1 != 0)) {
                // Great Circle distance computation using the law of cosines
                // Reference: https://www.geodatasource.com/developers/java
                double theta = lon1 - lon2;
                distance = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
                distance = Math.acos(distance);
                distance = Math.toDegrees(distance);
                distance = distance * 60 * 1.1515;

                // Initial bearing computation
                // Reference: https://www.movable-type.co.uk/scripts/latlong.html
                double y = Math.sin(lon2 - lon1) * Math.cos(lat2);
                double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
                double phi = Math.atan2(y, x);
                bearing = ((phi * 180 / Math.PI) + 360) % 360;
            }

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not get double value from place.");
        }

        // Update the view with the calculations
        TextView greatCircleDistance = findViewById(R.id.greatCircleDistance);
        greatCircleDistance.setText(Double.toString(distance));

        TextView initialBearing = findViewById(R.id.initialBearing);
        initialBearing.setText(Double.toString(bearing));
    }
}
