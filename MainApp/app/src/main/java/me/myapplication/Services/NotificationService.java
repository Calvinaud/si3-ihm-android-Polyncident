package me.myapplication.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.DisplayDetailsIncidentActivity;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

public class NotificationService extends Service {

    NotificationCompat.Builder notification;
    private static final int uniqueID = 1234;
    public ArrayList<Integer> subscrbedTypesId = new ArrayList<>();

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notification= new NotificationCompat.Builder(this, "1234");
        notification.setAutoCancel(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        this.subscrbedTypesId = (ArrayList<Integer>) intent.getSerializableExtra("subs");

        final IncidentDBHelper dbHelper = IncidentDBHelper.getSingleton();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                int prev = dbHelper.getIncidentNumber();
                while(true){
                        synchronized (this){
                            try{
                                wait(5000);
                                Cursor cursor = dbHelper.getLastIncidentCursor();
                                if(prev < dbHelper.getIncidentNumber()){
                                    if(getSubscribed().contains(cursor.getInt(cursor.getColumnIndexOrThrow("typeId")))) {
                                        sendNotif(cursor.getString(cursor.getColumnIndexOrThrow("title")), cursor.getString(cursor.getColumnIndexOrThrow("description")), cursor);
                                    }
                                }
                                prev=dbHelper.getIncidentNumber();
                            }catch (Exception ignored){}
                        }
                    }
                }
        };
        Thread notifThread = new Thread(r);
        notifThread.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public List<Integer> getSubscribed(){
        return this.subscrbedTypesId;
    }

    public void sendNotif(String title, String desc, Cursor cursor){

        //Importance importance; A FAIRE
        String urgence = cursor.getString(cursor.getColumnIndex("importance")); //temp
        int reporterId = cursor.getInt(cursor.getColumnIndexOrThrow("reporterId"));
        int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("locationId"));
        int typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeId"));
        String url = cursor.getString(cursor.getColumnIndexOrThrow("urlPhoto"));

        Incident incident=new Incident(reporterId,locationId,typeId, Importance.MINOR,title,desc,url);


        notification.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        notification.setTicker("ok");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Un incident a été ajouté : "+title);
        notification.setContentText(desc);

        Intent intent = new Intent(this, DisplayDetailsIncidentActivity.class);
        intent.putExtra("incident", incident);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueID,notification.build());
    }
}
