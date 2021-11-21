package edu.asu.bsse.rmenese1.androidapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Alert Activity (AlertActivity.java)
 * This is the alert activity controller.
 *
 * @author Ryan Meneses
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
        String key = intent.getStringExtra("ServicesActivity");

        Button modifyButton = (Button) findViewById(R.id.modifyButton);
        modifyButton.setText(String.format("Modify\n %s", key));

        Button removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setText(String.format("Remove\n %s", key));

        // Define the Add Button Listener
        final Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, AddPlaceActivity.class);
                intent.putExtra("AddPlaceActivity", "");
                ServicesActivity.this.startActivity(intent);
            }
        });

        // Define the Modify Button Listener
        modifyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, ModifyPlaceActivity.class);
                intent.putExtra("ModifyPlaceActivity", key);
                ServicesActivity.this.startActivity(intent);
            }
        });

        // Define the Remove Button Listener
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, RemovePlaceActivity.class);
                intent.putExtra("RemovePlaceActivity", key);
                ServicesActivity.this.startActivity(intent);
            }
        });

        // Define the Add Button Listener
        final Button calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, FindDistanceActivity.class);
                intent.putExtra("FindDistanceActivity", "");
                ServicesActivity.this.startActivity(intent);
            }
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
