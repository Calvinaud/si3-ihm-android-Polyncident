package me.myapplication.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.BroadcastReceivers.NotificationActionReceiver;
import me.myapplication.DisplayDetailsIncidentActivity;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class NotificationService extends Service {

    NotificationCompat.Builder notification;
    private static final int uniqueID = 1234;

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

        final int userId = intent.getIntExtra("userId", 0);

        final IncidentDBHelper dbHelper = IncidentDBHelper.getSingleton();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                int prev = dbHelper.getIncidentNumber();
                while(true){
                        synchronized (this){
                            try{
                                wait(5000);
                                ArrayList<Integer> subscrbedTypesId = new ArrayList<>();

                                SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);

                                boolean enabled = sharedPreferences.getBoolean("Enabled", true);
                                Logger.getAnonymousLogger().log(Level.WARNING,"enabled :"+enabled);

                                boolean enabled1 = sharedPreferences.getBoolean("Furniture", true);
                                if(enabled1){subscrbedTypesId.add(1);}
                                boolean enabled2 = sharedPreferences.getBoolean("Mat", true);
                                if(enabled2){subscrbedTypesId.add(2);}
                                boolean enabled3 = sharedPreferences.getBoolean("Others", true);
                                if(enabled3){subscrbedTypesId.add(3);}

                                Logger.getAnonymousLogger().log(Level.WARNING,"OKAY :"+subscrbedTypesId.get(0));


                                Cursor cursor = dbHelper.getLastIncidentCursor();
                                if(prev < dbHelper.getIncidentNumber() && cursor.getInt(cursor.getColumnIndexOrThrow("reporterId")) != userId && enabled){
                                    if(subscrbedTypesId.contains(cursor.getInt(cursor.getColumnIndexOrThrow("typeId")))) {
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

    public void sendNotif(String title, String desc, Cursor cursor){

        //Importance importance; A FAIRE
        int incidentId = cursor.getInt(cursor.getColumnIndex("incidentId"));
        String urgence = cursor.getString(cursor.getColumnIndex("importance")); //temp
        int reporterId = cursor.getInt(cursor.getColumnIndexOrThrow("reporterId"));
        int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("locationId"));
        int typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeId"));
        byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow("img"));
        String sdate = cursor.getString(cursor.getColumnIndexOrThrow("declarationDate"));

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = dateFormat.parse(sdate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        Incident incident=new Incident(incidentId,reporterId,locationId,typeId, Importance.MINOR,title,desc,img, date);

        Intent intentActionSub = new Intent(this,NotificationActionReceiver.class);
        intentActionSub.putExtra("action","S'ABONNER");
        intentActionSub.putExtra("incidentId", incidentId );
        PendingIntent pendingIntentSub = PendingIntent.getBroadcast(this,1,intentActionSub,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.addAction(R.drawable.ic_star_border_black_24dp, "S'ABONNER", pendingIntentSub);


        notification.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        notification.setTicker("ok");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(title);
        notification.setContentText(desc);


        Intent intent = new Intent(this, DisplayDetailsIncidentActivity.class);
        intent.putExtra("incident", incident);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(),notification.build());
    }
}
