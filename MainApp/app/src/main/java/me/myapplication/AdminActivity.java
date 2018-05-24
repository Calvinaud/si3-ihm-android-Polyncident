package me.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import me.myapplication.Adapters.TechosRecyclerViewAdapter;
import me.myapplication.Adapters.VisualizationCustomAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Incident;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new TechosRecyclerViewAdapter(this, new TechosRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cursor cursor) {

                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                Intent intent = new Intent(getApplicationContext(), AdminPlanningActivity.class);
                intent.removeExtra("userId");
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        }));
    }

}

/*recycler.setAdapter(new ContentAdapter(items, new ContentAdapter.OnItemClickListener() {
    @Override public void onItemClick(ContentItem item) {
        Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
    }
}));*/