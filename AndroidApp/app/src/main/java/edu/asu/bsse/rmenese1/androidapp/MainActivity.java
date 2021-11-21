package edu.asu.bsse.rmenese1.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main Activity (MainActivity.java)
 * This is the main activity controller.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 19, 2021
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private JSONObject places;

    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a Set the Spinner with an array of Places
        Spinner spinner = (Spinner) findViewById(R.id.placesSpinner);
        spinner.setOnItemSelectedListener(this);
        setSpinnerElements(spinner);

        // Make the Description Text View scrollable
        final TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        // Make the Address-Street Text View scrollable
        final TextView addressStreetTextView = findViewById(R.id.addressStreetTextView);
        addressStreetTextView.setMovementMethod(new ScrollingMovementMethod());

        // Define the Name Button Listener
        final Button nameButton = findViewById(R.id.nameButton);
        nameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServicesActivity.class);
                intent.putExtra("ServicesActivity", "Requesting services.");
                MainActivity.this.startActivity(intent);
            }
        });

        // Define the Test Button Listener
        final Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlertActivity.class);
                intent.putExtra("AlertActivity", "Hello Android Developer");
                MainActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * onRestart
     * This method is called when the app is opened from the background.
     * Action: From the MainActivity view send the app into the background and reopen.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onRestart");
    }

    /**
     * onStart
     * This method is called when the view is called into the foreground.
     * Action: Open the app into the MainActivity view.
     */
    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onStart");
    }

    /**
     * onResume
     * This method is called when the view returns to the foreground.
     * Action: Press OK from the AlertActivity view.
     */
    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onResume");
    }

    /**
     * onPause
     * This method is called when the view is set to change.
     * Action: Press the TEST button.
     */
    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onPause");
    }

    /**
     * onStop
     * This method is called when the view has changed.
     * Action: Press the TEST button and wait for AlertActivity view to display.
     */
    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onStop");
    }

    /**
     * onDestroy
     * This method is called when the app is closed.
     * Action: Close the app from the MainActivity view.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onDestroy");
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

        String key = parent.getItemAtPosition(position).toString();
        try {
            JSONObject place = getPlaces().getJSONObject(key);

            Button nameButton = (Button) findViewById(R.id.nameButton);
            nameButton.setText(key);

            TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
            descriptionTextView.setText(place.getString("description"));

            TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);
            categoryTextView.setText(place.getString("category"));

            TextView addressTitleTextView = (TextView) findViewById(R.id.addressTitleTextView);
            addressTitleTextView.setText(place.getString("address-title"));

            TextView addressStreetTextView = (TextView) findViewById(R.id.addressStreetTextView);
            addressStreetTextView.setText(place.getString("address-street"));

            TextView elevationTextView = (TextView) findViewById(R.id.elevationTextView);
            elevationTextView.setText(place.getString("elevation"));

            TextView latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
            latitudeTextView.setText(place.getString("latitude"));

            TextView longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
            longitudeTextView.setText(place.getString("longitude"));

        } catch (JSONException e) {
            System.out.println("Setting default place " + key);

            Button nameButton = (Button) findViewById(R.id.nameButton);
            nameButton.setText(getString(R.string.name_button));

            TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
            descriptionTextView.setText(getString(R.string.description_text_view));

            TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);
            categoryTextView.setText(getString(R.string.category_text_view));

            TextView addressTitleTextView = (TextView) findViewById(R.id.addressTitleTextView);
            addressTitleTextView.setText(getString(R.string.address_title_text_view));

            TextView addressStreetTextView = (TextView) findViewById(R.id.addressStreetTextView);
            addressStreetTextView.setText(getString(R.string.address_street_text_view));

            TextView elevationTextView = (TextView) findViewById(R.id.elevationTextView);
            elevationTextView.setText(getString(R.string.elevation_text_view));

            TextView latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
            latitudeTextView.setText(getString(R.string.latitude_text_view));

            TextView longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
            longitudeTextView.setText(getString(R.string.longitude_text_view));
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

        JSONObject places = readPlacesFile();

        ArrayList<String> elements = new ArrayList<>();
        elements.add("ASU-Poly");

        for (Iterator<String> it = places.keys(); it.hasNext(); ) {
            String place = it.next();
            elements.add(place);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Read places from res and convert to json object.
     *
     * @return JSONObject
     */
    private JSONObject readPlacesFile() {
        android.util.Log.d("Spinner", this.getClass().getSimpleName() + ": " + "setSpinnerElements");

        InputStream inputStream = getResources()
                .openRawResource(getResources().getIdentifier("places", "raw", getPackageName()));

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        try {
            for (String line; (line = br.readLine()) != null; ) {
                sb.append(line);
            }

            this.places = new JSONObject(sb.toString());

        } catch (IOException ioe) {
            System.out.print("Error parsing string.");

        } catch (JSONException je) {
            System.out.print("Error parsing json.");
        }

        return this.places;
    }

    /**
     * Get places object assigned on app launch.
     *
     * @return JSONObject
     */
    public JSONObject getPlaces() {
        return places;
    }
}