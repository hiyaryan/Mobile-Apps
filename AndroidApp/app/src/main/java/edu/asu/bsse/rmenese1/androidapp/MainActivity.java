package edu.asu.bsse.rmenese1.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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
                // Code here executes on main thread after user presses button
            }
        });
    }
}