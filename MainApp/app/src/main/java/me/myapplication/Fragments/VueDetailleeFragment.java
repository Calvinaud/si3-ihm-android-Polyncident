package me.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.myapplication.Adapters.VisualizationCustomAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

/**
 * Created by user on 28/04/2018.
 */

public class VueDetailleeFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<Incident> incidentList;
    private IncidentDBHelper dbHelper;
    private Incident incident;


    public static VueDetailleeFragment newInstance(int sectionNumber, IncidentDBHelper dbHelper) {
        VueDetailleeFragment fragment = new VueDetailleeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_vue_detaillee, container, false);
        Intent intent = getActivity().getIntent();
        incident = (Incident)intent.getSerializableExtra("incident");
        TextView title = rootView.findViewById(R.id.titleDetail);
        TextView description = rootView.findViewById(R.id.description);

        title.setText(incident.getTitle());
        description.setText(incident.getDescription());

        return rootView;
    }


}