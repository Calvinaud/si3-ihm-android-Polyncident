package me.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.myapplication.Adapters.DayRecyclerViewAdapter;
import me.myapplication.DeclarationActivity;
import me.myapplication.DisplayDetailsIncidentActivity;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Listener.OnSwipeTouchListener;
import me.myapplication.R;

/**
 * A fragment representing a list of Items.
 * interface.
 */
public class DayFragment extends Fragment {

    private long DATE_LONG;
    private static final String ARG="DATE_LONG";

    private Context context;
    private Date date;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayFragment(){}

    public static DayFragment newInstance(long title) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putLong(ARG,title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_planning_day, container, false);

       SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");

        TextView text= (TextView) view.findViewById(R.id.date);
        RecyclerView recyclerView = view.findViewById(R.id.incidentList);
        recyclerView.setHasFixedSize(true);

        context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        if (getArguments() != null) {
            Bundle args = getArguments();
            if (args.containsKey(ARG)) {

                date=new Date(args.getLong(ARG));
                text.setText("" + format.format(date));
                recyclerView.setAdapter(new DayRecyclerViewAdapter(context,1, date));
            }
        }

        return view;
    }



}
