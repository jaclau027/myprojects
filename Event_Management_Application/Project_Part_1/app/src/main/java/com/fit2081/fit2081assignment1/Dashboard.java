package com.fit2081.fit2081assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void newEventCategoryButtonClick(View view) {
        // update the UI using retrieved values
        Intent intent = new Intent(this, NewEventCategory.class);

        // finally launch the activity using startActivity method
        startActivity(intent);
    }

    public void addEventButtonClick(View view) {
        // update the UI using retrieved values
        Intent intent = new Intent(this, AddEvent.class);

        // finally launch the activity using startActivity method
        startActivity(intent);
    }


}