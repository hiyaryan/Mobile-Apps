package edu.asu.bsse.rmenese1.androidapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
 * Copyright 2021 Ryan Meneses. The SER423 Instructional Team and Arizona State University
 * have the right to build and evaluate this software package for the purposes of grading
 * and program assessment.
 *
 * @author Ryan Meneses     mailto: rmenese1@asu.edu
 * @version 1.0
 * @since November 21, 2021
 */
public class ModifyPlaceActivity extends AppCompatActivity {
    private JSONObject places;
    private boolean dbInitialized;
    private PlacesContract.PlacesDbHelper dbHelper;

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

        // Create new instance of dbHelper
        dbInitialized = false;

        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("ModifyPlaceActivity");

        String key = extras.getString("key");
        if (extras.getString("dbInitialized").equals("true")) {
            dbInitialized = true;
            dbHelper = new PlacesContract.PlacesDbHelper(getBaseContext());
        }
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
            JSONObject place = new JSONObject();

            // If the server is not up run using the database
            if (places != null) {

                // If the database is not initialized run using the file system
                if(!dbInitialized) {
                    android.util.Log.d("File",
                            this.getClass().getSimpleName() + ": Setting " + key + " fields with contents from file.");

                    place = (JSONObject) this.places.get(key);

                } else {
                    android.util.Log.d("DB",
                            this.getClass().getSimpleName() + ": Setting " + key + " fields with contents from database.");
                }

            } else {
                MethodInformation mi = new MethodInformation(null, getString(R.string.url),"get", new Object[] {key});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject jsonPlace = new JSONObject(ac.get().resultAsJson);
                place = jsonPlace.getJSONObject("result");
            }

            // Get all of the EditText fields to set default values
            EditText descriptionTextView = findViewById(R.id.descriptionEditText);
            EditText categoryTextView = findViewById(R.id.categoryEditText);
            EditText addressTitleTextView = findViewById(R.id.addressTitleEditText);
            EditText addressStreetTextView = findViewById(R.id.addressStreetEditText);
            EditText elevationTextView = findViewById(R.id.elevationEditText);
            EditText latitudeTextView = findViewById(R.id.latitudeEditText);
            EditText longitudeTextView = findViewById(R.id.longitudeEditText);

            // Set default values from the file
            if(!dbInitialized) {
                descriptionTextView.setText(place.getString("description"));
                categoryTextView.setText(place.getString("category"));
                addressTitleTextView.setText(place.getString("address-title"));
                addressStreetTextView.setText(place.getString("address-street"));
                elevationTextView.setText(place.getString("elevation"));
                latitudeTextView.setText(place.getString("latitude"));
                longitudeTextView.setText(place.getString("longitude"));

                // Set default values from the database
            } else {
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                // Look for item in db first
                String selection = PlacesContract.PlacesEntry.COLUMN_NAME_NAME + " = ?";
                String[] selectionArgs = { key };

                Cursor cursor = db.query(
                        PlacesContract.PlacesEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );

                cursor.moveToFirst();
                String description = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_CATEGORY));
                String address_title = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_TITLE));
                String address_street = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_STREET));
                String elevation = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_ELEVATION));
                String latitude = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_LONGITUDE));

                descriptionTextView.setText(description);
                categoryTextView.setText(category);
                addressTitleTextView.setText(address_title);
                addressStreetTextView.setText(address_street);
                elevationTextView.setText(elevation);
                latitudeTextView.setText(latitude);
                longitudeTextView.setText(longitude);

                cursor.close();
                db.close();
            }

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
                if(!dbInitialized) {
                    android.util.Log.d("File",
                            this.getClass().getSimpleName() + ": Updating " + key + " fields in file system.");

                    place = (JSONObject) this.places.get(key);

                } else {
                    android.util.Log.d("DB",
                            this.getClass().getSimpleName() + ": Updating " + key + " fields in database.");
                }

            } else {
                MethodInformation mi = new MethodInformation(null, getString(R.string.url),"get", new Object[] {key});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject jsonPlace = new JSONObject(ac.get().resultAsJson);
                place = jsonPlace.getJSONObject("result");
            }

            // Use the place json object to update file system or server
            if (!dbInitialized) {
                place.put("address-title", address_title);
                place.put("address-street", address_street);
                place.put("elevation", elevation);
                place.put("latitude", latitude);
                place.put("longitude", longitude);
                place.put("name", key);
                place.put("image", "");
                place.put("description", description);
                place.put("category", category);

                // Directly update the fields in the database
            } else {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Look for item in db first
                String selection = PlacesContract.PlacesEntry.COLUMN_NAME_NAME + " = ?";
                String[] selectionArgs = { key };

                ContentValues cv = new ContentValues();
                cv.put(PlacesContract.PlacesEntry.COLUMN_NAME_DESCRIPTION, description);
                cv.put(PlacesContract.PlacesEntry.COLUMN_NAME_CATEGORY, category);
                cv.put(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_TITLE, address_title);
                cv.put(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_STREET, address_street);
                cv.put(PlacesContract.PlacesEntry.COLUMN_NAME_ELEVATION, elevation);
                cv.put(PlacesContract.PlacesEntry.COLUMN_NAME_LATITUDE, latitude);
                cv.put(PlacesContract.PlacesEntry.COLUMN_NAME_LONGITUDE, longitude);

                db.update(PlacesContract.PlacesEntry.TABLE_NAME, cv, selection, selectionArgs);
                db.close();
            }

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not add JSON place object to places.");

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Exception: "+ ex.getMessage());
        }

        // Modify place in local storage if server is not up
        if (places != null) {
            // Modify place in file if database is not initialized
            if (!dbInitialized) {
                android.util.Log.d("File",
                        this.getClass().getSimpleName() + ": Writing updated " + key + " to file.");

                writePlacesToFile(this.places);

            } else {
                android.util.Log.d("DB",
                        this.getClass().getSimpleName() + ": Updated " + key + " in database.");
            }

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
