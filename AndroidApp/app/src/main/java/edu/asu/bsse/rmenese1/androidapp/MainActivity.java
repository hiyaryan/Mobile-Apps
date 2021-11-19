package edu.asu.bsse.rmenese1.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main Activity (MainActivity.java)
 * This is the main activity controller.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 19, 2021
 */
public class MainActivity extends AppCompatActivity {

    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        final TextView addressStreetTextView = findViewById(R.id.addressStreetTextView);
        addressStreetTextView.setMovementMethod(new ScrollingMovementMethod());

        final Button nameButton = findViewById(R.id.nameButton);
        nameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

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
}