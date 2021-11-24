package edu.asu.bsse.rmenese1.androidapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        setContentView(R.layout.activity_add);

        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onCreate");

        // Create new instance of dbHelper
        dbInitialized = false;
        dbHelper = new PlacesContract.PlacesDbHelper(getBaseContext());

        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("AddPlaceActivity");

        dbInitialized = Boolean.getBoolean(extras.getString("dbInitialized"));
        String places = extras.getString("places");

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

                // If places is null the app is connected to the JsonRPC server
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
                            mi = new MethodInformation(null, getString(R.string.url),"saveToJsonFile", new Object[] {});
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
                            .setMessage("Name is a required and unique field.")
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

                // If the db is not initialized write to file
                if(dbInitialized) {
                    places.put(place.get("name").toString(), place);

                    // Fill JSON object with existing places
                    for (Iterator<String> it = this.places.keys(); it.hasNext(); ) {
                        String key = it.next();
                        places.put(key, this.places.get(key));
                    }

                    writePlacesToFile(places);
                    return true;

                } else {
                    return savePlaceToDb(place);
                }
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
     * Save new place to database.
     *
     * @param place JSONObject
     * @return isDuplicate
     */
    private boolean savePlaceToDb(JSONObject place) {
        android.util.Log.d("DB", this.getClass().getSimpleName() + ": "
                + "Saving new place to database...");

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // Look for item in db first
            String selection = PlacesContract.PlacesEntry.COLUMN_NAME_NAME + " = ?";
            String[] selectionArgs = { place.getString("name") };

            Cursor cursor = db.query(
                    PlacesContract.PlacesEntry.TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            boolean placeExists = false;
            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_NAME));

                android.util.Log.d("DB",
                        this.getClass().getSimpleName() + ": Found " + name + " in database.");

                placeExists = true;
            }

            cursor.close();

            if (!placeExists) {
                // Add new rows to database
                ContentValues values = new ContentValues();

                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_NAME, place.getString("name"));
                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_DESCRIPTION, place.getString("description"));
                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_CATEGORY, place.getString("category"));
                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_TITLE, place.getString("address-title"));
                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_STREET, place.getString("address-street"));
                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_ELEVATION, place.getDouble("elevation"));
                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_LATITUDE, place.getDouble("latitude"));
                values.put(PlacesContract.PlacesEntry.COLUMN_NAME_LONGITUDE, place.getDouble("longitude"));

                // Add values to new row
                long newRowId = db.insert(PlacesContract.PlacesEntry.TABLE_NAME, null, values);

                android.util.Log.d("DB",
                        this.getClass().getSimpleName() + ": Added new row -> " + newRowId + ": " + place.getString("name"));

                return true;
            }

        } catch (JSONException je) {
            android.util.Log.d("Error",
                    this.getClass().getSimpleName() + ": Could not parse JSON.");

        } catch (Exception e) {
            android.util.Log.d("Exception",
                    this.getClass().getSimpleName() + ": Could not initialize database.");

        } finally {
            db.close();
        }

        android.util.Log.d("Exception",
                this.getClass().getSimpleName() + ": Duplicate names found.");

        return false;
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
