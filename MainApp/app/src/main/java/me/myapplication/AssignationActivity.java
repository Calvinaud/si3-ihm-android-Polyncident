package me.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Incident;

public class AssignationActivity extends AppCompatActivity {

    protected static TextView startTime;
    protected static TextView endTime;
    protected static TextView date;
    protected static TextView titre;
    protected static boolean isStartTime = true;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);


        setContentView(R.layout.activity_assignation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String titrename = "Affecter une tâche à "+ IncidentDBHelper.getSingleton().getUsername(userId);
        titre = (TextView) findViewById(R.id.titre);
        titre.setText("Affecter une tâche à Bob");


        startTime = (TextView)findViewById(R.id.selected_start_time);
        Button displayTimeButtonstart = (Button) findViewById(R.id.select_start_time);
        displayTimeButtonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTime = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getFragmentManager(), "Choisir une heure de fin");
            }
        });

        endTime = (TextView)findViewById(R.id.selected_end_time);
        Button displayTimeButtonEnd = (Button) findViewById(R.id.select_end_time);
        displayTimeButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTime = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getFragmentManager(), "Choisir une heure de fin");
            }
        });

        date = (TextView)findViewById(R.id.selected_date);
        Button displayTimeButton = (Button)findViewById(R.id.select_date);
        displayTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getFragmentManager(), "Select date");
            }
        });


        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startdate ="2018-05-25 03:00:00";
                String enddate= "2018-05-25 05:00:00";

                IncidentDBHelper.getSingleton().updateStatus(2, 1);

                IncidentDBHelper.getSingleton().insertAssignation(userId,2,startdate,enddate);
                Intent intent = new Intent(getApplicationContext(), AdminPlanningActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            date.setText("Selected date: " + String.valueOf(year) + " - " + String.valueOf(month) + " - " + String.valueOf(day));
        }
    }

    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            if(!isStartTime){
                startTime.setText("Heure choisit: " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
            }
            else {
                endTime.setText("Heure choisit: " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
            }

        }
    }
}
