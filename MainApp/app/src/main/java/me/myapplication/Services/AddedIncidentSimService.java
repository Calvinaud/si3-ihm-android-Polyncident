package me.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import me.myapplication.Helpers.IncidentDBHelper;

public class AddedIncidentSimService extends Service {
    public AddedIncidentSimService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                for ( int i = 0; i < 2; i++){
                    synchronized (this) {
                        try {
                            wait(10000);
                            IncidentDBHelper.getSingleton()
                                    .insertIncident(0, 3, 1
                                            ,
                                            2, "Je suis ds le service aled", "Il n'y a plus de co aled !"
                                    );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread notifThread = new Thread(r);
        notifThread.start();
        return Service.START_STICKY;
    }
}