package me.myapplication;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.myapplication.Fragments.DeclarationFragment;
import me.myapplication.Fragments.PlanningFragment;
import me.myapplication.Fragments.VisualizationFragment;
import me.myapplication.Fragments.VueDetailleeFragment;
import me.myapplication.Models.Incident;

/**
 * Created by user on 13/05/2018.
 */

public class DisplayIncidentActivity extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;
    private Incident incident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

    }



}
