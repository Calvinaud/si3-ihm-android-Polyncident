package me.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.myapplication.Models.Incident;

/**
 * Created by user on 13/05/2018.
 */

public class DisplayIncidentActivity extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;
    private Incident incident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

    }



}
