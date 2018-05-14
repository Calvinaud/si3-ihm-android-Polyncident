package me.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import me.myapplication.Adapters.DisplayCommentariesAdapter;
import me.myapplication.Adapters.VisualizationCustomAdapter;
import me.myapplication.Models.Incident;

public class DisplayDetailsIncidentActivity extends AppCompatActivity {
    private Incident incident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details_incident);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        incident = (Incident)intent.getSerializableExtra("incident");
        TextView title = findViewById(R.id.titleDetail);
        TextView description = findViewById(R.id.description);

        title.setText(incident.getTitle());
        description.setText(incident.getDescription());

        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DisplayCommentariesAdapter adapter = new DisplayCommentariesAdapter(this);
        recyclerView.setAdapter(adapter);
    }

}
