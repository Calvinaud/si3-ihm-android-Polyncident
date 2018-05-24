package me.myapplication.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import me.myapplication.Adapters.VisualizationCustomAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.R;

/**
 * Created by user on 28/04/2018.
 */

public class VisualizationFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private IncidentDBHelper dbHelper;
    private boolean myIncidents;


    public static VisualizationFragment newInstance(int sectionNumber) {
        VisualizationFragment fragment = new VisualizationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMyIncidents(boolean bool) {
        this.myIncidents= bool;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbHelper = ((MainActivity) getContext()).getDBHelper();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new VisualizationCustomAdapter(getContext(), this.myIncidents, ((MainActivity) getContext()).getUserId());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visualization, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new VisualizationCustomAdapter(getContext(), this.myIncidents, ((MainActivity) getContext()).getUserId());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}
