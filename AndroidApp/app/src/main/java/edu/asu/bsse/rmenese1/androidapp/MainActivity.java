package edu.asu.bsse.rmenese1.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
    private String key = "ASU-Poly";
    private PlacesContract.PlacesDbHelper dbHelper;
    private boolean dbInitialized;
    private boolean test = false;

    /**
     * onCreate
     * This method initializes the view.
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent testIntent = getIntent();
        String test = testIntent.getStringExtra("test");

        // Test button toggles app between file and database versions.
        if (test != null) {
            if (test.equals("true")) {
                android.util.Log.d("File",
                        this.getClass().getSimpleName() + ": Switching to file mode.");

                this.test = true;
            } else {
                android.util.Log.d("DB",
                        this.getClass().getSimpleName() + ": Switching to database mode.");

                this.test = false;
            }

            // Default App uses database model.
        } else {
            this.test = false;
        }

        // Create new instance of dbHelper
        dbInitialized = false;
        dbHelper = new PlacesContract.PlacesDbHelper(getBaseContext());

        // Create a Set the Spinner with an array of Places
        Spinner spinner = findViewById(R.id.placesSpinner);
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
        nameButton.setOnClickListener(v -> {
            Bundle extras = new Bundle();
            extras.putString("key", key);

            // Places will be null if the server is used to get a place
            if (places != null) {
                extras.putString("places", places.toString());
            }

            extras.putString("dbInitialized", String.valueOf(dbInitialized));

            Intent intent = new Intent(MainActivity.this, ServicesActivity.class);
            intent.putExtra("ServicesActivity", extras);
            MainActivity.this.startActivity(intent);
        });

        // Define the Test Button Listener
        final Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(v -> {
            Bundle extras = new Bundle();
            extras.putString("hello", "Hello Android Developer");
            extras.putString("test", String.valueOf(test));

            Intent intent = new Intent(MainActivity.this, AlertActivity.class);
            intent.putExtra("AlertActivity", extras);
            MainActivity.this.startActivity(intent);
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
        android.util.Log.d("LifeCycleMethod",
                this.getClass().getSimpleName() + ": onRestart");
    }

    /**
     * onStart
     * This method is called when the view is called into the foreground.
     * Action: Open the app into the MainActivity view.
     */
    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.d("LifeCycleMethod",
                this.getClass().getSimpleName() + ": onStart");
    }

    /**
     * onResume
     * This method is called when the view returns to the foreground.
     * Action: Press OK from the AlertActivity view.
     */
    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.d("LifeCycleMethod",
                this.getClass().getSimpleName() + ": onResume");
    }

    /**
     * onPause
     * This method is called when the view is set to change.
     * Action: Press the TEST button.
     */
    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.d("LifeCycleMethod",
                this.getClass().getSimpleName() + ": onPause");
    }

    /**
     * onStop
     * This method is called when the view has changed.
     * Action: Press the TEST button and wait for AlertActivity view to display.
     */
    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.d("LifeCycleMethod",
                this.getClass().getSimpleName() + ": onStop");
    }

    /**
     * onDestroy
     * This method is called when the app is closed.
     * Action: Close the app from the MainActivity view.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("LifeCycleMethod",
                this.getClass().getSimpleName() + ": onDestroy");
    }

    /**
     * Implement spinner element select listener.
     *
     * @param parent   AdapterView
     * @param view     View
     * @param position int
     * @param id       long
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        android.util.Log.d("Spinner",
                this.getClass().getSimpleName() + ": onItemSelected -> " + parent.getItemAtPosition(position));

        this.key = parent.getItemAtPosition(position).toString();
        try {
            // If the server is up the class attribute places will be null
            // Therefore based on the selected spinner item, the place object will
            // need to be called from the server
            JSONObject place = new JSONObject();
            if (this.places == null) {
                MethodInformation mi = new MethodInformation(this, getString(R.string.url), "get", new Object[]{key});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                JSONObject jsonPlace = new JSONObject(ac.get().resultAsJson);
                place = jsonPlace.getJSONObject("result");

            } else {
                if (!this.dbInitialized) {

                    place = this.places.getJSONObject(this.key);
                } else {
                    android.util.Log.d("DB",
                            this.getClass().getSimpleName() + ": Setting " + this.key + " fields with contents from database.");
                }
            }

            Button nameButton = findViewById(R.id.nameButton);
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            TextView categoryTextView = findViewById(R.id.categoryTextView);
            TextView addressTitleTextView = findViewById(R.id.addressTitleTextView);
            TextView addressStreetTextView = findViewById(R.id.addressStreetTextView);
            TextView elevationTextView = findViewById(R.id.elevationTextView);
            TextView latitudeTextView = findViewById(R.id.latitudeTextView);
            TextView longitudeTextView = findViewById(R.id.longitudeTextView);

            if (!this.dbInitialized) {
                nameButton.setText(this.key);
                descriptionTextView.setText(place.getString("description"));
                categoryTextView.setText(place.getString("category"));
                addressTitleTextView.setText(place.getString("address-title"));
                addressStreetTextView.setText(place.getString("address-street"));
                elevationTextView.setText(place.getString("elevation"));
                latitudeTextView.setText(place.getString("latitude"));
                longitudeTextView.setText(place.getString("longitude"));

            } else {
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                // Look for item in db
                String selection = PlacesContract.PlacesEntry.COLUMN_NAME_NAME + " = ?";
                String[] selectionArgs = {this.key};

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
                String name = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_CATEGORY));
                String address_title = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_TITLE));
                String address_street = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_STREET));
                String elevation = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_ELEVATION));
                String latitude = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_LONGITUDE));

                nameButton.setText(name);
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

        } catch (JSONException e) {
            System.out.println("Setting default place " + this.key);

            try {
                JSONObject defaultPlace = new JSONObject();

                defaultPlace.put("address-title", getString(R.string.address_title_text_view));
                defaultPlace.put("address-street", getString(R.string.address_street_text_view));
                defaultPlace.put("elevation", getString(R.string.elevation_text_view));
                defaultPlace.put("latitude", getString(R.string.latitude_text_view));
                defaultPlace.put("longitude", getString(R.string.longitude_text_view));
                defaultPlace.put("name", getString(R.string.name_button));
                defaultPlace.put("image", "");
                defaultPlace.put("description", getString(R.string.description_text_view));
                defaultPlace.put("category", getString(R.string.category_text_view));

                this.places.put("ASU-Poly", defaultPlace);

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Exception: " + ex.getMessage());
        }
    }

    /**
     * Implement spinner element no select listener.
     *
     * @param parent AdapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d("Spinner",
                this.getClass().getSimpleName() + ": onNothingSelected");
    }

    /**
     * Set the keys of places.json as the elements to display in spinner.
     *
     * @param spinner Spinner
     */
    private void setSpinnerElements(Spinner spinner) {
        android.util.Log.d("Spinner",
                this.getClass().getSimpleName() + ": setSpinnerElements");

        JSONObject places;
        ArrayList<String> elements = new ArrayList<>();

        // Try initializing the app using JsonRPC server.
        try {
            MethodInformation mi = new MethodInformation(this, getString(R.string.url), "getNames", new Object[]{});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

            places = new JSONObject(ac.get().resultAsJson);
            JSONArray names = (JSONArray) places.get("result");

            for (int i = 0; i < names.length(); i++) {
                elements.add(names.getString(i));
            }

        } catch (Exception ex) {
            android.util.Log.d("JsonRPC",
                    this.getClass().getSimpleName() + ": Server down running app offline.");

            places = null;
        }

        // If the server is down places will be null.
        // If places is null run the app using local storage.
        if (places == null) {
            places = readPlacesFile();

            // Pressing the test button effectively keeps dbInitialized false
            if (!this.test) {
                this.dbInitialized = initPlacesDb(places);
            }

            // If there was an error initializing the database run the app using the file system
            if (!this.dbInitialized) {
                android.util.Log.d("File",
                        this.getClass().getSimpleName() + ": Reading places from file.");

                for (Iterator<String> it = places.keys(); it.hasNext(); ) {
                    String place = it.next();
                    elements.add(place);
                }

            } else {
                android.util.Log.d("DB",
                        this.getClass().getSimpleName() + ": Reading places from database.");

                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String selectQuery = "SELECT * FROM " + PlacesContract.PlacesEntry.TABLE_NAME;
                Cursor cursor = db.rawQuery(selectQuery, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String place = cursor.getString(cursor.getColumnIndexOrThrow(PlacesContract.PlacesEntry.COLUMN_NAME_NAME));
                    elements.add(place);
                    cursor.moveToNext();
                }

                cursor.close();
                db.close();
            }
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
        StringBuilder places = new StringBuilder();
        try {
            android.util.Log.d("File",
                    this.getClass().getSimpleName() + ": Checking internal storage...");

            FileInputStream fis = openFileInput("places.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            for (String line; (line = br.readLine()) != null; ) {
                places.append(line);
            }

        } catch (IOException e) {
            android.util.Log.d("Error",
                    this.getClass().getSimpleName() + ": File not in assets. Checking resources...");

            try {
                InputStream is = getResources()
                        .openRawResource(getResources()
                                .getIdentifier("places", "raw", getPackageName()));
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                for (String line; (line = br.readLine()) != null; ) {
                    places.append(line);
                }

            } catch (IOException ioe) {
                android.util.Log.d("Error",
                        this.getClass().getSimpleName() + ": File not in resources.");
            }
        }

        try {
            this.places = new JSONObject(places.toString());

        } catch (JSONException je) {
            android.util.Log.d("Error",
                    this.getClass().getSimpleName() + ": Could not parse JSON.");
        }

        return this.places;
    }

    /**
     * Initialize the SQLite database.
     *
     * @param places JSONObject
     * @return is the db initialized?
     */
    private boolean initPlacesDb(JSONObject places) {
        File dbFile = getBaseContext().getDatabasePath("Places.db");

        if (!dbFile.exists()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Add values to the database
            for (Iterator<String> it = places.keys(); it.hasNext(); ) {
                String place = it.next();

                try {
                    android.util.Log.d("DB",
                            this.getClass().getSimpleName() + ": Initializing database from file.");

                    // Add new rows to database
                    ContentValues values = new ContentValues();

                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_NAME, places.getJSONObject(place).getString("name"));
                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_DESCRIPTION, places.getJSONObject(place).getString("description"));
                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_CATEGORY, places.getJSONObject(place).getString("category"));
                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_TITLE, places.getJSONObject(place).getString("address-title"));
                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_ADDRESS_STREET, places.getJSONObject(place).getString("address-street"));
                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_ELEVATION, places.getJSONObject(place).getDouble("elevation"));
                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_LATITUDE, places.getJSONObject(place).getDouble("latitude"));
                    values.put(PlacesContract.PlacesEntry.COLUMN_NAME_LONGITUDE, places.getJSONObject(place).getDouble("longitude"));

                    // Add values to new row
                    long newRowId = db.insert(PlacesContract.PlacesEntry.TABLE_NAME, null, values);

                    android.util.Log.d("DB",
                            this.getClass().getSimpleName() + ": Added new row -> " + newRowId + ": " + places.getJSONObject(place).getString("name"));

                } catch (JSONException je) {
                    android.util.Log.d("Error",
                            this.getClass().getSimpleName() + ": Could not parse JSON.");

                    db.close();
                    return false;
                } catch (Exception e) {
                    android.util.Log.d("Exception",
                            this.getClass().getSimpleName() + ": Could not initialize database.");

                    db.close();
                    return false;
                }
            }

            // Close the db after all places are imported
            db.close();
        }

        return true;
    }
}