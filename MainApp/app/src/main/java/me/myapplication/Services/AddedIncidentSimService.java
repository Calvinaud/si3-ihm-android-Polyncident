package me.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Date;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Incident;

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
                for ( int i = 1; i < 4; i++){
                    synchronized (this) {
                        try {
                            wait(20000);
                            IncidentDBHelper.getSingleton().insertComm(1,1, new Date(), "cont");
                            IncidentDBHelper.getSingleton()
                                    .insertIncident(2, 3, i
                                            ,
                                            2, "Je suis ds le service aled", "Type = "+i,new byte[]{},2, Calendar.getInstance().getTime()
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
