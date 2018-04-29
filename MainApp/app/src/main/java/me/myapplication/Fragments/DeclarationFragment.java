package me.myapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.R;

/**
 * Created by Aurelien on 29/04/2018.
 */

public class DeclarationFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_DB_HELPER = "dbhelper_object";

    private Spinner typeSpinner;
    private Spinner locationSpiner;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_declaration, container, false);
        this.locationSpiner = rootView.findViewById(R.id.declaration_location_spinner);
        this.typeSpinner = rootView.findViewById(R.id.declaration_type_spinner);
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
        this.locationSpiner.setAdapter(locations);
    }
}
