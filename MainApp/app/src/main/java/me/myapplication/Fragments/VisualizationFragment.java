package me.myapplication.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import me.myapplication.Adapters.VisualizationCustomAdapter;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

/**
 * Created by user on 28/04/2018.
 */

public class VisualizationFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<Incident> incidentList;



    public VisualizationFragment() {
    }

    public static VisualizationFragment newInstance(int sectionNumber) {
        VisualizationFragment fragment = new VisualizationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
        incidentList.add(new Incident(1,1,1, Importance.CRITICAL,"OK","0"));
        adapter= new VisualizationCustomAdapter(incidentList,getContext());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}
