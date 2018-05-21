package me.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import me.myapplication.Adapters.DisplayCommentariesAdapter;
import me.myapplication.Adapters.VisualizationCustomAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Incident;

public class DisplayDetailsIncidentActivity extends AppCompatActivity {
    private Incident incident;
    private Button addCom;
    private EditText newCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details_incident);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        incident = (Incident)intent.getSerializableExtra("incident");

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        InputStream in = null;
        if(incident.getUrlPhoto()!=null) {
            try {
                in = new URL("file://" + incident.getUrlPhoto()).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            imageView.setImageBitmap(bitmap);
        }



        TextView title = findViewById(R.id.titleDetail);
        TextView description = findViewById(R.id.description);

        title.setText(incident.getTitle());
        description.setText(incident.getDescription());

        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DisplayCommentariesAdapter adapter = new DisplayCommentariesAdapter(this);
        recyclerView.setAdapter(adapter);

        /*
        newCom = findViewById(R.id.commentTitle);
        addCom= findViewById(R.id.sendComm);
        addCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newCom.getText().toString().equals(""))
                    return;

                IncidentDBHelper.getSingleton()
                        .insertComm(0,1,"",newCom.getText().toString());
                IncidentDBHelper.getSingleton().logComms();

                finish();
            }
        });
        */
    }


}
