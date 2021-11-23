package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

/**
 * Add Place Activity (AddPlaceActivity.java)
 * This activity allows the user to add a new place.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 21, 2021
 */
public class AddPlaceActivity extends AppCompatActivity {
    private JSONObject places;

    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onCreate");

        Intent intent = getIntent();
        String places = intent.getStringExtra("places");

        try {
            if (places != null) {
                this.places = new JSONObject(places);
            }

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not parse JSON.");
        }

        // Define the Submit Button Listener
        final Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("Button", this.getClass().getSimpleName() + ": " + "SUBMIT");

                JSONObject place = getEditTextFields();

                boolean wasSaved = false;
                if (places != null) {
                    wasSaved = savePlaceToPlaces(place);

                } else {
                    try {
                        if(!place.get("name").toString().equals("")) {
                            // Send place to server
                            MethodInformation mi = new MethodInformation(null, getString(R.string.url),"add", new Object[] {place});
                            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                            // JSONObject result = new JSONObject(ac.get().resultAsJson);
                            // System.out.println(result);

                            // Save place to places on server
                            mi = new MethodInformation(null, getString(R.string.url),"saveToJsonFile", new Object[] {place});
                            ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                            // result = new JSONObject(ac.get().resultAsJson);
                            // System.out.println(result);

                            wasSaved = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(),
                                "Exception: "+ ex.getMessage());
                    }
                }

                // If saved navigate to main view else alert user of missing field.
                if(wasSaved) {
                    Intent intent = new Intent(AddPlaceActivity.this, MainActivity.class);
                    AddPlaceActivity.this.startActivity(intent);

                } else {
                    new AlertDialog.Builder(AddPlaceActivity.this)
                            .setTitle("Alert")
                            .setMessage("Name is a required field.")
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                                        + "Submitted without required name field.");
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        // Define the Clear Button Listener
        final Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("Button", this.getClass().getSimpleName() + ": " + "CLEAR");

                clearEditTextFields();
            }
        });
    }

    /**
     * Get the Edit Text Fields and form a JSON place object.
     *
     * @return JSONObject
     */
    private JSONObject getEditTextFields() {
        JSONObject place = new JSONObject();

        // Get the text from the Edit Text Fields
        // Get text from Name Edit Text
        EditText text = findViewById(R.id.nameEditText);
        String name = text.getText().toString();

        // Get text from Description Edit Text
        text = findViewById(R.id.descriptionEditText);
        String description = text.getText().toString();

        // Get text from Category Edit Text
        text = findViewById(R.id.categoryEditText);
        String category = text.getText().toString();

        // Get text from Address Title Edit Text
        text = findViewById(R.id.addressTitleEditText);
        String address_title = text.getText().toString();

        // Get text from Address Street Edit Text
        text = findViewById(R.id.addressStreetEditText);
        String address_street = text.getText().toString();

        // Get text from Elevation Edit Text
        text = findViewById(R.id.elevationEditText);
        double elevation;
        try {
            elevation = Double.parseDouble(text.getText().toString());

        } catch (NumberFormatException nfe) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not parse elevation to double.");

            elevation = 0.00;
        }

        // Get text from Latitude Edit Text
        text = findViewById(R.id.latitudeEditText);
        double latitude;
        try {
            latitude = Double.parseDouble(text.getText().toString());

        } catch (NumberFormatException nfe) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not parse latitude to double.");

            latitude = 0.00;
        }

        // Get text from Longitude Edit Text
        text = findViewById(R.id.longitudeEditText);
        double longitude;
        try {
            longitude = Double.parseDouble(text.getText().toString());

        } catch (NumberFormatException nfe) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not parse longitude to double.");

            longitude = 0.00;
        }

        // Create the JSON place object.
        try {
            place.put("address-title", address_title);
            place.put("address-street", address_street);
            place.put("elevation", elevation);
            place.put("latitude", latitude);
            place.put("longitude", longitude);
            place.put("name", name);
            place.put("image", "");
            place.put("description", description);
            place.put("category", category);

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not create JSON place object.");
        }

        return place;
    }

    /**
     * Saves the new place to places.json. Returns true if saved.
     *
     * @param place Place
     * @return boolean
     */
    private boolean savePlaceToPlaces(JSONObject place) {
        JSONObject places = new JSONObject();
        try {
            // If the user input a name continue processing else return false.
            if (!place.get("name").toString().equals("")) {
                places.put(place.get("name").toString(), place);

                // Fill JSON object with existing places
                for (Iterator<String> it = this.places.keys(); it.hasNext(); ) {
                    String key = it.next();
                    places.put(key, this.places.get(key));
                }

                writePlacesToFile(places);

                return true;
            }
        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not add JSON place object to places.");
        }

        return false;
    }

    /**
     * Save places to places.json.
     */
    private void writePlacesToFile(JSONObject places) {
        try {
            android.util.Log.d("File", this.getClass().getSimpleName() + ": "
                    + "Writing to file...");

            FileOutputStream fo = openFileOutput("places.json", MODE_PRIVATE);
            OutputStreamWriter os = new OutputStreamWriter(fo);
            os.write(places.toString());
            os.close();

        } catch (FileNotFoundException fnfe) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not find file for writing.");

        } catch (IOException ioe) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not write to file.");
        }
    }

    /**
     * Clear the Edit Text Fields.
     */
    private void clearEditTextFields() {
        // Get text from Name Edit Text
        EditText text = findViewById(R.id.nameEditText);
        text.setText("");

        // Get text from Description Edit Text
        text = findViewById(R.id.descriptionEditText);
        text.setText("");

        // Get text from Category Edit Text
        text = findViewById(R.id.categoryEditText);
        text.setText("");

        // Get text from Address Title Edit Text
        text = findViewById(R.id.addressTitleEditText);
        text.setText("");

        // Get text from Address Street Edit Text
        text = findViewById(R.id.addressStreetEditText);
        text.setText("");

        // Get text from Elevation Edit Text
        text = findViewById(R.id.elevationEditText);
        text.setText("");

        // Get text from Latitude Edit Text
        text = findViewById(R.id.latitudeEditText);
        text.setText("");

        // Get text from Longitude Edit Text
        text = findViewById(R.id.longitudeEditText);
        text.setText("");
    }
}
