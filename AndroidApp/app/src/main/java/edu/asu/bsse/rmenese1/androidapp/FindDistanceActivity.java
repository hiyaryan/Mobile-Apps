package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FindDistanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private JSONObject places;
    private String key;

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
            this.places = new JSONObject(places);

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
        for (Iterator<String> it = places.keys(); it.hasNext(); ) {
            String place = it.next();
            elements.add(place);
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
}
