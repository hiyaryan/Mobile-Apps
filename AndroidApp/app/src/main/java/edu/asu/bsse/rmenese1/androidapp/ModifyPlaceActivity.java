package edu.asu.bsse.rmenese1.androidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyPlaceActivity extends AppCompatActivity {
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
        String key = intent.getStringExtra("ModifyPlaceActivity");

        android.util.Log.d("LifeCycleMethod", this.getClass().getSimpleName() + ": " + "onCreate"
                + " -> " + key);
    }
}
