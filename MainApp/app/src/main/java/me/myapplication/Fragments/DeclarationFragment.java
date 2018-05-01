package me.myapplication.Fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.R;

/**
 * Created by Aurelien on 29/04/2018.
 */

public class DeclarationFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_DB_HELPER = "dbhelper_object";

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

    private IncidentDBHelper dbHelper;

    public static DeclarationFragment newInstance(int sectionNumber, IncidentDBHelper dbHelper) {
        DeclarationFragment fragment = new DeclarationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_DB_HELPER, dbHelper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.dbHelper = (IncidentDBHelper)getArguments().getSerializable(ARG_DB_HELPER);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillTypes();
        fillDeclarations();
        this.importanceSeekBar.setMax(Importance.values().length);
        this.submitButton.setOnClickListener(new SubmissionListener());
        this.descriptionEditText.setOnFocusChangeListener(new DescriptionListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_declaration, container, false);

        this.locationSpinner = rootView.findViewById(R.id.declaration_location_spinner);
        this.typeSpinner = rootView.findViewById(R.id.declaration_type_spinner);
        this.importanceSeekBar = rootView.findViewById(R.id.declaration_importance_seekBar);
        this.submitButton = rootView.findViewById(R.id.declaration_submit_button);
        this.titleEditText = rootView.findViewById(R.id.declaration_title_input);
        this.descriptionEditText = rootView.findViewById(R.id.declaration_description_input);
        this.typeLabel = rootView.findViewById(R.id.declaration_type_label);
        this.locationLabel = rootView.findViewById(R.id.declaration_location_label);

        this.collapsibleViews = new View[]{locationLabel, typeLabel, locationSpinner,
                                           typeSpinner, titleEditText, importanceSeekBar};

        return rootView;
    }

    public void fillTypes(){
        ArrayAdapter<String> types = new ArrayAdapter<String>(getContext(),
                                                              android.R.layout.simple_spinner_item,
                                                              this.dbHelper.getTypes());
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.typeSpinner.setAdapter(types);
    }

    public void fillDeclarations(){
        ArrayAdapter<String> locations = new ArrayAdapter<String>(getContext(),
                                                              android.R.layout.simple_spinner_item,
                                                              this.dbHelper.getLocations());
        locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.locationSpinner.setAdapter(locations);
    }

    private class DescriptionListener implements EditText.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {

            if(hasFocus){
                for (View collapsibleView : collapsibleViews) {
                    collapsibleView.setVisibility(View.GONE);
                }
            }
            else{
                Logger.getAnonymousLogger().log(Level.WARNING, "lol");
                for (View collapsibleView : collapsibleViews) {
                    collapsibleView.setVisibility(View.VISIBLE);
                }
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
            ((MainActivity)getActivity()).getViewPager().setCurrentItem(2);
        }
    }

}
