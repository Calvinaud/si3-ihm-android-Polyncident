package me.myapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class PlanningFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_DB_HELPER = "dbhelper_object";

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


    public static PlanningFragment newInstance(int sectionNumber, IncidentDBHelper dbHelper) {
        PlanningFragment fragment = new PlanningFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.dbHelper = ((MainActivity)getContext()).getDBHelper();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_planning, container, false);

        return rootView;
    }

}
