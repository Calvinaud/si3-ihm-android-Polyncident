package me.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

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
    private int userId;
    private Spinner impSpin;
    private Spinner typeSpin;

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
        adapter = new VisualizationCustomAdapter(getContext(), this.myIncidents, ((MainActivity) getContext()).getUserId(),-1,-1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visualization, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));



        impSpin= rootView.findViewById(R.id.prio);
        fillImportance(impSpin);

        typeSpin= rootView.findViewById(R.id.type);
        fillTypes(typeSpin);

        adapter = new VisualizationCustomAdapter(getContext(), this.myIncidents, ((MainActivity) getContext()).getUserId(),-1,-1);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void fillImportance(final Spinner impSpin){
        List<String> listImp = new ArrayList<>();
        listImp.add("Filtrer par importance");
        listImp.add("Négligeable");
        listImp.add("Mineur");
        listImp.add("Forte");
        listImp.add("Urgente");
        listImp.add("Tous");
        ArrayAdapter<String> importances = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item,
                listImp);
        importances.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impSpin.setAdapter(importances);
        impSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(i!=0) {
                    if(i==5) i=-1;
                    adapter = new VisualizationCustomAdapter(getContext(), myIncidents,
                            ((MainActivity) getContext()).getUserId(),i,-1);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void fillTypes(final Spinner impSpin){
        List<String> listImp = new ArrayList<>();
        listImp.add("Filtrer par types");
        listImp.add("Fournitures");
        listImp.add("Matériel cassé");
        listImp.add("Autres");
        listImp.add("Tous");
        ArrayAdapter<String> importances = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item,
                listImp);
        importances.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impSpin.setAdapter(importances);
        impSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    if(i==4) i=-1;
                    impSpin.setSelection(0);
                    adapter = new VisualizationCustomAdapter(getContext(), myIncidents,
                            ((MainActivity) getContext()).getUserId(),-1,i);
                    recyclerView.setAdapter(adapter);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
