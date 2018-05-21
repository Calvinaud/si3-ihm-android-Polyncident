package me.myapplication.Fragments;

import android.content.Context;
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
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.R;

/**
 * A fragment representing a list of Items.
 * interface.
 */
public class DayFragment extends Fragment {

    private static String ARG_SECTION_DATE="date";

    private long date;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayFragment(){}


    public static DayFragment newInstance(Date currentdate) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SECTION_DATE,currentdate.getTime());
        Log.i("edea",""+currentdate);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("edea",""+date);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning_day, container, false);

        Date currentdate= new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        try{
            currentdate=format.parse("2018-05-19");
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        TextView text= (TextView) view.findViewById(R.id.date);
      //  text.setText(format.format(currentdate));
        text.setText(""+date);

        RecyclerView recyclerView = view.findViewById(R.id.incidentList);
        recyclerView.setHasFixedSize(true);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new DayRecyclerViewAdapter(context,2, currentdate));

        return view;
    }

}
