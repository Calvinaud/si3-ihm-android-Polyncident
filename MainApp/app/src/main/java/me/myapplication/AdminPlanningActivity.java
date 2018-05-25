 package me.myapplication;

        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.RectF;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.util.TypedValue;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.alamkanak.weekview.DateTimeInterpreter;
        import com.alamkanak.weekview.MonthLoader;
        import com.alamkanak.weekview.WeekView;
        import com.alamkanak.weekview.WeekViewEvent;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Locale;

        import me.myapplication.Helpers.CalendarQueryHandler;
        import me.myapplication.Helpers.IncidentDBHelper;


 public class AdminPlanningActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private int userId;
     private Button affectation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_planning);
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        affectation = findViewById(R.id.synchroniser);
        affectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AssignationActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_planning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        Cursor cursor = IncidentDBHelper.getSingleton().getIncidentMonth(userId, newMonth, newYear);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            events.add(addEvent(cursor, newYear, newMonth));
            cursor.moveToNext();
        }

        return events;
    }

     private WeekViewEvent addEvent(Cursor cursor, int newYear, int newMonth){

         String fullStartDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
         String fullendDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));

         Log.i("full start time",fullStartDate);
         Log.i("full end time", fullendDate);


         String[] startTimeS = fullStartDate.split(" ");
         String[] startDating = startTimeS[0].split("-");
         String[] startTiming = startTimeS[1].split(":");

         String[] endTimeS = fullendDate.split(" ");
         String[] endDating = endTimeS[0].split("-");
         String[] endTiming = endTimeS[1].split(":");

         Calendar startTime = Calendar.getInstance();
         startTime.set(Calendar.DAY_OF_MONTH, (int)(Integer.parseInt(startDating[2])));
         startTime.set(Calendar.HOUR_OF_DAY, (int)(Integer.parseInt(startTiming[0])));
         startTime.set(Calendar.MINUTE, (int)(Integer.parseInt(startTiming[1])));
         startTime.set(Calendar.MONTH, newMonth-1);
         startTime.set(Calendar.YEAR, newYear);
         Calendar endTime = (Calendar) startTime.clone();
         endTime.set(Calendar.HOUR_OF_DAY, (int)(Integer.parseInt(endTiming[0])));

         int id = cursor.getInt(cursor.getColumnIndexOrThrow("incidentId"));
         String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
         String lieuName = cursor.getString(cursor.getColumnIndexOrThrow("lieuName"));
         String typeName = cursor.getString(cursor.getColumnIndexOrThrow("typeName"));



         return new WeekViewEvent(id, getEventTitle(title, lieuName, typeName), startTime, endTime);
     }


    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

     protected String getEventTitle(String titre, String lieu, String typeName) {
         return "Titre: "+titre+"\n Lieu: "+lieu+"\n Type: "+typeName;
     }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Intent intent = new Intent(getApplicationContext(), DisplayDetailsIncidentActivity.class);
        intent.removeExtra("incidentId");
        intent.putExtra("incidentId",event.getId());
        startActivity(intent);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        //Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
       // Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }
}