package me.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.VideoView;


import java.util.Calendar;
import java.util.Date;

import me.myapplication.Adapters.DisplayCommentariesAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Incident;

public class DisplayDetailsIncidentActivity extends AppCompatActivity {
    private Incident incident;
    private Button addCom;
    private int incidentId;
    private EditText newCom;

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

        TextView title = findViewById(R.id.titleDetail);
        TextView description = findViewById(R.id.description);
        TextView infos = findViewById(R.id.infos);
        title.setText(incident.getTitle());
        description.setText("Description de l'incident :\n"+incident.getDescription());
        TextView textDate = findViewById(R.id.date);
        Date date = incident.getDeclarationDate();
        textDate.setText("Incident posté le "+date.toString());
        TextView nameView = findViewById(R.id.username);
        String userName="";
        try {
            Cursor cursorUser = IncidentDBHelper.getSingleton().getUserCursor(incident.getReporterdID());
            userName=cursorUser.getString(cursorUser.getColumnIndexOrThrow("username"));
            nameView.setText(userName);
        } catch (IncidentDBHelper.NoRecordException e) {
            e.printStackTrace();
        }
        infos.setText("Lieu : "+displayLieu(incident.getLocationID())+"     Type : "+displayType(incident.getTypeID())+"       Statut : "+displayStatus(incident.getStatus())+"     Priorité : "+incident.getImportance().getText());
        ImageView profilPicture = findViewById(R.id.ProfileImageView);
        try {
            Cursor cursorUser = IncidentDBHelper.getSingleton().getUserCursor(incident.getReporterdID());
            cursorUser.moveToFirst();
            if (cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("ADMINISTRATEUR")) {
                profilPicture.setImageResource(R.mipmap.director);
            }
            if (cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("UTILISATEUR")) {
                profilPicture.setImageResource(R.mipmap.etudiant);
            }
            if (cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("TECHNICIEN")) {
                profilPicture.setImageResource(R.mipmap.technicien);
            }
        } catch (IncidentDBHelper.NoRecordException e) {
            e.printStackTrace();
        }

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
