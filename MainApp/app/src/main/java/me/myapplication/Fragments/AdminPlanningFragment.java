package me.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;

import me.myapplication.R;

/**
 * A simple {@link Fragment}
 */
public class AdminPlanningFragment extends Fragment {

    private View mWeekView;

    public AdminPlanningFragment() {
        // Required empty public constructor
    }

    public static AdminPlanningFragment newInstance() {
        AdminPlanningFragment fragment = new AdminPlanningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_planning, container, false);
        this.mWeekView = (WeekView) rootView.findViewById(R.id.weekView);



        return rootView;
    }

}
