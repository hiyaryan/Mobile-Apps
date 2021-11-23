package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Modify Place Activity (ModifyPlaceActivity.java)
 * This activity allows the user to modify an existing place.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 21, 2021
 */
public class ModifyPlaceActivity extends AppCompatActivity {
    private JSONObject places;

    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onCreate");

        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("ModifyPlaceActivity");

        String key = extras.getString("key");
        String places = extras.getString("places");

        try {
            if (places != null) {
                this.places = new JSONObject(places);
            }

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not parse JSON.");
        }

        // Set name text view to key of place to be modified
        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(key);
        setEditTextFields(key);

        // Define the Submit Button Listener
        final Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("Button", this.getClass().getSimpleName() + ": " + "SUBMIT");

                updatePlaceInPlaces(key);

                Intent intent = new Intent(ModifyPlaceActivity.this, MainActivity.class);
                ModifyPlaceActivity.this.startActivity(intent);
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
     * Sets the edit text fields on the view to the place's attributes.
     *
     * @param key Name
     */
    public void setEditTextFields(String key) {
        try {
            JSONObject place;
            if (places != null) {
                place = (JSONObject) this.places.get(key);

            } else {
                MethodInformation mi = new MethodInformation(null, getString(R.string.url),"get", new Object[] {key});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject jsonPlace = new JSONObject(ac.get().resultAsJson);
                place = jsonPlace.getJSONObject("result");
            }

            // Get text from Description Edit Text
            EditText text = findViewById(R.id.descriptionEditText);
            text.setText(place.getString("description"));

            // Get text from Category Edit Text
            text = findViewById(R.id.categoryEditText);
            text.setText(place.getString("category"));

            // Get text from Address Title Edit Text
            text = findViewById(R.id.addressTitleEditText);
            text.setText(place.getString("address-title"));

            // Get text from Address Street Edit Text
            text = findViewById(R.id.addressStreetEditText);
            text.setText(place.getString("address-street"));

            // Get text from Elevation Edit Text
            text = findViewById(R.id.elevationEditText);
            text.setText(place.getString("elevation"));

            // Get text from Latitude Edit Text
            text = findViewById(R.id.latitudeEditText);
            text.setText(place.getString("latitude"));

            // Get text from Longitude Edit Text
            text = findViewById(R.id.longitudeEditText);
            text.setText(place.getString("longitude"));

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could set edit text fields in modify view.");

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Exception: "+ ex.getMessage());
        }
    }

    /**
     * Saves the new place to places.json.
     *
     * @param key String
     */
    private void updatePlaceInPlaces(String key) {
        // Get the text from the Edit Text Fields
        // Get text from Description Edit Text
        EditText text = findViewById(R.id.descriptionEditText);
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

        JSONObject place = new JSONObject();
        try {
            if (places != null) {
                place = (JSONObject) this.places.get(key);

            } else {
                MethodInformation mi = new MethodInformation(null, getString(R.string.url),"get", new Object[] {key});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject jsonPlace = new JSONObject(ac.get().resultAsJson);
                place = jsonPlace.getJSONObject("result");
            }

            place.put("address-title", address_title);
            place.put("address-street", address_street);
            place.put("elevation", elevation);
            place.put("latitude", latitude);
            place.put("longitude", longitude);
            place.put("name", key);
            place.put("image", "");
            place.put("description", description);
            place.put("category", category);

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not add JSON place object to places.");

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Exception: "+ ex.getMessage());
        }

        // Modify place in local storage if server is not up
        if (places != null) {
            writePlacesToFile(this.places);

        } else {
            // Send place to server
            MethodInformation mi = new MethodInformation(null, getString(R.string.url),"add", new Object[] {place});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

            // JSONObject result = new JSONObject(ac.get().resultAsJson);
            // System.out.println(result);

            // Save place to places on server
            mi = new MethodInformation(null, getString(R.string.url),"saveToJsonFile", new Object[] {});
            ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

            // result = new JSONObject(ac.get().resultAsJson);
            // System.out.println(result);
        }
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
        // Get text from Description Edit Text
        EditText text = findViewById(R.id.descriptionEditText);
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
