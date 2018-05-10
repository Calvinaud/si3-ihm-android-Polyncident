package me.myapplication.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import me.myapplication.MainActivity;
import me.myapplication.R;

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
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<5; i++){
                    long futureTime=System.currentTimeMillis()+5000;
                    while(System.currentTimeMillis() < futureTime){
                        synchronized (this){
                            try{
                                wait(futureTime-System.currentTimeMillis());
                                sendNotif();
                            }catch (Exception e){}
                        }
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

    public void sendNotif(){
        notification.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        notification.setTicker("this is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Here is the title");
        notification.setContentText("I am the body");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueID,notification.build());

    }
}
