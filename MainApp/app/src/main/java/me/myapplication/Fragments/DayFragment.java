package me.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import me.myapplication.Adapters.DayRecyclerViewAdapter;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.R;

/**
 * A fragment representing a list of Items.
 * interface.
 */
public class DayFragment extends Fragment {

    private static Calendar dateday;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayFragment() {}

    public static DayFragment newInstance() {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_planning_day, container, false);

        TextView text= (TextView) view.findViewById(R.id.date);

        text.setText("date");
        RecyclerView recyclerView = view.findViewById(R.id.incidentList);
        recyclerView.setHasFixedSize(true);

            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new DayRecyclerViewAdapter());

        return view;
    }

}
