package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;

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
public class AlertActivity extends AppCompatActivity {
    /**
     * onCreate
     * This method initializes the view.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("AlertActivity");

        String hello = extras.getString("hello");
        boolean test = !extras.getString("test").equals("true");
        String title;
        if (test) {
            title = "File Mode";
        } else {
            title = "Database Mode";
        }

        new AlertDialog.Builder(AlertActivity.this)
                .setTitle(title)
                .setMessage(hello)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent intent1 = new Intent(AlertActivity.this, MainActivity.class);
                    intent1.putExtra("test", String.valueOf(test));
                    AlertActivity.this.startActivity(intent1);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * onRestart
     * This method is called when the app is opened from the background.
     * Action: From the AlertActivity view send the app into the background and reopen.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onRestart");
    }

    /**
     * onStart
     * This method is called when the view is called into the foreground.
     * Action: Press TEST and wait for AlertActivity view to enter the foreground.
     */
    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onStart");
    }

    /**
     * onResume
     * This method is called when the view returns to the foreground.
     * Action: Press TEST from the MainActivity view.
     */
    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onResume");
    }

    /**
     * onPause
     * This method is called when the view is set to change.
     * Action: Press the OK button.
     */
    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onPause");
    }

    /**
     * onStop
     * This method is called when the view has changed.
     * Action: Press the OK button and wait for MainActivity view to display.
     */
    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onStop");
    }

    /**
     * onDestroy
     * This method is called when the app is closed.
     * Action: Close the app from the AlertActivity view.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onDestroy");
    }
}
