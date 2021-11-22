package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
            this.places = new JSONObject(places);

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

                    removePlaceInPlaces(key);

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
