package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Services Activity (ServicesActivity.java)
 * This is the services activity controller that branches the user to the various services
 * offered by the app.
 *
 * Copyright 2021 Ryan Meneses. The SER423 Instructional Team and Arizona State University
 * have the right to build and evaluate this software package for the purposes of grading
 * and program assessment.
 *
 * @author Ryan Meneses     mailto: rmenese1@asu.edu
 * @version 1.0
 * @since November 19, 2021
 */
public class ServicesActivity extends AppCompatActivity {
    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("ServicesActivity");

        String key = extras.getString("key");

        Button modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setText(String.format("Modify\n %s", key));

        Button removeButton = findViewById(R.id.removeButton);
        removeButton.setText(String.format("Remove\n %s", key));

        // Define the Add Button Listener
        final Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(ServicesActivity.this, AddPlaceActivity.class);
            intent1.putExtra("AddPlaceActivity", extras);
            ServicesActivity.this.startActivity(intent1);
        });

        // Define the Modify Button Listener
        modifyButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(ServicesActivity.this, ModifyPlaceActivity.class);
            intent2.putExtra("ModifyPlaceActivity", extras);
            ServicesActivity.this.startActivity(intent2);
        });

        // Define the Remove Button Listener
        removeButton.setOnClickListener(v -> {
            Intent intent3 = new Intent(ServicesActivity.this, RemovePlaceActivity.class);
            intent3.putExtra("RemovePlaceActivity", extras);
            ServicesActivity.this.startActivity(intent3);
        });

        // Define the Calc Button Listener
        final Button calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(v -> {
            Intent intent4 = new Intent(ServicesActivity.this, FindDistanceActivity.class);
            intent4.putExtra("FindDistanceActivity", extras);
            ServicesActivity.this.startActivity(intent4);
        });
    }

    /**
     * onRestart
     * This method is called when the app is opened from the background.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onRestart");
    }

    /**
     * onStart
     * This method is called when the view is called into the foreground.
     */
    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onStart");
    }

    /**
     * onResume
     * This method is called when the view returns to the foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onResume");
    }

    /**
     * onPause
     * This method is called when the view is set to change.
     */
    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onPause");
    }

    /**
     * onStop
     * This method is called when the view has changed.
     */
    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onStop");
    }

    /**
     * onDestroy
     * This method is called when the app is closed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onDestroy");
    }
}
