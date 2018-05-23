package me.myapplication;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.myapplication.Fragments.DayFragment;
import me.myapplication.Listener.OnSwipeTouchListener;


public class PlanningActivity extends AppCompatActivity {

    private View view;
    Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String dateToday = "No date";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = df.format(Calendar.getInstance().getTime());


        TextView todaydate=(TextView) findViewById(R.id.todayDate);
        todaydate.setText("Nous somme le: "+dateToday);

        currentDate= new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        try{
            currentDate=format.parse("2018-05-19");
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, DayFragment.newInstance(currentDate.getTime()));
        ft.commit();

        view = (View) findViewById(R.id.fullView);
        view.setOnTouchListener(imageViewSwiped);
    }

    View.OnTouchListener imageViewSwiped = new OnSwipeTouchListener()
    {
        @Override
        public boolean onSwipeRight()
        {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            currentDate=new Date(currentDate.getTime()+86400000);
            ft.replace(R.id.fragment, DayFragment.newInstance(currentDate.getTime()));
            ft.commit();
            return true;
        }

        public boolean onSwipeLeft()
        {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            currentDate=new Date(currentDate.getTime()+3*86400000);
            ft.replace(R.id.fragment, DayFragment.newInstance(currentDate.getTime()));
            ft.commit();

            return true;
        }
    };

}
