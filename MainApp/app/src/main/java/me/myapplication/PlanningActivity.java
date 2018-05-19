package me.myapplication;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.myapplication.Fragments.DayFragment;


public class PlanningActivity extends AppCompatActivity {

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




        Calendar date=new GregorianCalendar();
        date.set(2018,12,12);
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, DayFragment.newInstance(0,date));
        ft.commit();



//        DayFragment fragment=(DayFragment) getFragmentManager().findFragmentById(R.id.fragment);

        /*RecyclerView recyclerView = findViewById(R.id.dayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ListDayRecyclerViewAdapter adapter = new ListDayRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);*/
    }

}
