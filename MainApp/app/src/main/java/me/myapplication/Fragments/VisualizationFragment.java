package me.myapplication.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.myapplication.Adapters.VisualizationCustomAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

/**
 * Created by user on 28/04/2018.
 */

public class VisualizationFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_DB_HELPER = "dbhelper_object";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<Incident> incidentList;
    private IncidentDBHelper dbHelper;


    public static VisualizationFragment newInstance(int sectionNumber, IncidentDBHelper dbHelper) {
        VisualizationFragment fragment = new VisualizationFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visualization, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        incidentList= new ArrayList<>();
        this.dbHelper.insertIncident(1,2,2,3,"title","desc");
        adapter= new VisualizationCustomAdapter(getContext(), dbHelper);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}
