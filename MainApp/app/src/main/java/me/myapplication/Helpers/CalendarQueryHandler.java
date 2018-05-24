package me.myapplication.Helpers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import me.myapplication.BuildConfig;

public class CalendarQueryHandler extends AsyncQueryHandler {
    private static final String TAG = "QueryHandler";

    // Projection arrays
    private static final String[] CALENDAR_PROJECTION = new String[]
            {
                    CalendarContract.Calendars._ID
            };

    // The indices for the projection array above.
    private static final int CALENDAR_ID_INDEX = 0;

    private static final int CALENDAR = 0;
    private static final int EVENT = 1;
    private static final int REMINDER = 2;

    private static CalendarQueryHandler queryHandler;

    // QueryHandler
    public CalendarQueryHandler(ContentResolver resolver) {
        super(resolver);
    }


    public static void insertAllEvent(Context context, Cursor cursor){

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){

            String startDateS = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
            String endDateS = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Date startDate = new Date();
            Date endDate = new Date();

            try {
                startDate = format.parse(startDateS);
                endDate = format.parse(endDateS);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long startTime = startDate.getTime();
            long endTime = endDate.getTime();

            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String lieuName = cursor.getString(cursor.getColumnIndexOrThrow("lieuName"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            insertEvent(context, startTime, endTime, title, lieuName, description);
            cursor.moveToNext();
        }

    }

    // insertEvent
    public static void insertEvent(Context context, long startTime,
                                   long endTime, String title, String lieu, String description) {
        ContentResolver resolver = context.getContentResolver();

        if (queryHandler == null)
            queryHandler = new CalendarQueryHandler(resolver);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.EVENT_LOCATION, lieu);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Calendar query start");

        queryHandler.startQuery(CALENDAR, values, CalendarContract.Calendars.CONTENT_URI,
                CALENDAR_PROJECTION, null, null, null);
    }

    // onQueryComplete
    @Override
    public void onQueryComplete(int token, Object object, Cursor cursor)
    {
        // Use the cursor to move through the returned records
        cursor.moveToFirst();

        // Get the field values
        long calendarID = cursor.getLong(CALENDAR_ID_INDEX);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Calendar query complete " + calendarID);

        ContentValues values = (ContentValues) object;
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE,
                TimeZone.getDefault().getDisplayName());

        startInsert(EVENT, null, CalendarContract.Events.CONTENT_URI, values);
    }
    // onInsertComplete
    @Override
    public void onInsertComplete(int token, Object object, Uri uri)
    {
        if (uri != null)
        {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "Insert complete " + uri.getLastPathSegment());

            switch (token)
            {
                case EVENT:
                    long eventID = Long.parseLong(uri.getLastPathSegment());
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Reminders.MINUTES, 10);
                    values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    startInsert(REMINDER, null, CalendarContract.Reminders.CONTENT_URI, values);
                    break;
            }
        }
    }


}