package me.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.VideoView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.myapplication.Adapters.DisplayCommentariesAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Incident;

public class DisplayDetailsIncidentActivity extends AppCompatActivity {
    private Incident incident;
    private Button addCom;
    private int incidentId;
    private EditText newCom;

    private TextView title;
    private TextView description;
    private TextView infos;
    private TextView dateText;
    private TextView name;
    private ImageView profilePicture;
    private ImageView status;
    private TextView locationView;
    private TextView typeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details_incident);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        incidentId = intent.getIntExtra("incidentId", 1);
        Log.i("incidentId:",""+intent.getExtras());

        incident = IncidentDBHelper.getSingleton().getIncidentById(incidentId);
        VideoView videoView = findViewById(R.id.VideoView);
        ImageView imageView = (ImageView) findViewById(R.id.incidentImageView);
        if(incident.getImg() != null && incident.getImg().length>0) {
            byte[] img = incident.getImg();
            Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
            imageView.setImageBitmap(bm);
            imageView.setVisibility(View.VISIBLE);
        }
        else imageView.setVisibility(View.GONE);

        title = findViewById(R.id.titleDetail);
        title.setText(incident.getTitle());

        description = findViewById(R.id.description);
        description.setText(incident.getDescription());


        dateText = findViewById(R.id.date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        dateText.setText(format.format(incident.getDeclarationDate()));



        name = findViewById(R.id.username);
        name.setText(
                IncidentDBHelper.getSingleton().getUsername(incident.getReporterdID())
        );


        locationView = findViewById(R.id.location);
        typeView = findViewById(R.id.type);

        locationView.setText(
                IncidentDBHelper.getSingleton().getLocationName(incident.getLocationID())
        );

        typeView.setText(
                IncidentDBHelper.getSingleton().getTypeName(incident.getTypeID())
        );

        profilePicture = findViewById(R.id.ProfileImageView);

        profilePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                profileIntent.putExtra("userId",incident.getReporterdID());

                startActivity(profileIntent);

            }



        });

        status = (ImageView)findViewById(R.id.statusView);
        switch (incident.getStatus()){
            case 0:
                status.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_watch_later_black_24dp));
                break;
            case 1:
                status.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_sync_black_24dp));
                break;
            case 2:
                status.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_check_circle_black_24dp));
                break;
        }

        try {
            Cursor cursorUser = IncidentDBHelper.getSingleton().getUserCursor(incident.getReporterdID());
            cursorUser.moveToFirst();
            if (cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("ADMINISTRATEUR")) {
                profilePicture.setImageResource(R.mipmap.director);
            }
            if (cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("UTILISATEUR")) {
                profilePicture.setImageResource(R.mipmap.etudiant);
            }
            if (cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("TECHNICIEN")) {
                profilePicture.setImageResource(R.mipmap.technicien);
            }
        } catch (IncidentDBHelper.NoRecordException e) {
            e.printStackTrace();
        }

        initCommentSection();

    }

    private void initCommentSection(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        incidentId = incident.getIncidentID();
        DisplayCommentariesAdapter adapter = null;
        adapter = new DisplayCommentariesAdapter(this, incidentId);
        recyclerView.setAdapter(adapter);

        newCom = findViewById(R.id.commentTitle);
        addCom = findViewById(R.id.sendComm);
        addCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newCom.getText().toString().equals(""))
                    return;

                IncidentDBHelper.getSingleton()
                        .insertComm(0, incident.getIncidentID(), Calendar.getInstance().getTime(), newCom.getText().toString());
                IncidentDBHelper.getSingleton().logComms();
                finish();
                startActivity(getIntent());
            }
        });
    }


    public String displayStatus(int n){
        switch(n){
            case 0 : return "Pas vu";
            case 1 : return "En cours";
            case 2 : return "Traité";
        }
        return "";
    }

    public String displayType(int n){
        switch(n){
            case 0 : return "Fournitures";
            case 1 : return "Matériel cassé";
            case 2 : return "Autres";
        }
        return "";
    }

    public String displayLieu(int n){
        switch(n){
            case 0 : return "E130";
            case 1 : return "E131";
            case 2 : return "E130";
            case 3 : return "0108";
            case 4 : return "0109";
            case 5 : return "0110";
            case 6 : return "Parking";

        }
        return "";
    }

}
