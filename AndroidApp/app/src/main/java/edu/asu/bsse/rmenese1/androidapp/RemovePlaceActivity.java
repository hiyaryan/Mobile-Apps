package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Remove Place Activity (RemovePlaceActivity.java)
 * This activity allows the user to remove an existing place.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 21, 2021
 */
public class RemovePlaceActivity extends AppCompatActivity {
    private JSONObject places;

    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("RemovePlaceActivity");

        String key = extras.getString("key");
        String places = extras.getString("places");

        try {
            // If places is null the app is connected to the JsonRPC server
            if (places != null) {
                this.places = new JSONObject(places);
            }

        } catch (JSONException je) {
            android.util.Log.d("Error", this.getClass().getSimpleName() + ": "
                    + "Could not parse JSON.");
        }

        new AlertDialog.Builder(RemovePlaceActivity.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete " + key + "?")
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    android.util.Log.d("Button", this.getClass().getSimpleName() + ": " + "CANCEL");

                    Intent intent1 = new Intent(RemovePlaceActivity.this, MainActivity.class);
                    RemovePlaceActivity.this.startActivity(intent1);
                })
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    android.util.Log.d("Button", this.getClass().getSimpleName() + ": " + "OK");

                    // If places is null the app is connected to the JsonRPC server
                    if (places != null) {
                        removePlaceInPlaces(key);

                    } else {
                        try {
                            // Send place to server
                            MethodInformation mi = new MethodInformation(null, getString(R.string.url), "remove", new Object[]{key});
                            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                            // JSONObject result = new JSONObject(ac.get().resultAsJson);
                            // System.out.println(result);

                            // Save place to places on server
                            mi = new MethodInformation(null, getString(R.string.url), "saveToJsonFile", new Object[]{});
                            ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                            // result = new JSONObject(ac.get().resultAsJson);
                            // System.out.println(result);

                        } catch (Exception ex) {
                            android.util.Log.w(this.getClass().getSimpleName(),
                                    "Exception: "+ ex.getMessage());
                        }
                    }

                    Intent intent1 = new Intent(RemovePlaceActivity.this, MainActivity.class);
                    RemovePlaceActivity.this.startActivity(intent1);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Saves the new place to places.json. Returns true if saved.
     *
     * @param key String
     */
    private void removePlaceInPlaces(String key) {
        android.util.Log.d("CRUD", this.getClass().getSimpleName() + ": "
                + "Delete -> " + key);

        this.places.remove(key);
        writePlacesToFile(this.places);
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
}
