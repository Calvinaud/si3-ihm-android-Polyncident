package me.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Importance;

public class DeclarationActivity extends Activity {

    private IncidentDBHelper dbHelper;

    //GUI components
    private Spinner typeSpinner;
    private Spinner locationSpinner;
    private SeekBar importanceSeekBar;
    private Button submitButton;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextView typeLabel;
    private TextView locationLabel;

    private View[] collapsibleViews;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_declaration);

        this.dbHelper = new IncidentDBHelper(this);
        try{
            dbHelper.openDataBase();
        }catch (Exception e){
            Logger.getAnonymousLogger().severe(e.toString());
        }

        initElementReferences();
        initElementListeners();

        fillTypes();
        fillDeclarations();
    }

    private void initElementReferences(){
        this.locationSpinner = findViewById(R.id.declaration_location_spinner);
        this.typeSpinner = findViewById(R.id.declaration_type_spinner);
        this.importanceSeekBar = findViewById(R.id.declaration_importance_seekBar);
        this.submitButton = findViewById(R.id.declaration_submit_button);
        this.titleEditText = findViewById(R.id.declaration_title_input);
        this.descriptionEditText = findViewById(R.id.declaration_description_input);
        this.typeLabel = findViewById(R.id.declaration_type_label);
        this.locationLabel = findViewById(R.id.declaration_location_label);

        this.collapsibleViews = new View[]{locationLabel, typeLabel, locationSpinner,
                typeSpinner, titleEditText, importanceSeekBar};
    }

    private void initElementListeners(){
        this.importanceSeekBar.setMax(Importance.values().length);
        this.submitButton.setOnClickListener(new me.myapplication.DeclarationActivity.SubmissionListener());
        this.descriptionEditText.setOnFocusChangeListener(new me.myapplication.DeclarationActivity.DescriptionListener());
    }

    private void fillTypes(){
        ArrayAdapter<String> types = new ArrayAdapter<String>(getBaseContext(),
                                                         android.R.layout.simple_spinner_item,
                                                         this.dbHelper.getTypes());
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.typeSpinner.setAdapter(types);
    }

    private void fillDeclarations(){
        ArrayAdapter<String> locations = new ArrayAdapter<String>(getBaseContext(),
                                                             android.R.layout.simple_spinner_item,
                                                             this.dbHelper.getLocations());
        locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.locationSpinner.setAdapter(locations);
    }

    private class DescriptionListener implements EditText.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {

            if(hasFocus){
                for (View collapsibleView : collapsibleViews)
                    collapsibleView.setVisibility(View.GONE);
            }
            else{
                Logger.getAnonymousLogger().log(Level.WARNING, "lol");

                for (View collapsibleView : collapsibleViews)
                    collapsibleView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class SubmissionListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(titleEditText.getText().toString().equals("")
               || descriptionEditText.getText().toString().equals(""))
                return;

            dbHelper.insertIncident(0, locationSpinner.getSelectedItemPosition()+1,
                    typeSpinner.getSelectedItemPosition()+1,importanceSeekBar.getProgress(),
                    titleEditText.getText().toString(), descriptionEditText.getText().toString()
            );
            dbHelper.logIncidents();

            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
