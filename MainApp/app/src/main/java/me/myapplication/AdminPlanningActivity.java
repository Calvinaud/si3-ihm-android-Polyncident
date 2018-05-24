package me.myapplication;

import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

public class AdminPlanningActivity extends AppCompatActivity {

    private int userId;
    WeekView mWeekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_planning);

        mWeekView = (WeekView) findViewById(R.id.weekView);

        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return new ArrayList<>();
            }
        };

       // mWeekView.setOnEventClickListener(mMonthLoaderListener);
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        //mWeekView.setEventLongPressListener(mEventLongPressListener);
        mWeekView.setNumberOfVisibleDays(5);
    }

}
